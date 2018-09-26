package com.cdtc.birthday;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdtc.birthday.utils.LogUtil;

public class LockScreenActivity extends AppCompatActivity implements View.OnTouchListener {

    private float mStartX, mWidth;
    private LinearLayout mLinearLayout;
    private RelativeLayout mRelativeLayout;
    private TextView timeView, timeWeekView, timeYYMMDDView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        hideBottomUIMenu();

        init();
        TimeThread timeThread=new TimeThread(timeView,timeWeekView,timeYYMMDDView);
        timeThread.start();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        float density = dm.density;
        mWidth = width / density;
        LogUtil.d("LockScreenActivity", "mWidth : " + mWidth);
    }

    /**
     * 绑定id,加载布局
     */
    private void init() {

        mLinearLayout = findViewById(R.id.slip_layout);
//        mLinearLayout.setOnTouchListener(this);
        mRelativeLayout = findViewById(R.id.lock_activity_layout);
        timeView=findViewById(R.id.time_textView);
        timeWeekView=findViewById(R.id.time_week_view);
        timeYYMMDDView=findViewById(R.id.time_year_month_day_view);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int key = event.getKeyCode();
        switch (key) {
            case KeyEvent.KEYCODE_BACK:
                return true;
            case KeyEvent.KEYCODE_MENU:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 隐藏导航栏，状态栏以及底部虚拟键
     */
    protected void hideBottomUIMenu() {
        //隐藏导航栏和状态栏
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        //隐藏虚拟键，并显示全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View view = this.getWindow().getDecorView();
            view.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    private void handleMoveView(float x) {
        float move_x = x - mStartX;
        if (move_x < 0) {
            move_x = 0;
        }
        mRelativeLayout.setTranslationX(move_x);

        float mWidthFloat = mWidth;
        if (mRelativeLayout.getBackground() != null) {
            mRelativeLayout.getBackground().setAlpha((int)
                    ((mWidthFloat - mRelativeLayout.getTranslationX()) / mWidthFloat * 200));
        }
    }

    private void doTriggerEvent(float x) {
        float move_x = x - mStartX;
        if (move_x > (mWidth * 0.4)) {
            moveMoveView(mWidth - mRelativeLayout.getLeft(), true);
        } else {
            moveMoveView(-mRelativeLayout.getLeft(), false);
        }
    }

    private void moveMoveView(float to, boolean exit) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mRelativeLayout, "translationX", to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mRelativeLayout.getBackground() != null) {
                    mRelativeLayout.getBackground().setAlpha((int)
                            ((mWidth - mRelativeLayout.getTranslationX()) / mWidth * 200));
                }
            }
        });

        animator.setDuration(250).start();
        if (exit) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    LogUtil.d("LOCKSCREEN+", "结束锁屏界面！");
                    LockScreenActivity.this.finish();
                    super.onAnimationEnd(animation);
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float nx = event.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.d("LockScreenActivity", "mStartX : " + mStartX);
                mStartX = nx;
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.d("LockScreenActivity", "nx : " + nx);
                handleMoveView(nx);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                doTriggerEvent(nx);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
