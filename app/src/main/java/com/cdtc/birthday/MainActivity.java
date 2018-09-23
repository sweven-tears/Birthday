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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout panelCalender;
    private LinearLayout panelHome;
    private LinearLayout panelMine;

    private static final int SHOW_HOME = 1, SHOW_CALENDER = 2, SHOW_MINE = 3;


    private RecyclerView birthHomeRecycler;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
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


