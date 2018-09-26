package com.cdtc.birthday.deskalert;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Sweven on 2018/9/24.
 * Email:sweventears@Foxmail.com
 */
public class FloatView extends View implements View.OnTouchListener {

    private Context mContext;
    private WindowManager wm;
    private static WindowManager.LayoutParams wmParams;
    public View mContentView;
    private float mRelativeX;
    private float mRelativeY;
    private float mScreenX;
    private float mScreenY;
    private boolean bShow = false;

    public FloatView(Context context) {
        super(context);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
        }
        mContext = context;
    }

    public void setLayout(int layout_id) {
        mContentView = LayoutInflater.from(mContext).inflate(layout_id, null);
        mContentView.setOnTouchListener(this);
    }

    private void updateViewPosition() {
        wmParams.x = (int) (mScreenX - mRelativeX);
        wmParams.y = (int) (mScreenY - mRelativeY);
        wm.updateViewLayout(mContentView, wmParams);
    }

    public void show() {
        if (mContentView != null) {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            wmParams.format = PixelFormat.RGBA_8888;
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.alpha = 1.0f;
            wmParams.gravity = Gravity.START | Gravity.TOP;
            wmParams.x = 0;
            wmParams.y = 0;
            wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            // 显示自定义悬浮窗口
            wm.addView(mContentView, wmParams);
            bShow = true;
        }
    }

    public void close() {
        if (mContentView != null) {
            wm.removeView(mContentView);
            bShow = false;
        }
    }

    public boolean isShow() {
        return bShow;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mScreenX = event.getRawX();
        mScreenY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRelativeX = event.getX();
                mRelativeY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                updateViewPosition();
                mRelativeX = mRelativeY = 0;
                break;
        }
        return false;
    }
}
