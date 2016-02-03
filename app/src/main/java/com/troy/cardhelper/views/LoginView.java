package com.troy.cardhelper.views;

import android.graphics.Bitmap;

/**
 * Created by chenlongfei on 16/1/26.
 */
public interface LoginView {
    void showLoading(String tips);

    void hideLoading();

    void showVerCode(Bitmap bitmap);

    String getUserName();

    String getPassWord();

    String getVerCode();

    void setVerCode(String verCode);

    void loginSuccess(String info);

    void showToast(String msg);
}
