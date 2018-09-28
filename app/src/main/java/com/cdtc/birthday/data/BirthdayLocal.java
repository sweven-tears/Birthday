package com.cdtc.birthday.data;

import java.util.List;

/**
 * Created by Sweven on 2018/9/25.
 * Email:sweventears@Foxmail.com
 */
public class BirthdayLocal {

    /**
     * 用户手机号
     */
    public String phone;

    /**
     * 用户名
     */
    public String username;

    /**
     * 用户存储的生日信息
     */
    public List<BirthBean> birthBeans;

    /**
     * 用户登录状态
     */
    public boolean isLogin;

    /**
     * 账户本地锁定状态
     */
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

    public List<BirthBean> getBirthBeans() {
        return birthBeans;
    }

    public void setBirthBeans(List<BirthBean> birthBeans) {
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
