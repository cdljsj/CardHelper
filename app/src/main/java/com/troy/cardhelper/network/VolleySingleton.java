package com.troy.cardhelper.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.troy.cardhelper.managers.CardHelperApp;

/**
 * Created by chenlongfei on 16/1/26.
 */
public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;

    private VolleySingleton() {
    }

    public static VolleySingleton getInstance() {
        synchronized (VolleySingleton.class) {
            if (mInstance == null) {
                mInstance = new VolleySingleton();
            }
            return mInstance;
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(CardHelperApp.mContext);
        }
        return mRequestQueue;
    }

    public <T> void addRequestQueue(Request<T> request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public <T> void addRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public void cancelRequest(String tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
