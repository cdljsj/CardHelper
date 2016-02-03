package com.troy.cardhelper.network;

/**
 * Created by chenlongfei on 16/1/26.
 */
public interface ResponseCallback {
    <T> void onSuccess(String tag, T data);

    void onFail(String tag, String error);

    void onException(String tag);
}
