package com.cdtc.birthday;

import java.util.Calendar;

/**
 * Created by Sweven on 2018/9/27.
 * Email:sweventears@Foxmail.com
 */
public class BornDay {
    public int year;
    public int month;
    public int date;

    public BornDay(int year, int month, int date) {
        this.year = year;
        this.month = month;
        this.date = date;
    }

    /**
     * [默认age为设置的生日当天的年龄]
     *
     * @param year  设置的生日年份
     * @param month 设置的生日月份
     * @param date  设置的生日号数
     * @param age   设置的年龄
     * @return 出生日期
     */
    public static BornDay getBornDay(int year, int month, int date, int age) {
        return new BornDay(year - age, month, date);
    }

    @Override
    public String toString() {
        return year + "-" + month + "-" + date;
    }
}
