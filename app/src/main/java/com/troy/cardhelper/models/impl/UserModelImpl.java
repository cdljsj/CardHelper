package com.troy.cardhelper.models.impl;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.troy.cardhelper.managers.Constant;
import com.troy.cardhelper.models.IUserModel;
import com.troy.cardhelper.network.ResponseCallback;
import com.troy.cardhelper.network.VolleySingleton;
import com.troy.cardhelper.utils.Crypt;
import com.troy.cardhelper.utils.ImageUtil;
import com.troy.cardhelper.utils.SPUtil;

import org.da.expressionj.expr.parser.EquationParser;
import org.da.expressionj.expr.parser.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 关于Cookies说明，每个一级目录都有不同的Cookies，同一级目录必须用相同的Cookies实现关联，登录成功后的ticket，用于身份验证。
 * Created by chenlongfei on 16/1/26.
 */
public class UserModelImpl implements IUserModel {
    private ResponseCallback responseCallback;
    private String mCookie = "";

    public UserModelImpl(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    //获取验证码图片
    @Override
    public void getVerCode() {
        ImageRequest request = new ImageRequest(Constant.GET_VERCODE_URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (response != null) {
                    responseCallback.onSuccess(Constant.REQUEST_TAG_GETVERCODE, response);
                } else {
                    responseCallback.onFail(Constant.REQUEST_TAG_GETVERCODE, "验证码获取失败");
                }
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    responseCallback.onFail(Constant.REQUEST_TAG_GETVERCODE, error.toString());
                } else {
                    responseCallback.onException(Constant.REQUEST_TAG_GETVERCODE);
                }
            }
        }) {
            @Override
            protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                String allCookie = headers.get("Set-Cookie");
                if (allCookie != null) {
                    String[] cookies = allCookie.split(";");
                    for (String cookie : cookies) {
                        if (cookie.startsWith("JSESSIONID")) {
                            mCookie = cookie;
                        }
                    }
                }
                Log.i("Troy", "getVerCodeCookie:" + allCookie);
                return super.parseNetworkResponse(response);
            }
        };
        VolleySingleton.getInstance().addRequestQueue(request, Constant.REQUEST_TAG_GETVERCODE);
    }

    //将验证码转换成算术运算
    @Override
    public void praseVerCode(final Bitmap result) {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.PARSE_VERCODE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("code")) {
                        if (jsonObject.getInt("code") == 0) {
                            String data = jsonObject.getString("data");
                            String result = "";
                            try {
                                result = EquationParser.parse(data).eval().toString();
                                responseCallback.onSuccess(Constant.REQUEST_TAG_PARSEVERCODE, result);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                responseCallback.onFail(Constant.REQUEST_TAG_PARSEVERCODE, "验证码转换失败");
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseCallback.onFail(Constant.REQUEST_TAG_PARSEVERCODE, "验证码转换失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    responseCallback.onFail(Constant.REQUEST_TAG_PARSEVERCODE, error.toString());
                } else {
                    responseCallback.onException(Constant.REQUEST_TAG_PARSEVERCODE);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //post 数据
                Map<String, String> params = new HashMap<String, String>();
                params.put("code_type", "SinopecVcode");
                params.put("img_content", ImageUtil.addBase64Header(result));
                params.put("img_type", "png");
                params.put("t", System.currentTimeMillis() + "");
                return params;
            }
        };
        VolleySingleton.getInstance().addRequestQueue(request, Constant.REQUEST_TAG_PARSEVERCODE);
    }


    //登录
    @Override
    public void requestLogin(final String userName, final String pw, final String verCode) {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Troy", "requestLogin====>response:" + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("success")) {
                        int success = jsonObject.getInt("success");
                        switch (success) {
                            //登录成功!
                            case 0:
                                responseCallback.onSuccess(Constant.REQUEST_TAG_LOGIN, response);
                                SPUtil.putString(Constant.SP_KEY_USERNAME, userName);
                                SPUtil.putString(Constant.SP_KEY_PASSWORD, pw);
                                confirmLoginStatus();
                                break;
                            //用户名/密码错误!
                            case 1:
                                responseCallback.onFail(Constant.REQUEST_TAG_LOGIN, "用户名或密码错误");
                                break;
                            //请重新获取验证码!
                            case 2:
                                //验证码错误!
                            case 3:
                                //用户登录失败，请重新登录!
                            case 4:
                                getVerCode();
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseCallback.onFail(Constant.REQUEST_TAG_LOGIN, "登录失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    responseCallback.onFail(Constant.REQUEST_TAG_LOGIN, error.toString());
                } else {
                    responseCallback.onException(Constant.REQUEST_TAG_LOGIN);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("memberAccount", userName);
                params.put("memberUmm", Crypt.SHA1(pw));
                params.put("check", verCode);
                params.put("rememberMe", "on");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                String allCookie = headers.get("Set-Cookie");
                if (allCookie != null) {
                    String[] cookies = allCookie.split(";");
                    for (String cookie : cookies) {
                        if (cookie.startsWith("ticket")) {
                            mCookie = cookie;
                        }
                    }
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Cookie", mCookie);
                return headers;
            }
        };


        VolleySingleton.getInstance().addRequestQueue(request, Constant.REQUEST_TAG_LOGIN);
    }

    //登出
    @Override
    public void requestLogout() {

    }


    //确认登录状态
    public void confirmLoginStatus() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.CONFIRM_LOGIN_STATUS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Troy", "confirmLoginStatus:" + response.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("memberAccount")) {
                        if (!"".equals(jsonObject.getString("memberAccount"))) {
                            responseCallback.onSuccess(Constant.REQUEST_TAG_CONFIRM_LOGIN_STATUS, response);
                        } else {
                            responseCallback.onFail(Constant.REQUEST_TAG_CONFIRM_LOGIN_STATUS, null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseCallback.onFail(Constant.REQUEST_TAG_CONFIRM_LOGIN_STATUS, null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    responseCallback.onFail(Constant.REQUEST_TAG_CONFIRM_LOGIN_STATUS, error.toString());
                } else {
                    responseCallback.onException(Constant.REQUEST_TAG_CONFIRM_LOGIN_STATUS);
                }

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                String allCookie = headers.get("Set-Cookie");
                if (allCookie != null) {
                    String[] cookies = allCookie.split(";");
                    for (String cookie : cookies) {
                        if (cookie.startsWith("JSESSIONID")) {
                            mCookie = mCookie + ";" + cookie;
                        }
                    }
                }
                SPUtil.putString(Constant.SP_KEY_COOKIE, mCookie);
                Log.i("Troy", "confirmLoginStatusCookie:" + mCookie);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Cookie", mCookie);
                return headers;
            }
        };
        VolleySingleton.getInstance().addRequestQueue(request, Constant.REQUEST_TAG_CONFIRM_LOGIN_STATUS);
    }

    //检查登录态,利用Cookie去执行检查
    @Override
    public void checkLoginStatus() {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.CHECK_LOGIN_STATUS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Troy", "checkLoginStatusResponse:" + response.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("memberAccount")) {
                        if (!"".equals(jsonObject.getString("memberAccount"))) {
                            responseCallback.onSuccess(Constant.REQUEST_TAG_CHECK_LOGIN_STATUS, response);
                        } else {
                            responseCallback.onFail(Constant.REQUEST_TAG_CHECK_LOGIN_STATUS, null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseCallback.onFail(Constant.REQUEST_TAG_CHECK_LOGIN_STATUS, null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    responseCallback.onFail(Constant.REQUEST_TAG_CHECK_LOGIN_STATUS, error.toString());
                } else {
                    responseCallback.onException(Constant.REQUEST_TAG_CHECK_LOGIN_STATUS);
                }

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                String cookie = headers.get("Set-Cookie");
                Log.i("Troy", "checkLoginStatusCookie:" + cookie);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String cookie = SPUtil.getString(Constant.SP_KEY_COOKIE);
                headers.put("Cookie", cookie);
                return headers;
            }
        };
        VolleySingleton.getInstance().addRequestQueue(request, Constant.REQUEST_TAG_CHECK_LOGIN_STATUS);
    }
}
