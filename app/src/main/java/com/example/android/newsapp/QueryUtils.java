package com.example.android.newsapp;

/**
 * Created by neuromancer on 14/12/2016.
 */


import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper methods related to requesting and receiving news data from guardianapis
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final int FIRST_READ_TIME_OUT = 10000;
    private static final int SECOND_READ_TIME_OUT = 10000;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static varialbes and methods, which can be accessed
     * directly from the class name {@link QueryUtils} (and an object instance of {@link QueryUtils} is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL objects from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Make an HHTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(FIRST_READ_TIME_OUT /* milliseconds */);
            urlConnection.setConnectTimeout(SECOND_READ_TIME_OUT /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Return a list of {@link News} objects that has been build up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        //If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        //Create an empty ArrayList that we can start adding news to
        List<News> newsList = new ArrayList<>();

        //Try to parse the JSON response string. If there's a problem with the way the JSON
        //is formatted, a JSONException exception object will be thrown.
        //Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            //Create a JSONObject form the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject jsonResults = baseJsonResponse.getJSONObject("response");

            //Extract the JSONArray associated with the key called "results"
            //which represents a list of results (or news)
            JSONArray newsArray = jsonResults.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject currentNews = newsArray.getJSONObject(i);


                //Extract the value for the key called "sectionName"
                String sectionName = currentNews.optString("sectionName");

                //Extract the value for the key called "webPublicationDate"
                String webPublicationDate = currentNews.optString("webPublicationDate");

                //Extract the value for the key called "webTitle"
                String webTitle = currentNews.optString("webTitle");

                //Extract the value for the key called "webUrl"
                String webUrl = currentNews.optString("webUrl");


                JSONObject fields = currentNews.getJSONObject("fields");

                //Extract the value for the key called "thumbnail"
                String thumbnail = fields.getString("thumbnail");

                //Create a new News object with the webTitle, webPublicationDate, webUrl, thumbnail
                //from the JSON response.
                News news = new News(webTitle, sectionName, webPublicationDate, webUrl, thumbnail);

                //Add the new News to the list of news
                newsList.add(news);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        //Return list of news
        return newsList;
    }


    public static List<News> fetchNewsData(String requestUrl) {
        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, " Problem making the HTTP request.", e);
        }
        //Extract relevant fields from the JSON response and create a list of News
        List<News> newsList = extractFeatureFromJson(jsonResponse);

        //Return the list of News
        return newsList;


    }

}

