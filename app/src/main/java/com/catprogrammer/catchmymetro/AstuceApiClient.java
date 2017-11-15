package com.catprogrammer.catchmymetro;

/**
 * Created by Junyang HE on 14/11/2017.
 */

import com.loopj.android.http.*;

public class AstuceApiClient {
    private static final String BASE_URL = "https://reseau-astuce.fr/fr/horaires-a-larret/28/StopTimeTable/NextDeparture";
    //private static final String BASE_URL = "https://requestb.in/1c043fp1";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void post(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        System.out.println(params.toString());
        client.addHeader("Accept", "*/*");
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.addHeader("User-Agent", "AsyncHttpClient");
        client.post(CatchMyMetro.getAppContext(), BASE_URL, null, params, "application/x-www-form-urlencoded", responseHandler);
    }
}
