package com.cdtc.birthday;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cdtc.birthday.utils.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class AddBirthActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, CompoundButton.OnCheckedChangeListener {

    private TextView addBirthBirth;
    private TextView birthCon;
    private TextView birthRel;
    private Switch floatWindow;
    private Switch lockScreen;
    private TextView wakeUpText;

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
        birthCon = findViewById(R.id.add_birth_con);
        birthRel = findViewById(R.id.add_birth_rel);
        floatWindow = findViewById(R.id.float_window_switch);
        lockScreen = findViewById(R.id.lock_screen_switch);
        lockScreen.setOnCheckedChangeListener(this);

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
                        currentSetTime.setTimeInMillis(System.currentTimeMillis());
                        currentSetTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentSetTime.set(Calendar.MINUTE, minuteOfDay);
                        if (minuteOfDay < 10) {
                            wakeUpText.setText("提醒时间 | " + hourOfDay + ":0" + minuteOfDay);
                        } else {
                            wakeUpText.setText("提醒时间 | " + hourOfDay + ":" + minuteOfDay);
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
        stopService(new Intent(this, LockScreenService.class));
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case :
//                break;
//            default:
//                break;
//        }

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
