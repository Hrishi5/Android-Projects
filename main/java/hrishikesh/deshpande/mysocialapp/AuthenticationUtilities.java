package hrishikesh.deshpande.mysocialapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;


public class AuthenticationUtilities {
    public static String googleDisplayName ;
    public static String googleEmailId ;
    public static String authDisplayName ;
    public static String authEmailId ;
    public static void signout(final Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance() ;
        if(mAuth != null) {
            mAuth.signOut();
        }else{
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            GoogleApiClient mGoogle = new GoogleApiClient.Builder(context)
                    .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                    .build();

            // Google sign out
            Auth.GoogleSignInApi.signOut(mGoogle).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                        Utilities.$t(context,"Signed Out!");
                        }
                    });

        }
    }

    public static String getDisplayName() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance() ;
        if(mAuth!=null && mAuth.getCurrentUser()!=null) {
            return authDisplayName ;   //mAuth.getCurrentUser().getDisplayName();
        }else{
            Utilities.$l("json",googleDisplayName);
            return googleDisplayName ;
        }

    }

    public static String getEmailId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance() ;
        if(mAuth != null && mAuth.getCurrentUser()!=null) {
            return authEmailId ; //mAuth.getCurrentUser().getEmail();
        }else{
            Utilities.$l("json",googleEmailId);
            return googleEmailId ;
        }
    }

}
