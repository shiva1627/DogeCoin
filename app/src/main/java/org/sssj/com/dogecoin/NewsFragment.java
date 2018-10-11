package org.sssj.com.dogecoin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    RecyclerView NewsRecyclerView;
    private static final String TAG = "NewsFragment";
    List<News> newsList = new ArrayList<>();
    RequestQueue requestQueue;
    String News_Url = "http://sscoinmedia.tech/DogeWebService/dogeNews.php";
    private NativeAd nativeAd;

    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    View view;

    /**
     * StartAppAd object declaration
     */
    private StartAppAd startAppAd = new StartAppAd(getContext());

    /**
     * StartApp Native Ad declaration
     */
    // private StartAppNativeAd sa_NativeAds;
    //  private NativeAdDetails sa_nativeAd_Details = null;

    //  private ImageView sa_ads_img = null;

    //   private TextView sa_ads_name = null;
    //  private TextView sa_Description = null;
//    RelativeLayout sa_ads_layout;
    //   Button sa_native_ad_call_to_action;

    /**
     * Native Ad Callback
     */
 /*   private AdEventListener nativeAdListener = new AdEventListener() {

        @Override
        public void onReceiveAd(com.startapp.android.publish.adsCommon.Ad ad) {

            // Get the native ad
            ArrayList<NativeAdDetails> nativeAdsList = sa_NativeAds.getNativeAds("ssD_NewsNativeAd");
            if (nativeAdsList.size() > 0) {
                sa_nativeAd_Details = nativeAdsList.get(0);
            }

            // Verify that an ad was retrieved
            if (sa_nativeAd_Details != null) {

                // When ad is received and displayed - we MUST send impression
                sa_nativeAd_Details.sendImpression(getActivity());

                if (sa_ads_img != null && sa_ads_name != null) {

                    // Set button as enabled
                    sa_ads_img.setEnabled(true);
                    sa_ads_name.setEnabled(true);
                    sa_ads_layout.setVisibility(View.VISIBLE);


                    // Set ad's image
                    sa_ads_img.setImageBitmap(sa_nativeAd_Details.getImageBitmap());

                    // Set ad's title
                    sa_ads_name.setText(sa_nativeAd_Details.getTitle());
                    sa_Description.setText(sa_nativeAd_Details.getDescription());
                }
            }
        }

        @Override
        public void onFailedToReceiveAd(com.startapp.android.publish.adsCommon.Ad ad) {

            // Error occurred while loading the native ad
            if (sa_ads_name != null) {
                sa_ads_name.setText("Error while loading Native Ad");
                Log.i("StartAppX", ad.getErrorMessage());
                sa_ads_layout.setVisibility(View.GONE);

            }
        }
    };*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.news_fragments, container, false);
        StartAppSDK.init(getActivity(), "207596372", false);
        //  sa_NativeAds = new StartAppNativeAd(getActivity());
/** Initialize Native Ad views **/
/** Initialize Native Ad views **/
     /*   sa_ads_layout = (RelativeLayout) view.findViewById(R.id.sa_ads_layout);
        sa_ads_layout.setVisibility(View.GONE);
        sa_ads_img = (ImageView) view.findViewById(R.id.sa_main_ads_img);
        sa_ads_name = (TextView) view.findViewById(R.id.sa_ads_app_name);
        sa_Description = (TextView) view.findViewById(R.id.sa_native_ad_description);
        sa_native_ad_call_to_action = (Button) view.findViewById(R.id.sa_native_ad_call_to_action);

        if (sa_ads_name != null) {
            sa_ads_name.setText("Loading Native Ad...");

        }
        sa_ads_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sa_Ads_AppClick();
            }
        });
        sa_ads_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sa_Ads_AppClick();
            }
        });
        sa_ads_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sa_Ads_AppClick();
            }
        });
        sa_Description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sa_Ads_AppClick();
            }
        });
        sa_native_ad_call_to_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sa_Ads_AppClick();
            }
        });
        sa_NativeAds.loadAd(
                new NativeAdPreferences()
                        .setAdsNumber(1)
                        .setAutoBitmapDownload(true)
                        .setPrimaryImageSize(0),

                nativeAdListener);
*/
        NewsRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        NewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //NewsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        AdSettings.addTestDevice("c0f52b1d-f324-468d-a61e-429fc386e0a0");
        requestQueue = MySingleton.getInstance(getActivity()).getRequestQueue();  //using singleton object
        Show_News();
        loadNativeAd();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void Show_News() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, News_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // for 1st array url
                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject o = jsonArray.getJSONObject(i);
                                Log.i("MyTest1", "--> " + o + "");

                                News Object_news = new News(o.getString("subject"), o.getString("date"), o.getString("details"));
                                newsList.add(Object_news);
                            }
                            NewsRecyclerView.setAdapter(new NewsAdapter(newsList, getContext()));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        // for 2st employee object url

                        /*try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("employees");
                            Log.i("ZZZZZ", "" + jsonArray.length());


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject o = jsonArray.getJSONObject(i);
                                Employee e = new Employee(o.getString("Name"), o.getString("Mobile"), o.getString("Address"));
                                empList.add(e);
                            }
                            NewsRecyclerView.setAdapter(new MyAdapter(empList, getApplicationContext()));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }*/

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void loadNativeAd() {
        nativeAd = new NativeAd(getActivity(), "239164800060456_239166033393666");

        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAd);
            }


        });

        // Request an ad
        nativeAd.loadAd();
    }

    private void inflateAd(NativeAd nativeAd) {
        //  Toast.makeText(getActivity(), "Nativeads Loaded", Toast.LENGTH_SHORT).show();

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdContainer = view.findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdContainer, false);
        nativeAdContainer.addView(adView);

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = view.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(getActivity(), nativeAd, true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.

        ImageView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);

//        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        /*nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());*/
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getAdSubtitle());

        // Download and display the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Download and display the cover image.
        nativeAdMedia.setNativeAd(nativeAd);
/*
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
*/

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        //  nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews);
        nativeAd.registerViewForInteraction(adView, clickableViews);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //   Toast.makeText(getActivity(), "OnDestroy", Toast.LENGTH_SHORT).show();
    }

   /* public void sa_Ads_AppClick() {
        if (sa_nativeAd_Details != null) {
            sa_nativeAd_Details.sendClick(getContext());
        }
    }*/

}
