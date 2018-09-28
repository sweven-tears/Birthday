package com.cdtc.birthday.data;

import com.cdtc.birthday.LunarSolarConverter;

/**
 * [实例化年月日]
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
     * @return 公历生日对应出生日期
     */
    public static BornDay getSolarBornDay(int year, int month, int date, int age) {
        return new BornDay(year - age, month, date);
    }

    /**
     * [默认age为设置的生日当天的年龄]
     *
     * @param year  设置的生日年份
     * @param month 设置的生日月份
     * @param date  设置的生日号数
     * @param age   设置的年龄
     * @return 农历生日对应出生的阳历日期
     */
    public static BornDay getLunarBornDay(int year, int month, int date, int age) {
        LunarSolarConverter.Solar nowSolar = new LunarSolarConverter.Solar();
        nowSolar.solarYear = year;
        nowSolar.solarMonth = month;
        nowSolar.solarDay = date;
        LunarSolarConverter.Lunar lunar = LunarSolarConverter.SolarToLunar(nowSolar);
        lunar.lunarYear=lunar.lunarYear-age;
        LunarSolarConverter.Solar bornSolar=LunarSolarConverter.LunarToSolar(lunar);
        return new BornDay(bornSolar.solarYear, bornSolar.solarMonth, bornSolar.solarDay);
    }

    @Override
    public String toString() {
        return year + "-" + month + "-" + date;
    }
}
