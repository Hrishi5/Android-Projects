package hrishikesh.deshpande.mysocialapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth1 ;
    EditText email ;
    EditText password ;
    public  static String currentUserEmail;
    public  static String currentUserName;
    GoogleApiClient mGoogle;
    FirebaseDatabase database = FirebaseDatabase.getInstance() ;
    DatabaseReference mrrot = database.getReference("user") ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        mAuth1 = FirebaseAuth.getInstance() ;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date past = format.parse("2017-11-18 00:02:54");
            Log.d("demo",""+past.getHours());
            PrettyTime p = new PrettyTime();
            Calendar c= Calendar.getInstance();
            Log.d("demo",""+c.getTime());
            Log.d("demo",p.format(past));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Signup.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogle = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        findViewById(R.id.signinbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email =(EditText) findViewById(R.id.email) ;
                String emailstr = null ;
                if(email == null ||
                        "".equalsIgnoreCase(email.getText().toString())) {
                    Toast.makeText(MainActivity.this,"Email cannot be empty !",Toast.LENGTH_SHORT).show();
                    return ;
                }else{
                    emailstr = email.getText().toString() ;
                }
                password =(EditText) findViewById(R.id.password) ;
                String passwordstr = password.getText().toString() ;
                currentUserEmail=emailstr;
                if(passwordstr == null || "".equalsIgnoreCase(passwordstr.trim())) {
                    Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth1.signInWithEmailAndPassword(emailstr,passwordstr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mrrot.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot == null) {
                                        // Toast.makeText(requestManager.this, "No Pending Request!", Toast.LENGTH_SHORT).show();
                                        //return;
                                    }else{
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            Log.d("json", "check " + child.getValue(userInfo.class));
                                            userInfo fr = child.getValue(userInfo.class);
                                            if (mAuth1.getCurrentUser().getEmail().trim().equalsIgnoreCase(fr.getEmail().trim())) {
                                                currentUserName=fr.getFirstName();
                                                AuthenticationUtilities.authDisplayName=fr.getFirstName() ;
                                                Log.d("json",mAuth1.getCurrentUser().getEmail());
                                                currentUserEmail=mAuth1.getCurrentUser().getEmail();
                                                AuthenticationUtilities.authEmailId = mAuth1.getCurrentUser().getEmail() ;
                                                //currentUserName=mAuth1.getCurrentUser().getDisplayName();
                                                Toast.makeText(MainActivity.this, "Logged In !", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(MainActivity.this,UserHome.class);
                                                startActivity(intent);
                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }else{
                            Toast.makeText(MainActivity.this, "Login Failed !", Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent(MainActivity.this,requestManager.class);
                            //startActivity(intent);
                            return ;
                        }
                    }
                }) ;

            }
        });
    }

    private void signIn(){
        Intent signinIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogle);
        startActivityForResult(signinIntent,1001);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1001){
            GoogleSignInResult result  = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount acc = result.getSignInAccount();
            userInfo info = new userInfo();
            info.setFirstName(acc.getGivenName());
            info.setLastName(acc.getFamilyName());
            info.setEmail(acc.getEmail());
            mrrot.child(acc.getEmail().replace('.','1')).setValue(info);
            currentUserEmail=acc.getEmail();
            currentUserName=acc.getGivenName();
            AuthenticationUtilities.googleDisplayName = acc.getGivenName() ;
            Utilities.$l("json",acc.getDisplayName() + " " + acc.getGivenName()) ;
            AuthenticationUtilities.googleEmailId = acc.getEmail() ;
            Utilities.$l("json",acc.getEmail());
            Intent intent = new Intent(MainActivity.this,UserHome.class);
            startActivity(intent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
