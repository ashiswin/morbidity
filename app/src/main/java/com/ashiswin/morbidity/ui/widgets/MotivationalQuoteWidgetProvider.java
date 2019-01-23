package com.ashiswin.morbidity.ui.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ashiswin.morbidity.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by arroyo on 5/8/18.
 */

public class MotivationalQuoteWidgetProvider extends AppWidgetProvider {

    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        final RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "https://andruxnet-random-famous-quotes.p.rapidapi.com/?cat=famous&count=1";
        // final String url ="https://quotes.rest/qod";

        // Set up logging
        final String TAG = "Motivational Widget";
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
                        updateAppWidgets(context, appWidgetManager, appWidgetIds,
                                         quote, author);
                    } catch (Exception e) {
                        Log.e(TAG, "Volley: ", e);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Volley: ", error);
                }
            })
        {
            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Key", "b0fd086fe0msh975bd8c14459cfep1eb72ejsn739659d5d4e5");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    private static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                         int[] appWidgetIds, String quote, String author) {

        // Construct the RemoteViews object and update object
        RemoteViews views = new RemoteViews(context.getPackageName(),
                                            R.layout.motivational_quote_widget);
        views.setTextViewText(R.id.widget_motivational_quote, quote);
        views.setTextViewText(R.id.widget_motivational_author, author);

        // Instruct the widget manager to update widgets
        for (int appWidgetId: appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
