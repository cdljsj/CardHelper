package com.troy.cardhelper.presenter;

/**
 * Created by chenlongfei on 16/1/26.
 */
public interface IUserPresenter {
    void login();
    void logout();
    void checkLoginStatus();
    void getVerCode();
}
