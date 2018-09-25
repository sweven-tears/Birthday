package com.cdtc.birthday.network.response;

/**
 * Created by Sweven on 2018/9/25.
 * Email:sweventears@Foxmail.com
 */
public class BaseResponse {

    /**
     * 状态码
     */
    public int status;

    /**
     * 描述信息
     */
    public String desc;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", desc='" + desc + '\'' +
                '}';
    }
}
