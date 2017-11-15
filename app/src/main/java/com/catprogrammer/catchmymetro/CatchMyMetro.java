package com.catprogrammer.catchmymetro;

import android.app.Application;
import android.content.Context;

/**
 * Created by Junyang HE on 15/11/2017.
 */

public class CatchMyMetro extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        CatchMyMetro.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return CatchMyMetro.context;
    }
}