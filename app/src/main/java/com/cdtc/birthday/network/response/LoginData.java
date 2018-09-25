package com.cdtc.birthday.network.response;

public class LoginData {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "token='" + token + '\'' +
                '}';
    }
}
