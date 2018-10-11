package org.sssj.com.dogecoin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.ads.AdSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private SectionPageAdapter msectionPageAdapter;


    private ViewPager mViewPager;
    String personPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "207596372", false);
        StartAppSDK.setUserConsent (getApplicationContext(),
                "pas",
                System.currentTimeMillis(),
                true);
        StartAppAd.disableSplash();
        StartAppAd.disableAutoInterstitial();
        setContentView(R.layout.activity_profile);
        AdSettings.addTestDevice("d445f630-b640-4227-8b90-3acbfe7aa26d");
        msectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_email);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            navUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            personPhotoUrl = user.getPhotoUrl().toString();

        }


        ImageView imgUser = (ImageView) headerView.findViewById(R.id.nav_img);

        Glide.with(getApplicationContext()).load(personPhotoUrl)
                .thumbnail(0.5f)
                .into(imgUser);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_withdraw) {
            Intent in = new Intent(ProfileActivity.this, WithdrawActivity.class);
            startActivity(in);

        } else if (id == R.id.nav_aacount) {
            Intent in = new Intent(ProfileActivity.this, User_account.class);
            startActivity(in);

        } else if (id == R.id.nav_donate) {

            Toast.makeText(this, "We will add this feature soon...", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_help) {
            Intent in = new Intent(ProfileActivity.this, Help.class);
            startActivity(in);

        } else if (id == R.id.nav_contactus) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "sscoinmedia@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "SSDoge App Support Request #");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));

        } else if (id == R.id.nav_rateus) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.sssj.com.dogecoin")));

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            finish();
            Intent in = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(in);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsFragment(), "News");
        adapter.addFragment(new HomeFragment(), "Home");
        viewPager.setAdapter(adapter);

    }


}