package com.cdtc.birthday;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LockScreenActivity extends AppCompatActivity implements View.OnTouchListener {

    private float mStartX;
    private LinearLayout mLinearLayout;
    private RelativeLayout mRelativeLayout;
    private float mWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                |WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                |WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        mLinearLayout=findViewById(R.id.slip_layout);
//        mLinearLayout.setOnTouchListener(this);
        mRelativeLayout=findViewById(R.id.lock_activity_layout);

        WindowManager wm=(WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        float density=dm.density;
        mWidth=width/density;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int key=event.getKeyCode();
        switch (key){
            case KeyEvent.KEYCODE_BACK:
                return true;
            case KeyEvent.KEYCODE_MENU:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void handleMoveView(float x){
        float move_x=x-mStartX;
        if (move_x<0){
            move_x=0;
        }
        mLinearLayout.setTranslationX(move_x);

        float mWidthFloat=mWidth;
        if (mRelativeLayout.getBackground()!=null){
            mRelativeLayout.getBackground().setAlpha((int)
                    ((mWidthFloat-mLinearLayout.getTranslationX())/mWidthFloat*200));
        }
    }

    private void doTriggerEvent(float x){
        float move_x=x-mStartX;
        if (move_x>(mWidth*0.4)){
            moveMoveView(mWidth-mLinearLayout.getLeft(),true);
        }else{
            moveMoveView(-mLinearLayout.getLeft(),false);
        }
    }

    private void moveMoveView(float to,boolean exit){
        ObjectAnimator animator=ObjectAnimator.ofFloat(mLinearLayout,"translationX",to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mRelativeLayout.getBackground()!=null){
                    mRelativeLayout.getBackground().setAlpha((int)
                            ((mWidth-mLinearLayout.getTranslationX())/mWidth*200));
                }
            }
        });

        animator.setDuration(250).start();
        if (exit){
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    LogUtil.d("LOCKSCREEN+","结束锁屏界面！");
                    LockScreenActivity.this.finish();
                    super.onAnimationEnd(animation);
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action=event.getAction();
        final float nx=event.getX();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mStartX=nx;
                break;
            case MotionEvent.ACTION_MOVE:
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
