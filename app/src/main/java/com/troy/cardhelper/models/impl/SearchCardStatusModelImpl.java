package com.troy.cardhelper.models.impl;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.troy.cardhelper.managers.Constant;
import com.troy.cardhelper.models.ISearchCardStatusModel;
import com.troy.cardhelper.network.ResponseCallback;
import com.troy.cardhelper.network.VolleySingleton;
import com.troy.cardhelper.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenlongfei on 16/2/1.
 */
public class SearchCardStatusModelImpl implements ISearchCardStatusModel {
    private ResponseCallback responseCallback;
    private Map<String, String> mStatusMap = new HashMap<String, String>();

    public SearchCardStatusModelImpl(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    @Override
    public void searchCardStatus(final String cardId) {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.SEARCH_CZKSTATUS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Troy", "searchCZKStatus====>response:" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("czkUseStatus")) {
                        String status = jsonObject.getString("czkUseStatus");
                        mStatusMap.put(cardId, status);
                        responseCallback.onSuccess(Constant.REQUEST_TAG_CZKSTATUS, mStatusMap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    responseCallback.onFail(Constant.REQUEST_TAG_CZKSTATUS, error.toString());
                } else {
                    responseCallback.onException(Constant.REQUEST_TAG_CZKSTATUS);
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String cookie = SPUtil.getString(Constant.SP_KEY_COOKIE);
                headers.put("Cookie", cookie);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("czkNo", cardId);
                return params;
            }
        };
        VolleySingleton.getInstance().addRequestQueue(request, Constant.REQUEST_TAG_CZKSTATUS);
    }

    @Override
    public void searchCardClear() {
        mStatusMap.clear();
    }
}
