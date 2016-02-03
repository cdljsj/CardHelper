package com.troy.cardhelper.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.troy.cardhelper.R;
import com.troy.cardhelper.managers.Constant;
import com.troy.cardhelper.presenter.IUserPresenter;
import com.troy.cardhelper.presenter.impl.UserPresenterImpl;
import com.troy.cardhelper.utils.SPUtil;
import com.troy.cardhelper.views.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private IUserPresenter mUserPresenter;
    private ImageView mVerCode;
    private Button mLogin;
    private EditText mUserName;
    private EditText mPassWord;
    private EditText mVerCodeEt;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserPresenter = new UserPresenterImpl(this, this);
        initViews();
        checkLoginStatus();
    }

    private void initViews() {
        mVerCode = (ImageView) findViewById(R.id.iv_verification_code);

        mProgressDialog = new ProgressDialog(this);

        mUserName = (EditText) findViewById(R.id.et_user_name);
        String userName = SPUtil.getString(Constant.SP_KEY_USERNAME);
        if (!TextUtils.isEmpty(userName) && !userName.equals("")) {
            mUserName.setText(userName);
        }

        mPassWord = (EditText) findViewById(R.id.et_pw);
        String passWord = SPUtil.getString(Constant.SP_KEY_PASSWORD);
        if (!TextUtils.isEmpty(passWord) && !passWord.equals("")) {
            mPassWord.setText(passWord);
        }

        mVerCodeEt = (EditText) findViewById(R.id.et_ver_code);
        mLogin = (Button) findViewById(R.id.btn_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }


    private void checkLoginStatus() {
        if (!TextUtils.isEmpty(getUserName()) && !TextUtils.isEmpty(getPassWord())) {
            mUserPresenter.checkLoginStatus();
        }
    }

    @Override
    public void showLoading(String tips) {
        mProgressDialog.setMessage(tips);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showVerCode(Bitmap bitmap) {
        if (bitmap != null) {
            mVerCode.setImageBitmap(bitmap);
        }
    }

    @Override
    public String getUserName() {
        return mUserName.getText().toString().trim();
    }

    @Override
    public String getPassWord() {
        return mPassWord.getText().toString().trim();
    }

    @Override
    public String getVerCode() {
        return mVerCodeEt.getText().toString().trim();
    }

    @Override
    public void setVerCode(String verCode) {
        mVerCodeEt.setText(verCode);
    }

    @Override
    public void loginSuccess(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SearchCardStatusActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login_menu, menu);
        MenuItem logoutItem = menu.findItem(R.id.logout);
        logoutItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mUserPresenter.logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        mUserName.setError(null);
        mPassWord.setError(null);
        if (TextUtils.isEmpty(getUserName())) {
            mUserName.setError(getString(R.string.username_not_empty));
            mUserName.requestFocus();
        } else if (TextUtils.isEmpty(getPassWord())) {
            mPassWord.setError(getString(R.string.password_not_empty));
            mPassWord.requestFocus();
        } else {
            mUserPresenter.login();
        }
    }
}
