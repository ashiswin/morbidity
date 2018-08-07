package com.ashiswin.morbidity;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * Created by arroyo on 5/8/18.
 */

public class MotivationalQuoteWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_motivational_quote);
        final RequestQueue queue = Volley.newRequestQueue(context);
        final String url ="https://quotes.rest/qod";

        final String default_quote = context.getString(R.string.default_quote);
        final String default_author = context.getString(R.string.default_author);

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            final int appWidgetId = appWidgetIds[i];

            // Request a json response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            String quote;
                            String author;
                            JSONObject quoteObject;
                            try {
                                quoteObject = response.getJSONObject("contents")
                                        .getJSONArray("quotes")
                                        .getJSONObject(0);
                                quote = quoteObject.getString("quote");
                                author = quoteObject.getString("author");
                            } catch (Exception e) {
                                quote = default_quote;
                                author = default_author;
                                Log.e("Read Json Object", "onResponse: ", e);
                            }

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
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            views.setTextViewText(
                                    R.id.widget_motivational_quote,
                                    default_quote);
                            views.setTextViewText(
                                    R.id.widget_motivational_author,
                                    default_author);
                            appWidgetManager.updateAppWidget(appWidgetId, views);
                        }
                    });

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        }
    }
}
