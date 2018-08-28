package org.sssj.com.dogecoin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {
    private static final int PHONE_STATE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    boolean permission_given, firstRun;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);


        pref = PreferenceManager.getDefaultSharedPreferences(this);
        permission_given = pref.getBoolean("permission_given", false);

        if (!permission_given) {

            if (ActivityCompat.checkSelfPermission(SplashScreen.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.READ_PHONE_STATE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PHONE_STATE_PERMISSION_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Toast.makeText(SplashScreen.this, "Clicked no", Toast.LENGTH_SHORT).show();
                            /*Intent in=new Intent(SplashScreen.this,HowToUse.class);
                            startActivity(in);
                            finish();*/
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    //just request the permission
                    ActivityCompat.requestPermissions(SplashScreen.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PHONE_STATE_PERMISSION_CONSTANT);
                }

            } else {

                proceedAfterPermission();
            }
        } else {

            proceedAfterPermission();
        }
    }


    private void proceedAfterPermission() {
        editor = pref.edit();
        editor.putBoolean("permission_given ", true);
        Intent in = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PHONE_STATE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this, android.Manifest.permission.READ_PHONE_STATE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PHONE_STATE_PERMISSION_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent in = new Intent(SplashScreen.this, HowToUse.class);
                            in.putExtra("from", 1);
                            startActivity(in);
                            dialog.cancel();
                            finish();*/
                            Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();

                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(SplashScreen.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                //  proceedAfterPermission();
            }
        }
    }
}