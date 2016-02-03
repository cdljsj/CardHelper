package com.troy.cardhelper.presenter.impl;

import android.content.Context;
import android.graphics.Bitmap;

import com.troy.cardhelper.R;
import com.troy.cardhelper.bean.CardInfo;
import com.troy.cardhelper.managers.Constant;
import com.troy.cardhelper.models.ISearchCardStatusModel;
import com.troy.cardhelper.models.IUserModel;
import com.troy.cardhelper.models.impl.SearchCardStatusModelImpl;
import com.troy.cardhelper.models.impl.UserModelImpl;
import com.troy.cardhelper.network.ResponseCallback;
import com.troy.cardhelper.presenter.ISearchCardStatusPresenter;
import com.troy.cardhelper.utils.SPUtil;
import com.troy.cardhelper.views.SearchCardStatusView;

import java.util.List;
import java.util.Map;

/**
 * Created by chenlongfei on 16/2/1.
 */
public class SearchCardStatusPresenterImpl implements ISearchCardStatusPresenter, ResponseCallback {
    private SearchCardStatusView mSearchCardStatusView;
    private ISearchCardStatusModel mSearchCardStatus;
    private IUserModel mUserModel;
    private Context mContext;
    private List<CardInfo> mCardInfo;

    public SearchCardStatusPresenterImpl(Context context, SearchCardStatusView searchCardStatusView) {
        mContext = context;
        mSearchCardStatusView = searchCardStatusView;
        mSearchCardStatus = new SearchCardStatusModelImpl(this);
        mUserModel = new UserModelImpl(this);
    }

    @Override
    public <T> void onSuccess(String tag, T data) {
        if (tag.equals(Constant.REQUEST_TAG_GETVERCODE)) {
            mUserModel.praseVerCode((Bitmap) data);
        } else if (tag.equals(Constant.REQUEST_TAG_PARSEVERCODE)) {
            String verCode = (String) data;
            String userName = SPUtil.getString(Constant.SP_KEY_USERNAME);
            String passWord = SPUtil.getString(Constant.SP_KEY_PASSWORD);
            if ("".equals(userName) || "".equals(passWord)) {
                mSearchCardStatusView.showToast("请重新登录");
            } else {
                mUserModel.requestLogin(userName, passWord, verCode);
            }
        } else if (tag.equals(Constant.REQUEST_TAG_LOGIN)) {
        } else if (tag.equals(Constant.REQUEST_TAG_CHECK_LOGIN_STATUS) || tag.equals(Constant.REQUEST_TAG_CONFIRM_LOGIN_STATUS)) {
            for (CardInfo cardInfo : mCardInfo) {
                mSearchCardStatus.searchCardStatus(cardInfo.getCardId());
            }
        } else if (tag.equals(Constant.REQUEST_TAG_CZKSTATUS)) {
            Map<String, String> result = (Map) data;
            if (result.size() == mCardInfo.size()) {
                mSearchCardStatusView.hideLoading();
            }
            mSearchCardStatusView.querySuccess(result);
        }
    }

    @Override
    public void onFail(String tag, String error) {
        if (tag.equals(Constant.REQUEST_TAG_LOGIN)) {
            mSearchCardStatusView.showToast(error);
        } else if (tag.equals(Constant.REQUEST_TAG_CZKSTATUS)) {
            mSearchCardStatusView.hideLoading();
        } else {
            mUserModel.getVerCode();
        }
    }

    @Override
    public void onException(String tag) {
        mSearchCardStatusView.hideLoading();
        mSearchCardStatusView.showToast(mContext.getString(R.string.network_error));
    }

    @Override
    public void searchCardStatus(List<CardInfo> cardInfos) {
        mCardInfo = cardInfos;
        mSearchCardStatusView.showLoading("正在查询...");
        mSearchCardStatus.searchCardClear();
        //先检查登录态
        mUserModel.checkLoginStatus();
    }
}
