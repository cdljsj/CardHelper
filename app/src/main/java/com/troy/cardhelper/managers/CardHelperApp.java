package com.troy.cardhelper.managers;

import android.app.Application;
import android.content.Context;

/**
 * Created by chenlongfei on 16/1/26.
 */
public class CardHelperApp extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
