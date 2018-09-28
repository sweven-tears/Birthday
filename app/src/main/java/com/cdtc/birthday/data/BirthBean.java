package com.cdtc.birthday.data;

import com.cdtc.birthday.DealHomeBirthDate;
import com.cdtc.birthday.LunarSolarConverter;

import java.util.Calendar;

/**
 * Created by Sweven on 2018/9/23.
 * Email:sweventears@Foxmail.com
 * <p>
 * 生日主的相关信息及设置
 */
public class BirthBean {

    /**
     * 生日主姓名
     */
    private String name;

    /**
     * 生日主出生日期（公历）
     * [超出当前时间则为未来出生时间]
     */
    private BornDay birthday;

    /**
     * 年龄
     */
    private int age;

    /**
     * 提示文本
     */
    private String tipText;

    /**
     * 过农历生日还是公历生日
     */
    private boolean isLunarBirth;

    /**
     * 下一次生日公历年份
     */
    private int nextBirthYear;

    /**
     * 下一次生日公历月份
     */
    private int nextBirthMonth;

    /**
     * 下一次生日公历号数
     */
    private int nextBirthDate;

    /**
     * 是否在锁屏界面显示
     */
    private boolean isLockScreen;

    /**
     * 当天具体提醒时间，二维数组 {12,12} -> {小时,分钟}
     */
    private int tipTime[];

    /**
     * 生日主卡片样式编号
     */
    private int imageStyle;

    private static final int NEXT_BIRTH_YEAR = 0, NEXT_BIRTH_MONTH = 1, NEXT_BIRTH_DATE = 2;

    public BirthBean(String name, BornDay birthday, int imageStyle) {
        this.name = name;
        this.birthday = birthday;
        this.imageStyle = imageStyle;
    }

    public BirthBean(String name, BornDay birthday, boolean isLunarBirth, int imageStyle) {
        this.name = name;
        this.birthday = birthday;
        this.isLunarBirth = isLunarBirth;
        this.imageStyle = imageStyle;
    }

    public BirthBean() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BornDay getBirthday() {
        return birthday;
    }

    public void setBirthday(BornDay birthday) {
        this.birthday = birthday;
    }

    /**
     * [通过出生日期和当前日期计算]
     *
     * @return age
     */
    public int getAge() {
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH) + 1;
        int nowDate = cal.get(Calendar.DATE);

        int birthYear = birthday.year;
        int birthMonth = birthday.month;
        int birthDate = birthday.date;

        age = nowYear - birthYear;

