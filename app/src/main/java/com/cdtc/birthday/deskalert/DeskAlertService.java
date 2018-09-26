package com.cdtc.birthday.deskalert;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.cdtc.birthday.data.BirthBean;
import com.cdtc.birthday.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DeskAlertService extends Service {
    private final static String TAG = "TrafficService";
    public static int OPEN = 0;
    public static int CLOSE = 1;

    private FloatView mFloatView;
    private TextView tv_traffic;
    private final int delayTime = 1000;

    private int count = 0;
    private Handler mHandler = new Handler();
    private Runnable mRefresh = new Runnable() {
        public void run() {
            if (mFloatView != null && mFloatView.isShow()) {
                count++;
                if (count== birthBeans.size()){
                    count=0;
                }
                Calendar time=Calendar.getInstance();
                long hour=time.get(Calendar.HOUR_OF_DAY);
                long minute=time.get(Calendar.MINUTE);
                long second=time.get(Calendar.SECOND);
                String name= birthBeans.get(count).getName();
                String birthday= birthBeans.get(count).getBirthday();
                String desc = hour+":"+minute+":"+second;
                tv_traffic.setText(desc);
            }
            mHandler.postDelayed(this, delayTime);
        }
    };
    private List<BirthBean> birthBeans;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        if (mFloatView == null) {
            mFloatView = new FloatView(this);
            mFloatView.setLayout(R.layout.float_traffic);
            tv_traffic = mFloatView.mContentView.findViewById(R.id.tv_traffic);
        }
        mHandler.postDelayed(mRefresh, 0);

        birthBeans = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            birthBeans.add(new BirthBean("李刚", "1999-9-23", 1));
            birthBeans.add(new BirthBean("东方红叶", "2003-10-12", 4));
            birthBeans.add(new BirthBean("钟晓珊", "1998-10-12", 0));
            birthBeans.add(new BirthBean("Peter", "1998-2-17", 2));
            birthBeans.add(new BirthBean("Mr.Li", "2013-3-15", 6));
            birthBeans.add(new BirthBean("Sweven Tears", "2005-7-21", 5));
            birthBeans.add(new BirthBean("小落", "1997-9-15", 3));
            birthBeans.add(new BirthBean("小乔", "2026-2-16", 7));
        }

        tv_traffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_traffic.setText("李刚");
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int type = intent.getIntExtra("type", OPEN);
            if (type == OPEN) {
                if (mFloatView != null && !mFloatView.isShow()) {
                    mFloatView.show();
                }
            } else if (type == CLOSE) {
                if (mFloatView != null && mFloatView.isShow()) {
                    mFloatView.close();
                }
                stopSelf();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRefresh);
    }
}
