package hrishikesh.deshpande.mysocialapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth mAuth1 ;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    int age;
    static final int DIALOG_ID=0;
    EditText DOB;
    String currentDateTimeString;
    int currentDay,currentYear,currentMonth;
    Context context ;
    String displayName ;
    String userId ;
    userInfo info ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        context = this ;
        //   currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//        Log.d("demo",currentDateTimeString);
        displayName = AuthenticationUtilities.getDisplayName() ;
        userId = AuthenticationUtilities.getEmailId() ;
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
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        currentYear= calendar.get(Calendar.YEAR);
        currentDay = calendar.get(Calendar.DATE);
        currentMonth = calendar.get(Calendar.MONTH);

        showDialog();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user") ;
        reference.orderByKey().equalTo(userId.replace('.','1')).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Utilities.$l("json","edit profile " + dataSnapshot);
                info = dataSnapshot.child(userId.replace('.','1')).getValue(userInfo.class) ;
                if(info != null) {
                    EditText fname =(EditText)findViewById(R.id.fnameEP);
                    fname.setText(info.getFirstName());
                    EditText lname =(EditText)findViewById(R.id.lnameEP);
                    lname.setText(info.getLastName()) ;
                    DOB =(EditText)findViewById(R.id.dobEP);
                    DOB.setText(info.getDataOfBirth());
                    TextView tv = Utilities.$tv(R.id.emailEP,EditProfile.this) ;
                    tv.setText(info.getEmail());
                }else{
                    Utilities.$t(context,"No valid information found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;
        findViewById(R.id.cancelEP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UserWall.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.signupEP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText fname =(EditText)findViewById(R.id.fnameEP);
                EditText lname =(EditText)findViewById(R.id.lnameEP);
                DOB =(EditText)findViewById(R.id.dobEP);

                if("".equalsIgnoreCase(fname.getText().toString().trim()) || "".equalsIgnoreCase(lname.getText().toString().trim())

                        ){
                    Toast.makeText(context,"All fields are mandatory",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!(age>13)){
                    Toast.makeText(context,"AGE SHOULD BE GREATER THAN 13",Toast.LENGTH_LONG).show();
                    return;
                }

                info.setFirstName(fname.getText().toString().trim());
                info.setLastName(lname.getText().toString().trim());
                info.setDataOfBirth(DOB.getText().toString().trim());
                info.setEmail(userId);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user") ;
                try{
                reference.child(userId.replace('.','1')).setValue(info) ;
                    Utilities.$t(context,"Profile edited !");
                    Utilities.$si(context,UserWall.class,null) ;
                }catch (Exception e) {
                    Utilities.$t(context,"Failed to edit profile !");
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            /*case R.id.appLogout:
                Utilities.$t(context,"Logged Out!");
                AuthenticationUtilities.signout(context);
                Utilities.$si(context,MainActivity.class,null) ;
                return true;*/
            case R.id.logout:
                Utilities.$t(context,"Logged Out!");
                AuthenticationUtilities.signout(context);
                Utilities.$si(context,MainActivity.class,null) ;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        DOB =(EditText)findViewById(R.id.dobEP);
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


