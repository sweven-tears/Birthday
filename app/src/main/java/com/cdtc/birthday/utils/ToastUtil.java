package com.cdtc.birthday.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast;

    /**
     * 没有更多数据
     *
     * @param context
     */
    public static void showNoMoreData(Context context) {
        showShort(context, "没有更多数据啦");
    }

    /**
     * 显示一个较短时间的提示
     *
     * @param context 上下文
     * @param message 显示的文字
     */
    public static void showShort(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 显示一个较长时间的提示
     *
     * @param context 上下文
     * @param message 显示的文本
     */
    public static void showLong(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 解析数据错误时展示的提示
     *
     * @param context
     */
    public static void showDataError(Context context) {
        if (toast == null) {
            toast = Toast.makeText(context, "网络错误，请稍后再试", Toast.LENGTH_SHORT);
        } else {
            toast.setText("网络错误，请稍后再试");
        }
        toast.show();
    }

    /**
     * 取消显示
     */
    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
