package hrishikesh.deshpande.mysocialapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by Hrishikesh Deshpande on 21-Nov-17.
 */

public class Utilities {

    public static void $t(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void $l(String tag,Object message) {
        Log.d(tag,String.valueOf(message)) ;
    }

    public static void $si(Context context, Class toClass, Map<String,Object> intentExtras) {
        Intent intent = new Intent(context,toClass) ;
        if(intentExtras != null) {
            for(Map.Entry<String,Object> entry : intentExtras.entrySet()) {

                //intent.putExtra(entry.getKey(),entry.getValue()) ;
            }
        }
        context.startActivity(intent) ;
    }

    public static void confirmationDialog(Context context,String prompt,DialogInterface.OnClickListener positiveAction) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(prompt);
        builder1.setCancelable(true);
        if(positiveAction != null) {
            builder1.setPositiveButton(
                    "Yes",
                    positiveAction
            );
        }
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static Object getIntentData(Intent intent,String tag) {
        if(intent !=null && intent.getExtras()!=null && intent.getExtras().containsKey(tag)) {
            return intent.getExtras().get(tag) ;
        }
        return null ;
    }

    public static String getStringIntentData(Intent intent,String tag) {
        Object o = getIntentData(intent,tag) ;
        if(o == null) {
            return null ;
        }
        return String.valueOf(o) ;
    }

    public static TextView $tv(int id,Activity context) {
        return (TextView)context.findViewById(id) ;
    }

    public static EditText $et(int id,Activity activity) {
        return (EditText)activity.findViewById(id) ;
    }


}
