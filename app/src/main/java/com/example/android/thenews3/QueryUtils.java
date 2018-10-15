package com.example.android.thenews3;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Helper methods for requesting and receiving news data from Guardian API.
 *
 * Created by yahir on 10/145/2018.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian API and return a list news objects
     */
    public static List<News> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        // within a try/catch statement
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of news objects
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the list of news objects}
        return news;
    }

    /**
     * Returns a new URL object
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request for URL
     * return a String response
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
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Note: Closing the input stream could throw an IOException
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the input stream into a String which contains the
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
     * Return a list of nes objects that has been built
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<>();

        // Try to parse the JSON response string.
        // JSONException exception object will be thrown if problems parsing.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract the JSONArray associated with the key called "response"
            JSONObject response = baseJsonResponse.getJSONObject("response");

            //In the variable resultsArray gets the JSONArray results stored
            JSONArray resultsArray = response.getJSONArray("results");

            // For each news in the newsArray, create an News object
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get a single news at position i within the list of news articles
                JSONObject currentNews = resultsArray.getJSONObject(i);

                // Extract article title
                String title = currentNews.getString("webTitle");

                // Extract the category "categoryName"
                String categoryName = currentNews.getString("sectionName");

                // Extract the value for the author tags
                String author;
                JSONArray tagAuthor = currentNews.getJSONArray("tags");
                //Get the tag value at the 0th place of the array
                if (tagAuthor.length() != 0) {
                    JSONObject currentAuthor = tagAuthor.getJSONObject(0);
                    // Get the tag value fro the author
                    author = currentAuthor.getString("webTitle");
                } else {
                    // Else set empty string
                    author = " ";
                }

                // Extract the value for date
                String date = currentNews.getString("webPublicationDate");
                // Format time/date in try/catch statement
                String dateFormatted = "";
                try {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                    Date dateFMT = format.parse(date);
                    DateFormat format2 = new SimpleDateFormat("HH:mm dd MMM yy", Locale.getDefault());
                    dateFormatted = format2.format(dateFMT);
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Problem extracting date from JSON: ", e);
                }

                // Extract the value for URL
                String url = currentNews.getString("webUrl");

                // Create a new News object with the title, category, author, date, URL
                News newsArticle = new News(title, categoryName, author, dateFormatted, url);

                // Add the new News to the list of news.
                news.add(newsArticle);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Error Parsing results", e);
        }

        // Return the list of news articles
        return news;
    }
}