package com.luonghm.rssreader;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsUtils {

    public static final String PARAM_EVENT_TYPE = "event_type";
    public static final String PARAM_SIGN_IN = "sign_in";
    public static final String PARAM_LOG_OUT = "log_out";
    public static final String PARAM_READ_POST = "read_post";
    public static final String PARAM_SAVED_POST = "saved_post";
    public static final String PARAM_REVIEW = "review";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_SETTINGS = "setting";
    public static final String PARAM_ARTICLE_TYPE = "article_type";

    public static void putEventClick(Context context, String eventName, String eventType) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_EVENT_TYPE, eventType);
        firebaseAnalytics.logEvent(eventName, bundle);
    }
}
