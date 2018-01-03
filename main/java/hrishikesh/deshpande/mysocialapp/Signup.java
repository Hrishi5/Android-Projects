package hrishikesh.deshpande.mysocialapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth1 ;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    int age;
    static final int DIALOG_ID=0;
    EditText DOB;
    String currentDateTimeString;
    int currentDay,currentYear,currentMonth;
    FirebaseDatabase database = FirebaseDatabase.getInstance() ;
    DatabaseReference mrrot = database.getReference("user") ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar t = (Toolbar) findViewById(R.id.tToolbar);
        ActionMenuView amvMenu = (ActionMenuView) t.findViewById(R.id.amvMenu);
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });

        setSupportActionBar(t);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //   currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//        Log.d("demo",currentDateTimeString);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        currentYear= calendar.get(Calendar.YEAR);
        currentDay = calendar.get(Calendar.DATE);
        currentMonth = calendar.get(Calendar.MONTH);

        showDialog();
        mAuth1 = FirebaseAuth.getInstance() ;
        findViewById(R.id.cancel1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this,MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.signup1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText pass =(EditText)findViewById(R.id.password1);
                EditText confirmPass =(EditText) findViewById(R.id.confirmpass);
                final EditText emailId = (EditText)findViewById(R.id.email1) ;
                final EditText fname =(EditText)findViewById(R.id.fname);
                final EditText lname =(EditText)findViewById(R.id.lname);
                DOB =(EditText)findViewById(R.id.DOB);

                if("".equalsIgnoreCase(fname.getText().toString().trim()) || "".equalsIgnoreCase(lname.getText().toString().trim())
                        || "".equalsIgnoreCase(emailId.getText().toString().trim()) || "".equalsIgnoreCase(pass.getText().toString().trim())
                        || "".equalsIgnoreCase(confirmPass.getText().toString().trim())){
                    Toast.makeText(Signup.this,"All fields are mandatory",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!(age>13)){
                    Toast.makeText(Signup.this,"AGE SHOULD BE GREATER THAN 13",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailId.getText().toString()).matches()){
                    Toast.makeText(Signup.this,"Email is invalid",Toast.LENGTH_LONG).show();
                    return;
                }
                if(pass.getText().toString().trim().equals(confirmPass.getText().toString().trim())) {
                    mAuth1.createUserWithEmailAndPassword(emailId.getText().toString(), pass.getText().toString()).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Signup.this, "User created !", Toast.LENGTH_SHORT).show();
                                userInfo info = new userInfo();
                                info.setFirstName(fname.getText().toString());
                                info.setLastName(lname.getText().toString());
                                info.setEmail(emailId.getText().toString());
                                info.setDataOfBirth(DOB.getText().toString());
                                mrrot.child(emailId.getText().toString().replace('.','1')).setValue(info);
                                Intent intent = new Intent(Signup.this,MainActivity.class);
                                startActivity(intent);

                                //  FirebaseUser user =mAuth.getCurrentUser();

                            } else {
                                Toast.makeText(Signup.this, "Signup Failed !", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }else{
                    Toast.makeText(Signup.this,"Password and repeated password doesn't match",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    public void showDialog(){
        DOB =(EditText)findViewById(R.id.DOB);
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    year=arg1;
                    month=arg2;
                    day =arg3;
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };
    private void showDate(int year, int month, int day) {
        DOB.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

        age = currentYear - year;

        if (month > currentMonth) {
            age--;
        } else if (month == currentMonth) {
            if (day > currentDay) {
                age--;
            }
        }
    }

}
