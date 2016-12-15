package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;


import java.util.List;

/**
 * Created by neuromancer on 15/12/2016.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * Query URL
     */
    private String url;

    /**
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }
        List<News> newsList = QueryUtils.fetchNewsData(url);
        return newsList;
    }
}
