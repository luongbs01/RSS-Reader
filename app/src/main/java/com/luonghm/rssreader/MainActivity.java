package com.luonghm.rssreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private TextView emailAddress;
    private TextView logout_text_view;
    private TextView newspaper_text_view;
    private ImageView photo;
    private ImageView logout_image_view;
    private TextView displayName;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private int selectedTab = -1;
    private AdView mAdView;
    private int position;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private NavigationView navigationView;

    private LinkedHashMap<String, String> linkedHashMap;
    private Iterator<Map.Entry<String, String>> iterator;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("dark_mode", false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null)
            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                }
            });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem view) {
                view.setCheckable(false);
                drawerLayout.closeDrawers();
                Intent intent = new Intent();
                switch (view.getItemId()) {
                    case R.id.account:
                        if (FirebaseAuth.getInstance().getCurrentUser() == null || FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                            intent = new Intent(MainActivity.this, Account.class);
                        }
                        FirebaseAnalyticsUtils.putEventClick(getApplicationContext(), FirebaseAnalyticsUtils.PARAM_SIGN_IN, "click_sign_in_button");
                        break;
                    case R.id.log_out_layout:
                        if (FirebaseAuth.getInstance().getCurrentUser() != null && !FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                            intent = new Intent(MainActivity.this, Account.class);
                            intent.putExtra("SignOut", 1);
                            FirebaseAnalyticsUtils.putEventClick(getApplicationContext(), FirebaseAnalyticsUtils.PARAM_LOG_OUT, "click_log_out_button");
                        }
                        break;
                    case R.id.newspaper_text_view:
                        intent = new Intent(MainActivity.this, NewsListActivity.class);
                        FirebaseAnalyticsUtils.putEventClick(getApplicationContext(), FirebaseAnalyticsUtils.PARAM_ARTICLE_TYPE, "click_article_type");
                        break;
                    case R.id.read_post:
                        intent = new Intent(MainActivity.this, MainActivity3.class);
                        FirebaseAnalyticsUtils.putEventClick(getApplicationContext(), FirebaseAnalyticsUtils.PARAM_READ_POST, "click_read_post");
                        break;
                    case R.id.saved_post:
                        intent = new Intent(MainActivity.this, MainActivity4.class);
                        FirebaseAnalyticsUtils.putEventClick(getApplicationContext(), FirebaseAnalyticsUtils.PARAM_SAVED_POST, "click_saved_post");
                        break;
                    case R.id.review:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                        FirebaseAnalyticsUtils.putEventClick(getApplicationContext(), FirebaseAnalyticsUtils.PARAM_REVIEW, "click_rate_app");
                        break;
                    case R.id.email:
                        intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"luongbs01@gmail.com"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Đóng góp ý kiến cho ứng dụng");
                        FirebaseAnalyticsUtils.putEventClick(getApplicationContext(), FirebaseAnalyticsUtils.PARAM_REVIEW, "click_review");
                        break;

                    case R.id.settings:
                        intent = new Intent(MainActivity.this, SettingsActivity.class);
                        FirebaseAnalyticsUtils.putEventClick(getApplicationContext(), FirebaseAnalyticsUtils.PARAM_SETTINGS, "click_settings");
                        break;
                }
                startActivity(intent);
                return true;
            }
        });


        linkedHashMap = new LinkedHashMap<>();
        position = sharedPreferences.getInt("Newspaper_list", 0);
        linkedHashMap.putAll(new NewsPaperList().getItem(position).getTab());
        iterator = linkedHashMap.entrySet().iterator();

        View headerLayout = navigationView.getHeaderView(0);
        tabLayout = findViewById(R.id.tablayout);
        emailAddress = headerLayout.findViewById(R.id.emailAddress);
        displayName = headerLayout.findViewById(R.id.displayName);
        photo = headerLayout.findViewById(R.id.photo);
        logout_text_view = headerLayout.findViewById(R.id.log_out);
        logout_image_view = headerLayout.findViewById(R.id.log_out_image_view);
        newspaper_text_view = findViewById(R.id.newspaper_text_view);

        while (iterator.hasNext()) {
            tabLayout.addTab(tabLayout.newTab().setText(iterator.next().getKey()));
        }

        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        MobileAds.initialize(this, initializationStatus -> {

        });


        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(3)
                .session(7)
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {

                    }
                }).build();

        ratingDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAdView = findViewById(R.id.adView);
        //TODO: set adUnitId
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (position == sharedPreferences.getInt("Newspaper_list", 0)) {
            selectedTab = tabLayout.getSelectedTabPosition();
            tabLayout.getTabAt(selectedTab).select();
        } else {
            linkedHashMap = new LinkedHashMap<>();
            position = sharedPreferences.getInt("Newspaper_list", 0);
            linkedHashMap.putAll(new NewsPaperList().getItem(position).getTab());
            iterator = linkedHashMap.entrySet().iterator();
            tabLayout.removeAllTabs();
            while (iterator.hasNext()) {
                tabLayout.addTab(tabLayout.newTab().setText(iterator.next().getKey()));
            }
            tabLayout.getTabAt(0).select();
        }
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void Process(View view) {
        drawerLayout.closeDrawers();
        Intent intent;
        switch (view.getId()) {
            case R.id.account:
                if (FirebaseAuth.getInstance().getCurrentUser() == null || FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                    intent = new Intent(this, Account.class);
                    startActivity(intent);
                }
                break;
            case R.id.log_out_layout:
                if (FirebaseAuth.getInstance().getCurrentUser() != null && !FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                    intent = new Intent(this, Account.class);
                    intent.putExtra("SignOut", 1);
                    startActivity(intent);
                }
                break;
        }
    }

    private void updateUI() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        toolbar.setTitle((new NewsPaperList().getItem(sharedPreferences.getInt("Newspaper_list", 0)).getName()));
        if (currentUser == null || currentUser.isAnonymous()) {
            emailAddress.setText("Đăng nhập");
            displayName.setVisibility(View.INVISIBLE);
            photo.setImageDrawable(getResources().getDrawable(R.drawable.ic_account));
            logout_text_view.setVisibility(View.INVISIBLE);
            logout_image_view.setVisibility(View.INVISIBLE);
        } else {
            emailAddress.setText(currentUser.getEmail());
            displayName.setVisibility(View.VISIBLE);
            displayName.setText(currentUser.getDisplayName());
            Glide.with(getApplicationContext()).load(currentUser.getPhotoUrl()).into(photo);
            logout_text_view.setVisibility(View.VISIBLE);
            logout_image_view.setVisibility(View.VISIBLE);
        }
    }
}




