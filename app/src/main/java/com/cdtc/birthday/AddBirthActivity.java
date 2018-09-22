package com.cdtc.birthday;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddBirthActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addBirthBirth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_birthinfo);

        init();
    }

    private void init() {
        addBirthBirth = findViewById(R.id.add_birth_birth);
        addBirthBirth.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_birth_birth:
                final Calendar currentTime=Calendar.getInstance();
                DatePickerDialog dialog=new DatePickerDialog(AddBirthActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                currentTime.set(Calendar.YEAR,year);
                                currentTime.set(Calendar.MONTH,monthOfYear+1);
                                currentTime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                LogUtil.d("日历选择",currentTime.getTimeInMillis()+"");
                            }
                        },currentTime.get(Calendar.YEAR),currentTime.get(Calendar.MONTH)
                        ,currentTime.get(Calendar.DAY_OF_MONTH));
                dialog.show();
        }

    }
}
