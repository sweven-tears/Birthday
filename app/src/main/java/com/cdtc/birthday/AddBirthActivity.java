package com.cdtc.birthday;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddBirthActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private TextView addBirthBirth;
    private TextView wakeUpText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_birthinfo);

        init();
    }

    private void init() {
        addBirthBirth = findViewById(R.id.add_birth_birth);
        addBirthBirth.setOnTouchListener(this);
        wakeUpText = findViewById(R.id.wake_time_text);
        wakeUpText.setOnTouchListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        }else if(view.getId()==R.id.add_birth_birth){
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
                        int month=monthOfYear+1;
                        currentTime.setTimeInMillis(System.currentTimeMillis());
                        currentTime.set(Calendar.YEAR, year);
                        currentTime.set(Calendar.MONTH, month);
                        currentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        addBirthBirth.setText(year + "年" + month + "月" + dayOfMonth+"日");
                        LogUtil.d("日历选择", year + "年" + month + "月" + dayOfMonth+"日");
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
        int hour = currentSetTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentSetTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddBirthActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        currentSetTime.setTimeInMillis(System.currentTimeMillis());
                        currentSetTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentSetTime.set(Calendar.MINUTE, minuteOfDay);
                        if (minuteOfDay < 10) {
                            wakeUpText.append(" | "+hourOfDay + ":0" + minuteOfDay);
                        } else {
                            wakeUpText.append(" | "+hourOfDay + ":" + minuteOfDay);
                        }
                        LogUtil.d("TIMEPICKERDIALOG", currentSetTime.getTimeInMillis()+"");
                    }
                }, hour, minute, false);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
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
}
