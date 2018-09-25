package com.cdtc.birthday.util;

import java.util.ArrayList;

/**
 * Created by Sweven on 2018/9/25.
 * Email:sweventears@Foxmail.com
 */
public class Birthday {

    public String phone;
    public String username;
    public ArrayList<BirthBean> birthBeans;
    public boolean isLogin;
    public boolean isLock;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<BirthBean> getBirthBeans() {
        return birthBeans;
    }

    public void setBirthBeans(ArrayList<BirthBean> birthBeans) {
        this.birthBeans = birthBeans;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }
}
