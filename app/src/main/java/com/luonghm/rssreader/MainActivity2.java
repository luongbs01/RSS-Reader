package com.luonghm.rssreader;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {
    private String title;
    private String image;
    private String pubDate;
    private String link;
    private RSSModel rssModel;
    private DbReadPosts dbReadPosts = new DbReadPosts(this);
    private SharedPreferences sharedPreferences;
    private AdView mAdView;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        WebView webView = findViewById(R.id.webview);
        title = intent.getStringExtra("title");
        image = intent.getStringExtra("image");
        pubDate = intent.getStringExtra("pubDate");
        link = intent.getStringExtra("link");

        rssModel = new RSSModel(title, image, pubDate, link);
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString("text_size", "1").equalsIgnoreCase("0"))
            webView.getSettings().setTextZoom(80);
        else if (sharedPreferences.getString("text_size", "1").equalsIgnoreCase("1"))
            webView.getSettings().setTextZoom(100);
        else webView.getSettings().setTextZoom(120);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            if (sharedPreferences.getBoolean("dark_mode", false))
                webView.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);
            else webView.getSettings().setForceDark(WebSettings.FORCE_DARK_OFF);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.save);

        if (dbReadPosts.check(rssModel, DbReadPosts.TABLE_NAME2)) {
            item.setIcon(R.drawable.ic_bookmark);
        } else {
            item.setIcon(R.drawable.ic_bookmark_done);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                ShareCompat.IntentBuilder.from(this).setType("text/plain").setChooserTitle("Share this article's link with:").setText(link).startChooser();
                return true;
            case R.id.save:
                if (dbReadPosts.check(rssModel, DbReadPosts.TABLE_NAME2)) {
                    FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                            child(DbReadPosts.TABLE_NAME2).child(rssModel.getTitle()).setValue(rssModel);
                    dbReadPosts.insert(rssModel, DbReadPosts.TABLE_NAME2);
                    item.setIcon(R.drawable.ic_bookmark_done);
                } else {
                    FirebaseDatabase.getInstance().getReference()
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(DbReadPosts.TABLE_NAME2)
                            .child(rssModel.getTitle()).removeValue();
                    dbReadPosts.delete(rssModel, DbReadPosts.TABLE_NAME2);
                    item.setIcon(R.drawable.ic_bookmark);
                }
                return true;
            default:
                return true;
        }
    }
}