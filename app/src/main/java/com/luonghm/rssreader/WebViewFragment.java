package com.luonghm.rssreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class WebViewFragment extends Fragment {
    private View rootView;
    private String title;
    private String image;
    private String pubDate;
    private String link;
    private RSSModel rssModel;
    private DbReadPosts dbReadPosts;
    private SharedPreferences sharedPreferences;

    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(String title, String image, String pubDate, String link) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(DbReadPosts.TITLE, title);
        args.putString(DbReadPosts.IMAGE, image);
        args.putString(DbReadPosts.PUBDATE, pubDate);
        args.putString(DbReadPosts.LINK, link);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        dbReadPosts = new DbReadPosts(getContext());
        setHasOptionsMenu(true);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar2);
        ((AppCompatActivity) rootView.getContext()).setSupportActionBar(toolbar);

        Bundle intent = getArguments();
        WebView webView = rootView.findViewById(R.id.webview);
        title = intent.getString(DbReadPosts.TITLE);
        image = intent.getString(DbReadPosts.IMAGE);
        pubDate = intent.getString(DbReadPosts.PUBDATE);
        link = intent.getString(DbReadPosts.LINK);

        rssModel = new RSSModel(title, image, pubDate, link);

        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
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
    public void onDestroy() {
        super.onDestroy();
        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        WebViewFragment webViewFragment = (WebViewFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        if (webViewFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.remove(webViewFragment).commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_item, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.save);

        if (dbReadPosts.check(rssModel, DbReadPosts.TABLE_NAME2)) {
            item.setIcon(R.drawable.ic_bookmark);
        } else {
            item.setIcon(R.drawable.ic_bookmark_done);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                ShareCompat.IntentBuilder.from(getActivity()).setType("text/plain").setChooserTitle("Share this article's link with:").setText(link).startChooser();
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