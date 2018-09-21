package com.cdtc.birthday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout panelHome,panelCalender,panelMine;

    private static final int SHOW_HOME=1,SHOW_CALENDER=2,SHOW_MINE=3;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showPanel(SHOW_HOME);
                    return true;
                case R.id.navigation_calender:
                    showPanel(SHOW_CALENDER);
                    return true;
                case R.id.navigation_mine:
                    showPanel(SHOW_MINE);
                    return true;
            }
            return false;
        }
    };
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initId();
        initData();
    }

    /**
     * [初始化布局中的id资源]
     */
    private void initId() {
        panelHome=findViewById(R.id.id_home);
        panelCalender=findViewById(R.id.id_calender);
        panelMine=findViewById(R.id.id_mine);
    }

    /**
     * [初始化数据]
     */
    private void initData() {
        showPanel(SHOW_HOME);
    }

    /**
     *[显示当前页面]
     */
    private void showPanel(int show) {
        panelHome.setVisibility(View.INVISIBLE);
        panelMine.setVisibility(View.INVISIBLE);
        panelCalender.setVisibility(View.INVISIBLE);
        if (show==SHOW_HOME){
            panelHome.setVisibility(View.VISIBLE);
        }
        else if (show==SHOW_CALENDER){
            panelCalender.setVisibility(View.VISIBLE);
        }
        else if (show==SHOW_MINE){
            panelMine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id=navigation.getSelectedItemId();
        if (id==R.id.navigation_home){
            showPanel(SHOW_HOME);
        }
        else if (id==R.id.navigation_calender){
            showPanel(SHOW_CALENDER);
        }
        else if (id==R.id.navigation_mine){
            showPanel(SHOW_MINE);
        }
    }
}
