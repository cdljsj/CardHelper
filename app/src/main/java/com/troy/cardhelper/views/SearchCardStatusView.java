package com.troy.cardhelper.views;

import java.util.Map;

/**
 * Created by chenlongfei on 16/2/1.
 */
public interface SearchCardStatusView {
    void querySuccess(Map<String, String> result);

    void queryEnd();

    void showLoading(String tips);

    void hideLoading();

    void showToast(String tips);

}
