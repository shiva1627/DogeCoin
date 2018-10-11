package org.sssj.com.dogecoin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startapp.android.publish.adsCommon.StartAppAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class User_account extends AppCompatActivity {
    FirebaseAuth mAuth;
    ImageView imgUser;
    TextView nameUser, mailUser, txtMainBal, txtCurrBal, txtDogeAddr;
    String personPhotoUrl;
    String MainBal = "http://sscoinmedia.tech/DogeWebService/dogeClaimTimer1.php";
    RequestQueue requestQueue;
    int startappCount = 0;
    String deviceId = "not find";
    TelephonyManager telephonyManager;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefseditor;
    private AlertDialog progressDialog;
    private StartAppAd startAppAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_account);
        startAppAd = new StartAppAd(this);
        prefs = getSharedPreferences("startappCount", Context.MODE_PRIVATE);
        prefseditor = prefs.edit();
        prefseditor.putInt("startappCount", 1);
        prefseditor.apply();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.show();

        telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();


        requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();


        imgUser = (ImageView) findViewById(R.id.user_pro_image);
        nameUser = (TextView) findViewById(R.id.txtUser_pro_name);
        mailUser = (TextView) findViewById(R.id.txtUser_pro_name_mail);
        txtMainBal = (TextView) findViewById(R.id.txttotalbal2);
        txtCurrBal = (TextView) findViewById(R.id.txtcurrbal2);
        txtDogeAddr = (TextView) findViewById(R.id.txtdoge2);

        if (user != null) {
            nameUser.setText(user.getDisplayName());
            mailUser.setText(user.getEmail());
            personPhotoUrl = user.getPhotoUrl().toString();

        }


        Glide.with(getApplicationContext()).load(personPhotoUrl)
                .thumbnail(0.5f)
                .into(imgUser);
        Load_Bal();

    }

    @Override
    protected void onStart() {
        super.onStart();
        startAppAd.showAd("ssD_UserA/cInterstetial"); // show the ad
        startAppAd.loadAd();

    }

    private void Load_Bal() {
        StringRequest addStringRequest = new StringRequest(Request.Method.POST, MainBal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.i("Claim_Timer", "Response " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    txtCurrBal.setText(jsonObject.getString("ubal"));
                    txtMainBal.setText(jsonObject.getString("total"));
                    txtDogeAddr.setText(jsonObject.getString("daddr"));

                } catch (JSONException e) {
                    progressDialog.dismiss();

                    Log.i("Claim_Timer", " Err " + e);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("XXXXX", "Error " + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email", mAuth.getCurrentUser().getEmail());
                param.put("devid", deviceId);

                return param;
            }
        };
        requestQueue.add(addStringRequest);
    }


}
