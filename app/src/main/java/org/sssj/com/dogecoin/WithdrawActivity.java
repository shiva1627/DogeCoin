package org.sssj.com.dogecoin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class WithdrawActivity extends AppCompatActivity {
    private static Double minBalance = 15.0;
    private static Double maxBalance = 25.0;
    private static Double WDFees = 1.0;

    String Curr_Bal_URL = "http://sscoinmedia.tech/DogeWebService/dogeClaimTimer1.php";
    String Withdraw_URL = "http://sscoinmedia.tech/DogeWebService/dogeAddrequest.php";

    private com.facebook.ads.AdView adView;
    RequestQueue requestQueue;
    FirebaseAuth mAuth;

    //  ProgressDialog progressDialog;
    private AlertDialog progressDialog;

    TextView txtCurrentBalance;
    Button btnwithRequest;
    Double currentBal;
    EditText edAmount, edEstiAmt, edDogeAddr;
    Double WDAmt, WDEstiAmt;
    String DogeAddr;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefseditor;


    String deviceId = "not find";
    TelephonyManager telephonyManager;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        txtCurrentBalance = (TextView) findViewById(R.id.txtwbalance2);
        edAmount = (EditText) findViewById(R.id.edwithAmt);
        edEstiAmt = (EditText) findViewById(R.id.edwithEstimatedAmt);
        edDogeAddr = (EditText) findViewById(R.id.edwithDogeAddress);
        prefs = getSharedPreferences("startappCount", Context.MODE_PRIVATE);
        prefseditor = prefs.edit();
        prefseditor.putInt("startappCount", 1);
        prefseditor.apply();


        telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();




        // Instantiate an AdView view
        adView = new com.facebook.ads.AdView(getApplicationContext(), " 239164800060456_239842343326035", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

      /*  progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");*/
        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.show();
        edAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    try {
                        Double edamt = Double.parseDouble(edAmount.getText().toString());
                        if (edamt < currentBal && edamt >= minBalance && edamt <= maxBalance) {
                            WDAmt = Double.parseDouble(edAmount.getText().toString());
                            WDEstiAmt = WDAmt - WDFees;
                            edEstiAmt.setText(String.valueOf(WDEstiAmt));
                        } else {
                            edAmount.setText("");
                            edEstiAmt.setText("");
                            if (edamt > maxBalance) {
                                Toast toast = Toast.makeText(WithdrawActivity.this, "You can request max 25 Doge per request...", Toast.LENGTH_LONG);
                                // Here we can set the Gravity to Top and Right
                                toast.setGravity(Gravity.CENTER, 0, -200);
                                toast.show();
                            }else if (edamt < minBalance) {
                                Toast toast = Toast.makeText(WithdrawActivity.this, "You can request min 15 Doge per request...", Toast.LENGTH_LONG);
                                // Here we can set the Gravity to Top and Right
                                toast.setGravity(Gravity.CENTER, 0, -200);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(WithdrawActivity.this, "You haven't enough balance to request...", Toast.LENGTH_LONG);
                                // Here we can set the Gravity to Top and Right
                                toast.setGravity(Gravity.CENTER, 0, -200);
                                toast.show();
                            }
                        }
                    } catch (NumberFormatException e) {
                        edAmount.setText("");
                        edEstiAmt.setText("");
                        Toast.makeText(WithdrawActivity.this, "Please Enter valid amount", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        btnwithRequest = (Button) findViewById(R.id.btnwithRequest);

        btnwithRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                DogeAddr = edDogeAddr.getText().toString();
                if (WDAmt != null && WDEstiAmt != null && !DogeAddr.isEmpty()) {
                    Withdraw_Doge();
                    //Toast.makeText(WithdrawActivity.this, "You can get " + WDEstiAmt + " Doge", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(WithdrawActivity.this, "Please Filled All Fields", Toast.LENGTH_SHORT).show();

                }
            }
        });

        mAuth = FirebaseAuth.getInstance();

        requestQueue = MySingleton.getInstance(this).getRequestQueue();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Check_Curr_Balance();
    }

    private void Check_Curr_Balance() {
        StringRequest addStringRequest = new StringRequest(Request.Method.POST, Curr_Bal_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.i("Claim_Timer", "Response " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    currentBal = jsonObject.getDouble("ubal");
                    txtCurrentBalance.setText(currentBal + "");
                    if (currentBal > 0) {
                        edAmount.setEnabled(true);
                        edDogeAddr.setEnabled(true);
                        btnwithRequest.setEnabled(true);
                    }


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error " + e, Toast.LENGTH_SHORT).show();
                    Log.i("Claim_Timer", " Err " + e);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("WithdrawActivity", "Error " + error);
                Toast.makeText(getApplicationContext(), "Try again... ", Toast.LENGTH_SHORT).show();

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

    private void Withdraw_Doge() {
        StringRequest addStringRequest = new StringRequest(Request.Method.POST, Withdraw_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                Log.i("WithdrawActivity", "Response " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getInt("success") == 1) {
                        txtCurrentBalance.setText(jsonObject.get("newBal") + "");
                        Log.i("XXXXX", "NewBal " + jsonObject.get("newBal"));
                        currentBal=jsonObject.getDouble("newBal");
                        Toast.makeText(WithdrawActivity.this, "Request Successfully placed !!!", Toast.LENGTH_LONG).show();
                        edDogeAddr.setText("");
                        edAmount.setText("");
                        edEstiAmt.setText("");
                    }



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Try again... ", Toast.LENGTH_SHORT).show();
                    Log.i("Claim_Timer", " Err " + e);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("WithdrawActivity", "Error " + error);
                Toast.makeText(getApplicationContext(), "Try again... " , Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email", mAuth.getCurrentUser().getEmail());
                param.put("reqamt", WDAmt.toString());
                param.put("fees", WDFees + "");
                param.put("dogeaddress", DogeAddr);
                return param;
            }
        };
        requestQueue.add(addStringRequest);
    }
}
