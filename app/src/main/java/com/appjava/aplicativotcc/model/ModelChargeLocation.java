package com.appjava.aplicativotcc.model;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ModelChargeLocation {
    private static final String API_KEY = "b6561eae-740e-4965-b397-2702a166fdcb";
    private static final String BASE_URL = "https://api.openchargemap.io/v3/poi/";

    public static void getChargeLocationFromAPI(Callback callback) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("output", "json")
                .addQueryParameter("countrycode", "BR")
                .addQueryParameter("latitude", "-29.1676")
                .addQueryParameter("longitude", "-51.1794")
                .addQueryParameter("distance", "100")
                .addQueryParameter("distanceunit", "KM")
                .addQueryParameter("maxresults", "50")
                .addQueryParameter("key", API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }
}
