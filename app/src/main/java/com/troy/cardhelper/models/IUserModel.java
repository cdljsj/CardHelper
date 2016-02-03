package com.troy.cardhelper.models;

import android.graphics.Bitmap;

/**
 * Created by chenlongfei on 16/1/26.
 */
public interface IUserModel {
    void getVerCode();

    void praseVerCode(Bitmap result);

    void checkLoginStatus();

    void requestLogin(String userName, String pw,String verCode);

    void requestLogout();

}
