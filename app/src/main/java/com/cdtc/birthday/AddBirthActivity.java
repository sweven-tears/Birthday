package com.cdtc.birthday;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cdtc.birthday.utils.LogUtil;
import com.cdtc.birthday.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class AddBirthActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnTouchListener,
        CompoundButton.OnCheckedChangeListener {
    private static final int RESULT = 200;
    private static final int REQUESt = 201;

    private TextView addBirthBirth;
    private TextView birthRel;
    private TextView birthAge;
    private LinearLayout lsLayout;
    private Switch lockScreen;
    private TextView wakeUpText;
    private TextView ensure;
    private ImageView cancel;
    private int[] birth = new int[3];
    private int[] wake = new int[2];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_birthinfo);

        init();
    }

    /**
     * 绑定id
     */
    private void init() {
        addBirthBirth = findViewById(R.id.add_birth_birth);
        addBirthBirth.setOnTouchListener(this);
        wakeUpText = findViewById(R.id.wake_time_text);
        wakeUpText.setOnTouchListener(this);
        birthRel = findViewById(R.id.add_birth_rel);
        birthAge = findViewById(R.id.add_birth_age);
        lsLayout = findViewById(R.id.lock_screen_layout);
        lsLayout.setOnClickListener(this);
        lockScreen = findViewById(R.id.lock_screen_switch);
        lockScreen.setOnCheckedChangeListener(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_home);
        cancel = findViewById(R.id.actionbar_cancel);
        cancel.setImageResource(R.drawable.ic_chevron_left_black_24dp);
        cancel.setVisibility(View.VISIBLE);
        cancel.setOnClickListener(this);
        ensure = findViewById(R.id.action_bar_add);
        ensure.setText("确定");
        ensure.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        ensure.setVisibility(View.VISIBLE);
        ensure.setOnClickListener(this);
    }

    /**
     * 设置生日
     */
    private void setBirthTime() {
        final Calendar currentTime = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(AddBirthActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int month = monthOfYear + 1;
                        currentTime.setTimeInMillis(System.currentTimeMillis());
                        currentTime.set(Calendar.YEAR, year);
                        currentTime.set(Calendar.MONTH, month);
                        currentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        addBirthBirth.setText(year + "年" + month + "月" + dayOfMonth + "日");
                        birth[0] = year;
                        birth[1] = month;
                        birth[2] = dayOfMonth;
                        LogUtil.d("日历选择", year + "年" + month + "月" + dayOfMonth + "日");
                    }
                }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH)
                , currentTime.get(Calendar.DAY_OF_MONTH));
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                addBirthBirth.setEnabled(true);
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                addBirthBirth.setEnabled(true);
            }
        });
        dialog.show();
    }

    /**
     * 设置提醒时间
     */
    private void setWakeTime() {
        final Calendar currentSetTime = Calendar.getInstance();
        final int hour = currentSetTime.get(Calendar.HOUR_OF_DAY);
        final int minute = currentSetTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddBirthActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        wake[0] = hourOfDay;
                        wake[1] = minuteOfDay;
                        currentSetTime.setTimeInMillis(System.currentTimeMillis());
                        currentSetTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentSetTime.set(Calendar.MINUTE, minuteOfDay);
                        if (minuteOfDay < 10) {
                            wakeUpText.setText(hourOfDay + ":0" + minuteOfDay);
                        } else {
                            wakeUpText.setText(hourOfDay + ":" + minuteOfDay);
                        }
                        LogUtil.d("TIMEPICKERDIALOG", currentSetTime.getTimeInMillis() + "");
                    }
                }, hour, minute, false);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                LogUtil.d("DIALOGCANCEL", hour + " : " + minute);
                wakeUpText.setEnabled(true);
            }
        });
        timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                wakeUpText.setEnabled(true);
            }
        });
        timePickerDialog.show();
    }

    /** 清空信息栏 */
    private void cleanInfo(){
        birthRel.setText("");
        addBirthBirth.setText("");
        birthAge.setText("");
        lockScreen.setChecked(false);
        wakeUpText.setText("");
    }

    /**
     * 检测服务是否启动
     */
    private boolean isServiceWorking(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningServiceInfos =
                (ArrayList<ActivityManager.RunningServiceInfo>)
                        activityManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningServiceInfos.size(); i++) {
            if (runningServiceInfos.get(i).service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(new Intent(this, LockScreenService.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lock_screen_layout:
                if (lockScreen.isChecked()) {
                    lockScreen.setChecked(false);
                } else {
                    lockScreen.setChecked(true);
                }
                break;
            case R.id.action_bar_add:
                new AlertDialog.Builder(AddBirthActivity.this)
                        .setMessage("确定保存吗?")
                        .setPositiveButton("取消", null)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(AddBirthActivity.this, MainActivity.class);
                                if (!TextUtils.isEmpty(birthRel.getText())) {
                                    intent.putExtra("nickName", birthRel.getText().toString());
                                } else {
                                    ToastUtil.showShort(AddBirthActivity.this, "请填写昵称");
                                }
                                if (!TextUtils.isEmpty(addBirthBirth.getText())) {
                                    intent.putExtra("birth_time", birth);
                                } else {
                                    ToastUtil.showShort(AddBirthActivity.this, "请填写出生日期");
                                }
                                if (!TextUtils.isEmpty(birthAge.getText())) {
                                    intent.putExtra("birth_age", Integer.valueOf(birthAge.getText().toString()));
                                } else {
                                    ToastUtil.showShort(AddBirthActivity.this, "请填写年龄");
                                }
                                if (lockScreen.isChecked()) {
                                    intent.putExtra("lock_screen", true);
                                } else {
                                    intent.putExtra("lock_screen", false);
                                }
                                if (!TextUtils.isEmpty(wakeUpText.getText())) {
                                    intent.putExtra("wake_up_time", wake);
                                } else {
                                    ToastUtil.showShort(AddBirthActivity.this, "请填写提醒时间");
                                }
                                setResult(RESULT, intent);
                                cleanInfo();
                            }
                        })
                        .show();
                break;
            case R.id.actionbar_cancel:
                this.finish();
                break;
            default:
                break;

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.wake_time_text) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    wakeUpText.setBackgroundColor(Color.GRAY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    wakeUpText.setEnabled(false);
                    wakeUpText.setBackgroundColor(0);
                    setWakeTime();
                    break;
                default:
                    break;
            }
        } else if (view.getId() == R.id.add_birth_birth) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    addBirthBirth.setBackgroundColor(Color.GRAY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    addBirthBirth.setEnabled(false);
                    addBirthBirth.setBackgroundColor(0);
                    setBirthTime();
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            startService(new Intent(this, LockScreenService.class));
        } else {
            stopService(new Intent(this, LockScreenService.class));
        }
    }


}
