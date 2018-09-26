package com.cdtc.birthday;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.icu.text.UFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sweven on 2018/9/21.
 * Email:sweventears@Foxmail.com
 */
class DealHomeBirthDate {

    /**
     * [计算当天与生日之间的天数]
     *
     * @return .
     */
    public static int getBetweenDays(int birthYear, int birthMonth, int birthDate) {

        Date today = new Date();
        Date birth = new Date(birthYear - 1900, birthMonth - 1, birthDate);

        Calendar cal = Calendar.getInstance();

        cal.setTime(today);
        long time1 = cal.getTimeInMillis();

        cal.setTime(birth);
        long time2 = cal.getTimeInMillis();

        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * [获取星期]
     *
     * @param year  年
     * @param month 月
     * @param date  日
     * @return 周
     */
    public static String getWeekOfDate(int year, int month, int date) {
        Date dt = new Date(year - 1900, month - 1, date);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * @param birthYear  出生年
     * @param birthMonth 出生月
     * @param birthDate  出生日
     * @return 距下一次生日的年龄
     */
    public static int getAge(int birthYear, int birthMonth, int birthDate) {
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH) + 1;
        int nowDate = cal.get(Calendar.DATE);

        int age = nowYear - birthYear;

        if (nowMonth <= birthMonth) {
            if (nowMonth == birthMonth) {
                if (nowDate < birthDate) age--;
            } else {
                age--;
            }
        }
        return age + 1;
    }

    /**
     * @param year 年
     * @return 传回农历 year年的生肖
     */
    public static String animalsYear(int year) {
        final String[] Animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇",
                "马", "羊", "猴", "鸡", "狗", "猪"};
        return Animals[(year - 4) % 12];
    }

    /**
     * [根据出生日期计算星座]
     *
     * @param birthday 出生日期
     * @return 星座
     */
    public static String constellation(String birthday) {
        final String[] cons = new String[]{"白羊座", "金牛座", "双子座", "巨蟹座", "狮子座",
                "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};
        int position = 0;
        int month = Integer.parseInt(birthday.split("-")[1]);
        int date = Integer.parseInt(birthday.split("-")[2]);
        switch (month) {
            case 3:
                if (date >= 21) {
                    position = 1;
                } else {
                    position = 12;
                }
                break;
            case 4:
                if (date >= 21) {
                    position = 2;
                } else {
                    position = 1;
                }
                break;
            case 5:
                if (date >= 21) {
                    position = 3;
                } else {
                    position = 2;
                }
                break;
            case 6:
                if (date >= 22) {
                    position = 4;
                } else {
                    position = 3;
                }
                break;
            case 7:
                if (date >= 23) {
                    position = 5;
                } else {
                    position = 4;
                }
                break;
            case 8:
                if (date >= 23) {
                    position = 6;
                } else {
                    position = 5;
                }
                break;
            case 9:
                if (date >= 23) {
                    position = 7;
                } else {
                    position = 6;
                }
                break;
            case 10:
                if (date >= 24) {
                    position = 8;
                } else {
                    position = 7;
                }
                break;
            case 11:
                if (date >= 23) {
                    position = 9;
                } else {
                    position = 8;
                }
                break;
            case 12:
                if (date >= 22) {
                    position = 10;
                } else {
                    position = 9;
                }
                break;
            case 1:
                if (date >= 20) {
                    position = 11;
                } else {
                    position = 10;
                }
                break;
            case 2:
                if (date >= 19) {
                    position = 12;
                } else {
                    position = 1;
                }
                break;
        }
        return cons[position - 1];
    }
}
