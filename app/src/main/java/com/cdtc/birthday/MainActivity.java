package com.cdtc.birthday;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout panelCalender;
    private LinearLayout panelHome;
    private LinearLayout panelMine;

    private static final int SHOW_HOME = 1, SHOW_CALENDER = 2, SHOW_MINE = 3;

    private RecyclerView birthHomeRecycler;

    private TextView actionBarTitle, actionBarAdd;
    private LinearLayoutManager layoutManager;

    private BottomNavigationView navigation;
    private long firstTime;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // 双击Home键返回第一个生日卡片
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 1000) {
                        firstTime = secondTime;
                    } else {
                        layoutManager.scrollToPositionWithOffset(1, 0);
                    }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_home);

        initId();
        initData();
        initListener();

        startAllService();
    }

    /**
     * [初始化布局中的id资源]
     */
    private void initId() {
        actionBarTitle = findViewById(R.id.actionbar_title);
        actionBarAdd = findViewById(R.id.action_bar_add);

        panelHome = findViewById(R.id.id_home);
        panelCalender = findViewById(R.id.id_calender);
        panelMine = findViewById(R.id.id_mine);

        birthHomeRecycler = findViewById(R.id.home_panel_recycler_view);
    }

    /**
     * [初始化数据]
     */
    private void initData() {
        navigation.setSelectedItemId(R.id.navigation_home);
        showPanel(SHOW_HOME);

        ArrayList<BirthInfo> birthInfos = new ArrayList<>();
        birthInfos.add(new BirthInfo());
        for (int i = 0; i < 5; i++) {
            birthInfos.add(new BirthInfo("李刚", "1999-9-23", 1));
            birthInfos.add(new BirthInfo("东方红叶", "2003-10-12", 4));
            birthInfos.add(new BirthInfo("钟晓珊", "1998-10-12", 0));
            birthInfos.add(new BirthInfo("Peter", "1998-2-17", 2));
            birthInfos.add(new BirthInfo("Mr.Li", "2013-3-15", 6));
            birthInfos.add(new BirthInfo("Sweven Tears", "2005-7-21", 5));
            birthInfos.add(new BirthInfo("小落", "1997-9-15", 3));
            birthInfos.add(new BirthInfo("小乔", "2026-2-16", 7));
        }

        layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPositionWithOffset(1, 0);
        birthHomeRecycler.setLayoutManager(layoutManager);
        new PagerSnapHelper().attachToRecyclerView(birthHomeRecycler);
        birthHomeRecycler.addItemDecoration(new SpaceItemDecoration(70));
        birthHomeRecycler.setItemAnimator(new DefaultItemAnimator());

        BirthHomeAdapter birthHomeAdapter = new BirthHomeAdapter(MainActivity.this, birthInfos);
        birthHomeRecycler.setAdapter(birthHomeAdapter);

    }

    /**
     * [显示当前页面]
     */
    private void showPanel(int show) {

        panelHome.setVisibility(View.INVISIBLE);
        panelMine.setVisibility(View.INVISIBLE);
        panelCalender.setVisibility(View.INVISIBLE);

        if (show == SHOW_HOME) {
            panelHome.setVisibility(View.VISIBLE);
            actionBarTitle.setText("生辰日");
            actionBarAdd.setVisibility(View.VISIBLE);
        } else if (show == SHOW_CALENDER) {
            panelCalender.setVisibility(View.VISIBLE);
            actionBarTitle.setText("日历");
            actionBarAdd.setVisibility(View.INVISIBLE);
        } else if (show == SHOW_MINE) {
            panelMine.setVisibility(View.VISIBLE);
            actionBarTitle.setText("设置");
            actionBarAdd.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * [初始化所有的监听器]
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        actionBarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddBirthActivity.class);
                startActivity(intent);
            }
        });
        actionBarAdd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        actionBarAdd.setTextColor(Color.GRAY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        actionBarAdd.setTextColor(Color.WHITE);
                        break;
                    case MotionEvent.ACTION_UP:
                        actionBarAdd.setTextColor(Color.WHITE);
                        break;
                }
                return false;
            }
        });
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


