package com.cdtc.birthday.view;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cdtc.birthday.LogUtil;
import com.cdtc.birthday.MainActivity;
import com.cdtc.birthday.R;
import com.cdtc.birthday.network.Api;
import com.cdtc.birthday.network.OkHttpUtil;
import com.cdtc.birthday.network.request.LoginRequest;
import com.cdtc.birthday.network.response.LoginResponse;
import com.cdtc.birthday.utils.ToastUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    /**
     * 头像
     */
    private ImageView userHeadPicture;

    /**
     * 手机号
     */
    private EditText loginPhone;

    /**
     * 密码
     */
    private EditText passWord;

    /**
     * 登录按钮
     */
    private Button loginButton;

    /**
     * activity
     */
    private Activity activity;

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initVariable();

        initView();
    }


    /**
     * 初始化变量
     */
    private void initVariable() {
        activity = this;
    }

    /**
     * 初始化组件
     */
    private void initView() {
        userHeadPicture = findViewById(R.id.login_image);
        loginPhone = findViewById(R.id.login_phone);
        passWord = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_btn);

        //绑定登录事件
        loginButton.setOnClickListener(view -> {
            String phone = loginPhone.getText().toString();
            String password = passWord.getText().toString();
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
                LoginRequest request = new LoginRequest();
                request.setLoginPhone(phone);
                request.setPassword(password);

                OkHttpUtil.doJsonPost(Api.LOGIN, new Gson().toJson(request), new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        LogUtil.i("OkHttpUtil", "onFailure: 请求失败" + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String responseString = response.body().string();
                        LoginResponse loginResponse;
                        try {

                            loginResponse = new Gson().fromJson(responseString, LoginResponse.class);
                            if (loginResponse.status == OkHttpUtil.OK) {
                                LogUtil.v(TAG, "登录成功 ： token " + loginResponse.getData().getToken());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                               ToastUtil.showShort(activity, loginResponse.desc);
                            }
                        } catch (Exception ignored) {

                        }
                    }
                });
            } else {
                ToastUtil.showShort(activity, "输入有误，请重新输入");
            }
        });
    }

    private void requestInfo(LoginRequest request) {


    }
}
