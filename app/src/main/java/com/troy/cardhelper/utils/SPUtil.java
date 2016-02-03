package com.troy.cardhelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.troy.cardhelper.managers.CardHelperApp;
import com.troy.cardhelper.managers.Constant;

/**
 * Created by chenlongfei on 16/1/29.
 */
public class SPUtil {
    public static void putString(String key, String value) {
        SharedPreferences sp = CardHelperApp.mContext.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        SharedPreferences sp = CardHelperApp.mContext.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
}
