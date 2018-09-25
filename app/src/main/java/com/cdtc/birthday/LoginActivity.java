package com.cdtc.birthday;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdtc.birthday.network.Api;
import com.cdtc.birthday.network.OkHttpUtil;
import com.cdtc.birthday.network.request.LoginRequest;
import com.cdtc.birthday.network.response.BaseResponse;
import com.cdtc.birthday.network.response.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private ImageView userHeadPicture;
    private EditText EditPhone;
    private EditText EditPassWord;
    private Button loginButton;
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userHeadPicture = findViewById(R.id.login_image);
        EditPhone = findViewById(R.id.login_phone);
        EditPassWord = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_btn);
        init();
    }

    private void init() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judge();
            }
        });
    }

    private void judge() {
        String phone = EditPhone.getText().toString();
        String password = EditPassWord.getText().toString();
        if (isNoNull(phone) && isNoNull(password)) {
            LoginRequest request = new LoginRequest();
            request.setLoginPhone(phone);
            request.setPassword(password);
            requestInfo(request);
        } else {
            Toast.makeText(this, "输入有误，请重新输入", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestInfo(LoginRequest request) {

        OkHttpUtil.doJsonPost(Api.LOGIN, new Gson().toJson(request), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogUtil.i("OkHttpUtil", "onFailure: 请求失败" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String responseString = response.body().string();
                LoginResponse baseResponse;
                try {

                    baseResponse = new Gson().fromJson(responseString, LoginResponse.class);
                    if (baseResponse.status == OkHttpUtil.OK) {
                        //todo 跳转回“我的”界面
                        LogUtil.v(TAG, "登录成功");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "手机号、密码不匹配", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ignored) {
                }
            }
        });
    }

    private boolean isNoNull(String text) {
        String removeSpace = text.replace(" ", "");
        return removeSpace.length() > 0;
    }
}
