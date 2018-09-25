package com.cdtc.birthday.network.response;

/**
 * Created by Sweven on 2018/9/25.
 * Email:sweventears@Foxmail.com
 */
public class LoginResponse extends BaseResponse {
    /**
     * 响应数据
     */
    private LoginData data;

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "data=" + data +
                "} " + super.toString();
    }
}

