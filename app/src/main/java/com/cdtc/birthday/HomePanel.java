package com.cdtc.birthday;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
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
class HomePanel implements View.OnClickListener {

    private Context context;

    private TextView birthCountDownText,
            birthYearMonthText, birthDateText, birthWeekText,
            birthLunarYearText, birthLunarMonthDateText, birthConstellationText;

    private LinearLayout birthLayout;

    private int birthYear;
    private int birthMonth;
    private int birthDate;

    private int fontColor;

    private String name;

    HomePanel(Context context, LinearLayout birthLayout, TextView birthCountDownText, TextView birthYearMonthText, TextView birthDateText, String[] broth, String name, int fontColor) {
        this.context = context;
        this.birthLayout = birthLayout;
        this.birthCountDownText = birthCountDownText;
        this.birthYearMonthText = birthYearMonthText;
        this.birthDateText = birthDateText;
        this.birthYear = Integer.parseInt(broth[0]);
        this.birthMonth = Integer.parseInt(broth[1]);
        this.birthDate = Integer.parseInt(broth[2]);
        this.name = name;
        this.fontColor = fontColor;
        initTextView();
        initData();
    }

    /**
     * [代码添加新TextView]
     */
    private void initTextView() {
        birthWeekText = addTextView();
        birthLunarYearText = addTextView();
        birthLunarMonthDateText = addTextView();
        birthConstellationText = addTextView();

        birthWeekText.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);

        birthLayout.addView(birthWeekText);
        birthLayout.addView(birthLunarYearText);
        birthLayout.addView(birthLunarMonthDateText);
        birthLayout.addView(birthConstellationText);
    }

    /**
     * [初始化数据]
     */
    @SuppressLint("SetTextI18n")
    private void initData() {

        int age = getAge(birthYear, birthMonth, birthDate);

        birthCountDownText.setTextColor(fontColor);
        birthYearMonthText.setTextColor(fontColor);

        Calendar now = Calendar.getInstance();
        birthYear = now.get(Calendar.YEAR);

        if (name.equals("无记录")) {
            birthMonth = now.get(Calendar.MONTH) + 1;
            birthDate = now.get(Calendar.DATE);
            birthCountDownText.setText("您还未添加\n生日记录");
        } else {
            if (birthMonth < now.get(Calendar.MONTH) + 1) {
                birthYear = birthYear + 1;
            } else if (birthMonth == now.get(Calendar.MONTH) + 1) {
                if (birthDate < now.get(Calendar.DATE)) {
                    birthYear = birthYear + 1;
                }
            }
            int remainTime = getBetweenDays();
            if (remainTime != 0) {
                birthCountDownText.setText(name + "\n" + age + "岁生日\n倒计时:\n" + remainTime + "天");
            } else {
                birthCountDownText.setText(name + "\n" + age + "岁生日\n就是今天!");
            }
        }

        birthYearMonthText.setText(birthYear + "年" + birthMonth + "月");
        birthDateText.setText(birthDate + "");

        birthWeekText.setText(getWeekOfDate(birthYear, birthMonth, birthDate));
        // Extra Information
        birthLunarYearText.setText("戊戌年" + "\t" + "狗年");
        birthLunarMonthDateText.setText("八月" + "\t" + "十五");
        birthConstellationText.setText("天秤座");

        birthLayout.setOnClickListener(this);


    }

    /**
     * [新建TextView]
     *
     * @return .
     */
    private TextView addTextView() {
        TextView textView = new TextView(context);
        textView.setTextColor(fontColor);
        textView.setTextSize(18);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setPaddingRelative(0, 16, 0, 16);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        textView.setLayoutParams(lp);

        return textView;
    }

    /**
     * [计算当天与生日之间的天数]
     *
     * @return .
     */
    private int getBetweenDays() {

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
    private static String getWeekOfDate(int year, int month, int date) {
        Date dt = new Date(year - 1900, month - 1, date);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    private int getAge(int birthYear, int birthMonth, int birthDate) {
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.birth_layout) {
            //TODO 打开信息详情页面
//            Intent intent=new Intent(context,EditActivity.class);
//            intent.putExtra("name",name);
//            intent.putExtra("birthday",birthday);
//            context.startActivity(intent);
            Toast.makeText(context, "打开编辑页", Toast.LENGTH_SHORT).show();
        }
    }
}
