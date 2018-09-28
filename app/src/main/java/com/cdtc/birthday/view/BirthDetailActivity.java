package com.cdtc.birthday.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.cdtc.birthday.AddBirthActivity;
import com.cdtc.birthday.MainActivity;
import com.cdtc.birthday.R;
import com.cdtc.birthday.data.BornDay;
import com.cdtc.birthday.utils.LogUtil;
import com.cdtc.birthday.utils.ToastUtil;

import java.util.Calendar;

public class BirthDetailActivity extends AppCompatActivity implements View.OnTouchListener {

    public static final int RESULT = 300, REQUEST = 301;

    private EditText birthDetailEditTextName;
    private TextView birthDetailTextViewBirthday;
    private TextView birthDetailTextViewNextBirthday;
    private EditText birthDetailEditTextAge;
    private TextView birthDetailTextViewClockTime;
    private Switch birthDetailSwitchIsLunarBirth;
    private Switch birthDetailSwitchIsLockScreen;

    private TextView actionBarTitle;
    private TextView actionBarEdit;

    private boolean editState;

    private Bundle backData;
    private String name;
    private int[] birthday;
    private int[] nextBirth;
    private int age;
    private int[] clockTime;
    private boolean isLunarBirth;
    private boolean isLockScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth_detail);

        // 设置字体大小不随系统字体大小的改变而改变
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

        initActionBar();
        initId();
        initData();
        initListener();
    }

    /**
     * 自定义actionBar
     */
    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_home);
    }

    /**
     * 初始化ID
     */
    private void initId() {
        actionBarTitle = findViewById(R.id.actionbar_title);
        actionBarEdit = findViewById(R.id.action_bar_add);

        birthDetailEditTextName = findViewById(R.id.birth_detail_edit_text_name);
        birthDetailTextViewBirthday = findViewById(R.id.birth_detail_text_view_birthday);
        birthDetailTextViewNextBirthday = findViewById(R.id.birth_detail_text_view_next_birth);
        birthDetailEditTextAge = findViewById(R.id.birth_detail_edit_text_age);
        birthDetailTextViewClockTime = findViewById(R.id.birth_detail_text_view_clock_time);
        birthDetailSwitchIsLunarBirth = findViewById(R.id.birth_detail_switch_is_lunar_birth);
        birthDetailSwitchIsLockScreen = findViewById(R.id.birth_detail_switch_is_lock_screen);
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SetTextI18n")
    private void initData() {
        actionBarTitle.setText("生日详情");
        actionBarEdit.setText("编辑");
        actionBarEdit.setTextSize(16);
        actionBarEdit.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        if (intent != null) {
            bundle = intent.getBundleExtra("allMessage");
        }

        name = bundle.getString("name");
        birthday = bundle.getIntArray("birthday");
        nextBirth = bundle.getIntArray("nextBirth");
        age = bundle.getInt("age");
        clockTime = bundle.getIntArray("clockTime");
        isLunarBirth = bundle.getBoolean("isLunarBirth");
        isLockScreen = bundle.getBoolean("isLockScreen");

        birthDetailEditTextName.setText(name);

        birthDetailTextViewBirthday.setText(birthday != null ? birthday[0] + "年" + birthday[1] + "月" + birthday[2] + "日" : "");

        birthDetailTextViewNextBirthday.setText(nextBirth != null ? nextBirth[0] + "年" + nextBirth[1] + "月" + nextBirth[2] + "日" : "");

        birthDetailEditTextAge.setText(age + "");

        birthDetailTextViewClockTime.setText(clockTime != null ? clockTime[0] + ":" + clockTime[1] : "未设置提醒时间");

        birthDetailSwitchIsLunarBirth.setChecked(isLunarBirth);
        birthDetailSwitchIsLockScreen.setChecked(isLockScreen);

        // 初始设置不可编辑
        setEditState(false);
    }

    /**
     * 设置监听
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        actionBarEdit.setOnTouchListener(this);
        actionBarEdit.setOnClickListener(view -> {
            if (actionBarEdit.getText().toString().equals("编辑")) {
                setEditState(true);
                actionBarEdit.setText("完成");
            } else if (actionBarEdit.getText().toString().equals("完成")) {
                setEditState(false);
                saveData();
                actionBarEdit.setText("编辑");
            }
        });

        birthDetailTextViewClockTime
                .setOnClickListener(view -> setWakeTime(clockTime));

        birthDetailTextViewNextBirthday
                .setOnClickListener(view -> setNextBirth(nextBirth));

        birthDetailSwitchIsLunarBirth
                .setOnCheckedChangeListener((btn, b) -> isLunarBirth = b);

        birthDetailSwitchIsLockScreen
                .setOnCheckedChangeListener((btn, b) -> isLockScreen = b);
    }

    private void saveData() {
        name = birthDetailEditTextName.getText().toString();
        age = Integer.parseInt(birthDetailEditTextAge.getText().toString());
        BornDay born;
        if (isLunarBirth) {
            born = BornDay.getLunarBornDay(nextBirth[0], nextBirth[1], nextBirth[2], age);
        } else {
            born = BornDay.getSolarBornDay(nextBirth[0], nextBirth[1], nextBirth[2], age);
        }
        birthday = new int[]{born.year, born.month, born.date};

        backData=new Bundle();
        backData.putString("name", name);
        backData.putIntArray("birthday", birthday);
        backData.putIntArray("nextBirth", nextBirth);
        backData.putInt("age", age);
        backData.putBoolean("isLunarBirth", isLunarBirth);
        backData.putBoolean("isLockScreen", isLockScreen);
        backData.putIntArray("clockTime", clockTime);
    }

    /**
     * 设置下一次生日时间
     *
     * @param birth date
     */
    private void setNextBirth(int[] birth) {
        int year = birth[0];
        int month = birth[1];
        int date = birth[2];
        @SuppressLint("SetTextI18n")
        DatePickerDialog dialog = new DatePickerDialog(BirthDetailActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    int month1 = monthOfYear + 1;
                    birthDetailTextViewNextBirthday.setText(year1 + "年" + month1 + "月" + dayOfMonth + "日");
                    nextBirth = new int[]{year1, month1, dayOfMonth};
                }, year, month, date);
        dialog.show();
    }

    /**
     * 设置提醒时间
     *
     * @param clock time
     */
    private void setWakeTime(int[] clock) {
        int hour;
        int minute;
        Calendar currentSetTime = Calendar.getInstance();

        if (clock != null) {
            hour = clock[0];
            minute = clock[1];
        } else {
            hour = currentSetTime.get(Calendar.HOUR_OF_DAY);
            minute = currentSetTime.get(Calendar.MINUTE);
        }

        @SuppressLint("SetTextI18n")
        TimePickerDialog timePickerDialog = new TimePickerDialog(BirthDetailActivity.this,
                (view, hourOfDay, minuteOfDay) -> {
                    if (minuteOfDay < 10) {
                        birthDetailTextViewClockTime.setText(hourOfDay + ":0" + minuteOfDay);
                    } else {
                        birthDetailTextViewClockTime.setText(hourOfDay + ":" + minuteOfDay);
                    }
                    clockTime = new int[]{hourOfDay, minuteOfDay};
                }, hour, minute, false);
        timePickerDialog.show();
    }

    /**
     * @return 获取编辑状态
     */
    public boolean isEditState() {
        return editState;
    }

    /**
     * @param editState 编辑状态否
     */
    public void setEditState(boolean editState) {
        birthDetailEditTextName.setEnabled(editState);
        birthDetailTextViewNextBirthday.setEnabled(editState);
        birthDetailEditTextAge.setEnabled(editState);
        birthDetailSwitchIsLunarBirth.setEnabled(editState);
        birthDetailSwitchIsLockScreen.setEnabled(editState);
        birthDetailTextViewClockTime.setEnabled(editState);

        this.editState = editState;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionBarEdit.setTextColor(Color.GRAY);
                break;
            case MotionEvent.ACTION_MOVE:
                actionBarEdit.setTextColor(Color.WHITE);
                break;
            case MotionEvent.ACTION_UP:
                actionBarEdit.setTextColor(Color.WHITE);
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isEditState()) {
                showTips();
            } else {
                Intent intent=new Intent(BirthDetailActivity.this, MainActivity.class);
                intent.putExtra("allMessage",backData);
                setResult(RESULT,intent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showTips() {
        ToastUtil.showShort(this, "提示是否保存");
    }
}
