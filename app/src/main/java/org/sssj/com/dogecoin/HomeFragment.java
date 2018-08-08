package org.sssj.com.dogecoin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.NativeAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    String Claim_url = "http://sscoinmedia.tech/DogeWebService/dogeBalanceUpdate.php";
    String Claim_timer_url = "http://sscoinmedia.tech/DogeWebService/dogeClaimTimer.php";


    CountDownTimer countdt;
    TextView txtEmail, txtubal, txtClaimRate, txtlastclaim;
    FirebaseAuth mAuth;
    Button btnclaim;
  //  private InterstitialAd mInterstitialAd;

    private InterstitialAd interstitialAd;
    private AdView adView;

    RequestQueue requestQueue;

    private ProgressBar spinner2;
    boolean fromMain = false;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    private LinearLayout nativeadView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

      //  AdSettings.addTestDevice("c0f52b1d-f324-468d-a61e-429fc386e0a0");
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        requestQueue = MySingleton.getInstance(getActivity()).getRequestQueue();

        spinner2 = (ProgressBar) view.findViewById(R.id.progressBar2);
        spinner2.setVisibility(View.GONE);


        txtClaimRate = (TextView) view.findViewById(R.id.txtclaimrate);
        txtlastclaim = (TextView) view.findViewById(R.id.txtlastclaim);
        txtubal = (TextView) view.findViewById(R.id.txtcurrentbal);
        btnclaim = (Button) view.findViewById(R.id.btnclaim);

        btnclaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner2.setVisibility(View.VISIBLE);
                Claim_Doge();
                btnclaim.setEnabled(false);

            }
        });

        Intent in = getActivity().getIntent();
        txtubal.setText(in.getStringExtra("ubal"));


        // Instantiate an AdView view
        adView = new AdView(getActivity(), " 239164800060456_239842343326035", AdSize.BANNER_HEIGHT_50);
        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) view.findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();



        // Instantiate an InterstitialAd object
        interstitialAd = new InterstitialAd(getActivity(), "239164800060456_239844109992525");

        // load the ad
        interstitialAd.loadAd();


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // txtEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtemailid);
        //  txtEmail.setText(user.getEmail().toString());

        return view;
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        // FirebaseAuth.getInstance().signOut();
    }*/


    private void Claim_Doge() {

        StringRequest addStringRequest = new StringRequest(Request.Method.POST, Claim_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spinner2.setVisibility(View.GONE);
                Log.i("RESPONCEX", "Claim " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("ZZZZZ", "" + jsonObject.get("success"));
                    txtlastclaim.setText(getString(R.string.lastclaim) + " " + jsonObject.get("claimamt"));
                    Toast.makeText(getActivity(), "You got " + jsonObject.get("claimamt") + " Doge", Toast.LENGTH_SHORT).show();
                    startCountdown(300000, 100);
                    txtubal.setText(jsonObject.get("ubal").toString());
                    Long tsLong = System.currentTimeMillis();
                    // String ts = currentTime.toString();

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putLong("LastClaim", tsLong);
                    editor.putString("lastBalance", jsonObject.get("ubal").toString());

                    editor.commit();

                    // requestQueue.stop();

                   load_interstitial();

                } catch (JSONException e) {
                   load_interstitial();

                    btnclaim.setEnabled(true);
                    // requestQueue.stop();
                    spinner2.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error " + e, Toast.LENGTH_SHORT).show();
                    Log.i("ZZZZZ", " Err " + e);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                load_interstitial();

                // requestQueue.stop();
                btnclaim.setEnabled(true);
                spinner2.setVisibility(View.GONE);
                Log.i(TAG, "Error " + error);
                Toast.makeText(getActivity(), "try again... ", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email", mAuth.getCurrentUser().getEmail());
                param.put("claimok", "ok");
                return param;
            }
        };
        requestQueue.add(addStringRequest);
    }

    private void load_interstitial() {
        // Show the ad
        interstitialAd.show();
    }


    public void startCountdown(long time, long interval) {
        countdt = new CountDownTimer(time, interval) {
            public void onTick(long millisUntilFinished) {

                String file_duration = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                );
                btnclaim.setText(file_duration);
            }

            public void onFinish() {
                btnclaim.setEnabled(true);
                btnclaim.setText("Claim");
            }
        };
        countdt.start();
    }

    @Override
    public void onResume() {
        load_interstitial();

        // Request an ad
        adView.loadAd();
        super.onResume();
        spinner2.setVisibility(View.GONE);
        if (!btnclaim.getText().equals("Claim")) {
            Log.i("Claim_Timer", btnclaim.getText() + "  Not Equal ");
        } else {
            Log.i("Claim_Timer", "Equal ");
            Claim_Timer();
        }

    }

    private void Claim_Timer() {
        StringRequest addStringRequest = new StringRequest(Request.Method.POST, Claim_timer_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spinner2.setVisibility(View.GONE);
                Log.i("Claim_Timer", "Response " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    txtClaimRate.setText(jsonObject.getString("claimrange"));
                    Log.i("RESPONCEX", "Timer " + response);

                    Log.i("Claim_Timer", "Diff ->" + jsonObject.get("diff"));
                    txtubal.setText(jsonObject.getDouble("ubal") + "");

                    int diffTime = jsonObject.getInt("diff");
                    if (diffTime < 300) {
                        btnclaim.setEnabled(false);
                        // countdt.cancel();
                        startCountdown((300 - diffTime) * 1000, 100);
                    } else {
                        btnclaim.setEnabled(true);
                        Log.i("Claim_Timer", "Diff in  ->");
                    }

                   /*//* if (diffTime > 601 && fromMain) {
                    //  if (mAuth.getCurrentUser() == null) {
                    getActivity().finish();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    //   }
                }*/

                    //   txtubal.setText(jsonObject.get("ubal").toString());

                } catch (JSONException e) {
                    spinner2.setVisibility(View.GONE);
                    Log.i("Claim_Timer", " Err " + e);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinner2.setVisibility(View.GONE);
                Log.i(TAG, "Error " + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email", mAuth.getCurrentUser().getEmail());
                return param;
            }
        };
        requestQueue.add(addStringRequest);
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }

        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }
}
