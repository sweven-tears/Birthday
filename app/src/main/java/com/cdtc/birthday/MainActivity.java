package com.cdtc.birthday;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout panelCalender;
    private LinearLayout panelHome;
    private LinearLayout panelMine;

    private static final int SHOW_HOME = 1, SHOW_CALENDER = 2, SHOW_MINE = 3;

    private LinearLayout birthLayout;
    private TextView birthCountDown, birthYearMonthText, birthDateText;

    private static final int backgroundImage[] = {
            R.drawable.home_background_01, R.drawable.home_background_02,
            R.drawable.home_background_03, R.drawable.home_background_04,
            R.drawable.home_background_05, R.drawable.home_background_06,
            R.drawable.home_background_07, R.drawable.home_background_08,};

    private static final int foregroundImage[] = {
            R.drawable.home_foreground_01, R.drawable.home_foreground_02,
            R.drawable.home_foreground_03, R.drawable.home_foreground_04,
            R.drawable.home_foreground_05, R.drawable.home_foreground_06,
            R.drawable.home_foreground_07, R.drawable.home_foreground_08,};

    private static final int fontColor[] = {
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE};

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
        initListener();

        startAllService();
    }

    /**
     * [初始化布局中的id资源]
     */
    private void initId() {
        panelHome = findViewById(R.id.id_home);
        panelCalender = findViewById(R.id.id_calender);
        panelMine = findViewById(R.id.id_mine);

        birthLayout = findViewById(R.id.birth_layout);
        birthCountDown = findViewById(R.id.birth_count_down);
        birthYearMonthText = findViewById(R.id.birth_year_month);
        birthDateText = findViewById(R.id.birth_day);
    }

    /**
     * [初始化数据]
     */
    private void initData() {
        navigation.setSelectedItemId(R.id.navigation_home);
        showPanel(SHOW_HOME);

        //这三条信息是从数据库获取的
        int style=0;
        String brothDate = "2005-10-1";
        String name = "小强";
//        String birthday="0000-00-00";
//        String name="无记录";

        new HomePanel(MainActivity.this, birthLayout,
                birthCountDown, birthYearMonthText, birthDateText,
                brothDate.split("-"), name, fontColor[style]);

        panelHome.setBackgroundResource(backgroundImage[style]);
        birthLayout.setBackgroundResource(foregroundImage[style]);

    }

    /**
     * [显示当前页面]
     */
    private void showPanel(int show) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        panelHome.setVisibility(View.INVISIBLE);
        panelMine.setVisibility(View.INVISIBLE);
        panelCalender.setVisibility(View.INVISIBLE);

        if (show == SHOW_HOME) {
            panelHome.setVisibility(View.VISIBLE);
            actionBar.setTitle("生辰日");
        } else if (show == SHOW_CALENDER) {
            panelCalender.setVisibility(View.VISIBLE);
            actionBar.setTitle("日历");
        } else if (show == SHOW_MINE) {
            panelMine.setVisibility(View.VISIBLE);
            actionBar.setTitle("设置");
        }
    }

    /**
     * [初始化所有的监听器]
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
    }

    /**
     * 启动Service
     */
    private void startAllService() {
        startService(new Intent(this, LongRunningService.class));
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    }

    /**
     * 请求权限
     */
    private void requstPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS}, 1);
        } else {
            Toast.makeText(this, "已授权！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已授权！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "未授权！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = navigation.getSelectedItemId();
        if (id == R.id.navigation_home) {
            showPanel(SHOW_HOME);
        } else if (id == R.id.navigation_calender) {
            showPanel(SHOW_CALENDER);
        } else if (id == R.id.navigation_mine) {
            showPanel(SHOW_MINE);
        }
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("Main", "MainActivity:onDestroy()");
        stopService(new Intent(this, LongRunningService.class));
        super.onDestroy();
    }
}


