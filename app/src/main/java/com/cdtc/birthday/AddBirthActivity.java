package com.cdtc.birthday;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddBirthActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private Button addBirthBirth;
    private LinearLayout wakeUplayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_birthinfo);

        init();
    }

    private void init() {
        addBirthBirth = findViewById(R.id.add_birth_birth);
        addBirthBirth.setOnClickListener(this);
        wakeUplayout = findViewById(R.id.wake_time);
        wakeUplayout.setOnTouchListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_birth_birth:
                final Calendar currentTime = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(AddBirthActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                Calendar calendar=Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear + 1);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                LogUtil.d("日历选择", currentTime.getTimeInMillis() + "");
                            }
                        }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH)
                        , currentTime.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.wake_time) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    wakeUplayout.setBackgroundResource(R.color.gray);
                    break;
                case MotionEvent.ACTION_MOVE:
                    wakeUplayout.setBackgroundResource(0);
                    break;
                case MotionEvent.ACTION_UP:
                    wakeUplayout.setBackgroundResource(0);
                    Calendar currentTime=Calendar.getInstance();
                    TimePickerDialog timePickerDialog=new TimePickerDialog(AddBirthActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                                    Calendar calendar=Calendar.getInstance();
                                    calendar.setTimeInMillis(System.currentTimeMillis());
                                    calendar.set(Calendar.HOUR,hourOfDay);
                                    calendar.set(Calendar.MINUTE,minuteOfDay);
                                }
                            },currentTime.get(Calendar.HOUR_OF_DAY),currentTime.get(Calendar.MINUTE)
                            ,false);
                    timePickerDialog.show();
                    break;
                default:
                    break;
            }
        }
        return true;
    }
}
