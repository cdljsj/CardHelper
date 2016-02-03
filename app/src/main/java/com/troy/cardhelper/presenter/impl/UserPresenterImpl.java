package com.troy.cardhelper.presenter.impl;

import android.content.Context;
import android.graphics.Bitmap;

import com.troy.cardhelper.R;
import com.troy.cardhelper.managers.Constant;
import com.troy.cardhelper.models.IUserModel;
import com.troy.cardhelper.models.impl.UserModelImpl;
import com.troy.cardhelper.network.ResponseCallback;
import com.troy.cardhelper.presenter.IUserPresenter;
import com.troy.cardhelper.views.LoginView;

/**
 * Created by chenlongfei on 16/1/26.
 */
public class UserPresenterImpl implements IUserPresenter, ResponseCallback {
    private IUserModel mUserModel;
    private LoginView mLoginView;
    private Context mContext;

    public UserPresenterImpl(Context context, LoginView loginView) {
        mContext = context;
        mLoginView = loginView;
        mUserModel = new UserModelImpl(this);
    }

    @Override
    public void login() {
        mLoginView.showLoading(mContext.getString(R.string.login_tips));
        //登录前先获取验证码
        getVerCode();
    }

    @Override
    public void logout() {

    }

    @Override
    public void checkLoginStatus() {
        mLoginView.showLoading(mContext.getString(R.string.check_login_status));
        //检查会话是否失效
        mUserModel.checkLoginStatus();
    }

    @Override
    public void getVerCode() {
        mLoginView.showLoading(mContext.getString(R.string.parseVerCode));
        mUserModel.getVerCode();
    }


    @Override
    public <T> void onSuccess(String tag, T data) {
        if (tag.equals(Constant.REQUEST_TAG_GETVERCODE)) {
            mUserModel.praseVerCode((Bitmap) data);
        } else if (tag.equals(Constant.REQUEST_TAG_PARSEVERCODE)) {
            String verCode = (String) data;
            String userName = mLoginView.getUserName();
            String passWord = mLoginView.getPassWord();
            mUserModel.requestLogin(userName, passWord, verCode);
        } else if (tag.equals(Constant.REQUEST_TAG_LOGIN)) {
            mLoginView.hideLoading();
            mLoginView.loginSuccess(mContext.getString(R.string.login_success));
        } else if (tag.equals(Constant.REQUEST_TAG_CHECK_LOGIN_STATUS)) {
            mLoginView.hideLoading();
            mLoginView.loginSuccess(mContext.getString(R.string.already_login));
        }
    }

    @Override
    public void onFail(String tag, String error) {
        mLoginView.hideLoading();
        if (tag.equals(Constant.REQUEST_TAG_LOGIN)) {
            mLoginView.showToast(error);
        } else {
            getVerCode();
        }
    }

    @Override
    public void onException(String tag) {
        mLoginView.hideLoading();
        mLoginView.showToast(mContext.getString(R.string.network_error));
    }

}
