package com.luonghm.rssreader;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TabFragment extends Fragment {
    private static final String POSITION = "position";
    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<RSSModel> rssModelList;
    private int position;
    private LinkedHashMap<String, String> linkedHashMap;
    private Iterator<Map.Entry<String, String>> iterator;
    private SharedPreferences sharedPreferences;
    private View rootView;

    public TabFragment() {
        // Required empty public constructor
    }

    public static TabFragment newInstance(int position) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = rootView.findViewById(R.id.recycler_view);
        int gridColumnCount =
                getResources().getInteger(R.integer.grid_column_count);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), gridColumnCount));

        linkedHashMap = new LinkedHashMap<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        position = sharedPreferences.getInt("Newspaper_list", 0);
        linkedHashMap.putAll(new NewsPaperList().getItem(position).getTab());
        iterator = linkedHashMap.entrySet().iterator();
        Bundle savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt(POSITION);
            for (int i = 0; i < pos; i++)
                iterator.next();
            new FeedTask().execute(iterator.next().getValue());
        } else
            new FeedTask().execute(linkedHashMap.entrySet().iterator().next().getValue());
    }

    public List<RSSModel> parseFeed(InputStream inputStream) throws
            XmlPullParserException, IOException {
        String title = null;
        String image = null;
        String pubDate = null;
        String link = null;
        boolean isItem = false;
        List<RSSModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();
                String name = xmlPullParser.getName();

                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }
                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        title = null;
                        image = null;
                        pubDate = null;
                        link = null;
                        continue;
                    }
                }
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }
                if (name.equalsIgnoreCase("title")) {
                    title = result;
                    if (title.contains(".") || title.contains("/")) title = null;
                } else if (name.equalsIgnoreCase("description")) {
                    if (result != null) {
                        int beginIndex = result.indexOf("src");
                        if (beginIndex > -1) result = result.substring(beginIndex);
                        beginIndex = result.indexOf("https");
                        if (beginIndex > -1) result = result.substring(beginIndex);
                        beginIndex = result.indexOf("https");
                        int endIndex;
                        if (position > 0)
                            endIndex = result.indexOf("\"");
                        else endIndex = result.indexOf("'");
                        if (beginIndex > -1 && endIndex > -1)
                            image = result.substring(beginIndex, endIndex);
                    }
                } else if (name.equalsIgnoreCase("pubDate")) {
                    pubDate = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                }


                if (title != null && image != null && pubDate != null && link != null) {
                    if (isItem) {
                        RSSModel rssModel = new RSSModel(title, image, pubDate, link);
                        items.add(rssModel);
                    }
                    title = null;
                    image = null;
                    pubDate = null;
                    link = null;
                    isItem = false;
                }
            }
            return items;
        } finally {
            inputStream.close();

        }
    }

    private class FeedTask extends AsyncTask<String, Void, List<RSSModel>> {

        @Override
        protected List<RSSModel> doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                rssModelList = parseFeed(inputStream);
                return rssModelList;
            } catch (IOException | XmlPullParserException e) {
                Log.e("LOG", "Error", e);
            }
            return rssModelList;
        }

        @Override
        protected void onPostExecute(List<RSSModel> rssModelList) {
            if (getContext() != null)
                recyclerView.setAdapter(new Adapter(getContext(), rssModelList));
        }
    }
}