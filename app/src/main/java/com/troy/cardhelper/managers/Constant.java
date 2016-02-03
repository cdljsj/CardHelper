package com.troy.cardhelper.managers;

/**
 * Created by chenlongfei on 16/1/26.
 */
public class Constant {
    //获取验证码图片接口
    public static final String GET_VERCODE_URL = "http://www.sinopecsales.com/websso/YanZhengMaServlet?";
    //登录接口
    public static final String LOGIN_URL = "http://www.sinopecsales.com/websso/loginAction_login.json";
    //查询充值卡状态接口
    public static final String SEARCH_CZKSTATUS_URL = "http://www.sinopecsales.com/gas/webjsp/memberOilCardAction_searchCzkStatus.json";
    //检查登录状态
    public static final String CHECK_LOGIN_STATUS_URL = "http://www.sinopecsales.com/gas/html/memberLoginAction_logInOrOut.json";
    //确认登录状态
    public static final String CONFIRM_LOGIN_STATUS_URL = "http://www.sinopecsales.com/gas/jsp/memberLoginAction_logInOrOut.json";
    //解析验证码接口
    public static final String PARSE_VERCODE_URL = "http://j.jtjr99.com/vcode";
    //登出
    public static final String LOGOUT_URL = "http://www.sinopecsales.com/websso/logoutAction.action";

    public static final String REQUEST_TAG_GETVERCODE = "getVerCode";
    public static final String REQUEST_TAG_PARSEVERCODE = "parseVerCode";
    public static final String REQUEST_TAG_LOGIN = "login";
    public static final String REQUEST_TAG_CZKSTATUS = "searchCzkStatus";
    public static final String REQUEST_TAG_CHECK_LOGIN_STATUS = "checkLoginStatus";
    public static final String REQUEST_TAG_CONFIRM_LOGIN_STATUS="confirmLoginStatus";


    public static final String SP_NAME = "cardHelper";
    public static final String SP_KEY_USERNAME = "userName";
    public static final String SP_KEY_PASSWORD = "password";
    public static final String SP_KEY_COOKIE="cookie";

    public static final String KEY_EXTRA_CARD_ID="cardId";

}
