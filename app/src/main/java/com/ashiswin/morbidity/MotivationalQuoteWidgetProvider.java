package com.ashiswin.morbidity;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by arroyo on 5/8/18.
 */

public class MotivationalQuoteWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_motivational_quote);
        final RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "https://andruxnet-random-famous-quotes.p.rapidapi.com/?cat=famous&count=1";
        // final String url ="https://quotes.rest/qod";

        final String default_quote = context.getString(R.string.default_quote);
        final String default_author = context.getString(R.string.default_author);

        // Set up logging
        VolleyLog.setTag("Volley");
        Log.isLoggable("Volley", Log.VERBOSE);

        // Request a json response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    String quote;
                    String author;
                    JSONObject quoteObject;
                    try {
                        quoteObject = response.getJSONObject(0);
                        quote = quoteObject.getString("quote");
                        author = quoteObject.getString("author");
                    } catch (Exception e) {
                        quote = default_quote;
                        author = default_author;
                        Log.e("Read Json Object", "onResponse: ", e);
                    }

                    updateAll(N, quote, author, views, appWidgetManager, appWidgetIds);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    updateAll(N, default_quote, default_author, views, appWidgetManager, appWidgetIds);
                }
            })
        {
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("X-RapidAPI-Key", "b0fd086fe0msh975bd8c14459cfep1eb72ejsn739659d5d4e5");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    private void updateAll(int widgetLength, String quote, String author,
                           RemoteViews views, AppWidgetManager appWidgetManager,
                           int[] appWidgetIds) {

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < widgetLength; i++) {
            int appWidgetId = appWidgetIds[i];
            views.setTextViewText(
                    R.id.widget_motivational_quote,
                    quote
            );
            views.setTextViewText(
                    R.id.widget_motivational_author,
                    author
            );
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
