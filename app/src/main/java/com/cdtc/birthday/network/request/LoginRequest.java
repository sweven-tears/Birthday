package com.cdtc.birthday.network.request;

/**
 * Created by Sweven on 2018/9/25.
 * Email:sweventears@Foxmail.com
 */
public class LoginRequest {
    private String loginPhone;
    private String password;

    public String getLoginPhone() {
        return loginPhone;
    }

    public void setLoginPhone(String loginPhone) {
        this.loginPhone = loginPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "loginPhone='" + loginPhone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
