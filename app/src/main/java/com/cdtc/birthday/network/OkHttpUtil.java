package com.cdtc.birthday.network;

import com.google.gson.Gson;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Sweven on 2018/9/25.
 * Email:sweventears@Foxmail.com
 */
public class OkHttpUtil {

    private static OkHttpClient client = new OkHttpClient();

    /**
     * 请求成功
     */
    public static int OK = 200;

    /**
     * json请求头
     */
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    /**
     * 异步get请求
     *
     * @param url      请求的地址
     * @param callback 回调接口
     */
    public static void doGet(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);

    }

    /**
     * 异步post请求
     *
     * @param url         请求的地址
     * @param requestBody 请求体
     * @param callback    回调接口
     */
    public static void doPost(String url, RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 异步Json Post
     *
     * @param url      请求的地址
     * @param json     json字符串
     * @param callback 回调接口
     */
    public static void doJsonPost(String url, String json, Callback callback) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 异步Json Post请求
     *
     * @param url        请求的地址
     * @param jsonObject json字符串
     * @param callback   回调接口
     */
    public static void doJsonPost(String url, Object jsonObject, Callback callback) {
        RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(jsonObject));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
