package com.cdtc.birthday;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeThread extends Thread {
    private TextView timeView, timeWeekView, timeYYMMDDView;
    private int msgKey1 = 22;

    public TimeThread(TextView timeView, TextView timeWeekView, TextView timeYYMMDDView) {
        this.timeView = timeView;
        this.timeWeekView = timeWeekView;
        this.timeYYMMDDView = timeYYMMDDView;
    }

    @Override
    public void run() {
        do {
            try {
                Message message = new Message();
                message.what = msgKey1;
                mHandler.sendMessage(message);
                Thread.sleep(60000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 22:
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                    String date = sdf.format(new Date());
                    timeView.setText(date);
                    timeWeekView.setText(getWeek());
                    timeYYMMDDView.setText(getYYMMDD());
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取今天星期几
     *
     * @return
     */
    public static String getWeek() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        switch (week) {
            case 1:
                return "星期天";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    /**
     * 获取今天星期几
     *
     * @return
     */
    public static String getYYMMDD() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (month < 10) {
            if (day < 10) {
                return "——" + year + ".0" + month + ".0" + day;
            } else {
                return "——" + year + ".0" + month + "." + day;
            }
        } else {
            if (day < 10) {
                return "——" + year + "." + month + ".0" + day;
            } else {
                return "——" + year + "." + month + "." + day;
            }
        }
    }
}