        if (nowMonth <= birthMonth) {
            if (nowMonth == birthMonth) {
                if (nowDate < birthDate) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * [根据出生日期、生日主昵称、当前日期计算]
     *
     * @return 倒计时文本
     */
    public String getTipText() {
        int nextYear = getNextBirthYear();
        int nextMonth = getNextBirthMonth();
        int nextDate = getNextBirthDate();

        if (getName().equals("无记录")) {
            tipText = "您还未添加\\n生日记录";
        } else {
            if (getAge() < 0) {
                int remainTime = DealHomeBirthDate.getBetweenDays(nextYear, nextMonth, nextDate);
                tipText = name + "\n离出生还有\n" + remainTime + "天\no(ﾟДﾟ)っ！";
            } else {
                int remainTime = DealHomeBirthDate.getBetweenDays(nextYear, nextMonth, nextDate);
                if (remainTime != 0) {
                    tipText = name + "\n" + getAge() + "岁生日\n倒计时:\n" + remainTime + "天";
                } else {
                    tipText = name + "\n" + getAge() + "岁生日\n就是今天!";
                }
            }
        }
        return tipText;
    }

    public boolean isLunarBirth() {
        return isLunarBirth;
    }

    public void setLunarBirth(boolean lunarBirth) {
        isLunarBirth = lunarBirth;
    }

    public int getNextBirthYear() {
        return isLunarBirth() ?
                getNextLunarBirthday()[NEXT_BIRTH_YEAR] :
                getNextSolarBirthday()[NEXT_BIRTH_YEAR];
    }

    public int getNextBirthMonth() {
        return isLunarBirth() ?
                getNextLunarBirthday()[NEXT_BIRTH_MONTH] :
                getNextSolarBirthday()[NEXT_BIRTH_MONTH];
    }

    public int getNextBirthDate() {
        return isLunarBirth() ?
                getNextLunarBirthday()[NEXT_BIRTH_DATE] :
                getNextSolarBirthday()[NEXT_BIRTH_DATE];
    }

    public boolean isLockScreen() {
        return isLockScreen;
    }

    public void setLockScreen(boolean lockScreen) {
        isLockScreen = lockScreen;
    }

    public int getImageStyle() {
        return imageStyle;
    }

    public int[] getTipTime() {
        return tipTime;
    }

    public void setTipTime(int[] tipTime) {
        this.tipTime = tipTime;
    }

    public void setImageStyle(int imageStyle) {
        this.imageStyle = imageStyle;
    }

    /**
     * @return 获取下一次公历生日的公历年月日
     */
    private int[] getNextSolarBirthday() {
        int birthYear = birthday.year;
        int birthMonth = birthday.month;
        int birthDate = birthday.date;

        Calendar now = Calendar.getInstance();

        int nextYear = now.get(Calendar.YEAR);
        int nextMonth;
        int nextDate;
        if (name.equals("无记录")) {
            nextMonth = now.get(Calendar.MONTH) + 1;
            nextDate = now.get(Calendar.DATE);
        } else {
            if (birthYear <= now.get(Calendar.YEAR)) {
                if (birthMonth < now.get(Calendar.MONTH) + 1) {
                    nextYear = nextYear + 1;
                } else if (birthMonth == now.get(Calendar.MONTH) + 1) {
                    if (birthDate < now.get(Calendar.DATE)) {
                        nextYear = nextYear + 1;
                    }
                }
            } else {
                nextYear = birthYear;
            }
            nextMonth = birthMonth;
            nextDate = birthDate;
        }
        return new int[]{nextYear, nextMonth, nextDate};
    }

    /**
     * @return 获取下一次阴历生日的公历年月日
     */
    private int[] getNextLunarBirthday() {
        int birthYear = birthday.year;
        int birthMonth = birthday.month;
        int birthDate = birthday.date;

        // 实例化公历出生日期
        LunarSolarConverter.Solar birthSolar = new LunarSolarConverter.Solar();
        birthSolar.solarYear = birthYear;
        birthSolar.solarMonth = birthMonth;
        birthSolar.solarDay = birthDate;

        // 转为农历出生日期
        LunarSolarConverter.Lunar birthLunar = LunarSolarConverter.SolarToLunar(birthSolar);
        birthYear = birthLunar.lunarYear;
        birthMonth = birthLunar.lunarMonth;
        birthDate = birthLunar.lunarDay;

        Calendar current = Calendar.getInstance();
        int nowYear = current.get(Calendar.YEAR);
        int nowMonth = current.get(Calendar.MONTH) + 1;
        int nowDate = current.get(Calendar.DATE);

        // 实例化公历当前日期
        LunarSolarConverter.Solar nowSolar = new LunarSolarConverter.Solar();
        nowSolar.solarYear = nowYear;
        nowSolar.solarMonth = nowMonth;
        nowSolar.solarDay = nowDate;

        // 转为农历当前日期
        LunarSolarConverter.Lunar nowLunar = LunarSolarConverter.SolarToLunar(nowSolar);
        nowYear = nowLunar.lunarYear;
        nowMonth = nowLunar.lunarMonth;
        nowDate = nowLunar.lunarDay;

        // 计算下一次农历生日的日期
        LunarSolarConverter.Lunar nextLunar = new LunarSolarConverter.Lunar();
        nextLunar.lunarYear = nowYear;
        if (name.equals("无记录")) {
            nextLunar.lunarMonth = nowMonth;
            nextLunar.lunarDay = nowDate;
        } else {
            if (birthYear <= nowYear) {
                if (birthMonth < nowMonth) {
                    nextLunar.lunarYear = nextLunar.lunarYear + 1;
                } else if (birthMonth == nowMonth) {
                    if (birthDate < nowDate) {
                        nextLunar.lunarYear = nextLunar.lunarYear + 1;
                    }
                }
            } else {
                nextLunar.lunarYear = birthYear;
            }
            nextLunar.lunarMonth = birthMonth;
            nextLunar.lunarDay = birthDate;
        }
        // 计算出下一次的农历生日后转为公历日期
        LunarSolarConverter.Solar nextSolar = LunarSolarConverter.LunarToSolar(nextLunar);

        return new int[]{nextSolar.solarYear, nextSolar.solarMonth, nextSolar.solarDay};
    }

    @Override
    public String toString() {
        return "BirthBean{" +
                "name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", imageStyle=" + imageStyle +
                '}';
    }
}
