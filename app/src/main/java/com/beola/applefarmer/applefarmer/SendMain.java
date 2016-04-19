package com.beola.applefarmer.applefarmer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SendMain extends AppCompatActivity {
    private static final int PERMISSION_SEND_SMS = 1;
    public Context mContext;

        public SendMain(Context context) {
            mContext = context;
        }

        public void sendMain(final String phoneNumber, final float appleNumber, final int clickNumber) {
            // Check SMS permission
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                // request permission (see result in onRequestPermissionsResult() method)
                ActivityCompat.requestPermissions(SendMain.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        PERMISSION_SEND_SMS);
                //Toast.makeText(mContext, "Autoriser les sms ?", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Envoyer votre score au numéro suivant :\n" + phoneNumber)
                        .setTitle("Envoyer votre score")
                        .setIcon(R.drawable.ic_apple_button)
                        .setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendSMSMessage(phoneNumber, appleNumber, clickNumber);
                            }
                        });
                builder.show();
            }
        }

    protected void sendSMSMessage(String phoneNumber, float appleNumber, int clickNumber) {
        try {
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, "Je possède " + appleNumber + " pommes et j'ai cliqué " + clickNumber + " fois !", null, null);
            Toast.makeText(mContext, "SMS envoyé !", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "Impossible d'envoyer un SMS. Veuillez reessayer plus tard.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_SEND_SMS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    //sendSMSMessage(phoneNumber, appleNumber, clickNumber);
                    Toast.makeText(mContext, "TEST - Envoyer un sms", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied
                    Toast.makeText(mContext, "Permissions non accordés", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
