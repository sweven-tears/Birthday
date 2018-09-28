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
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cdtc.birthday.data.BirthBean;
import com.cdtc.birthday.data.BornDay;
import com.cdtc.birthday.utils.LogUtil;
import com.cdtc.birthday.utils.ToastUtil;
import com.cdtc.birthday.view.BirthDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout panelCalender;
    private LinearLayout panelHome;
    private ConstraintLayout panelMine;

    private static final int SHOW_HOME = 1, SHOW_CALENDER = 2, SHOW_MINE = 3;

    private RecyclerView birthHomeRecycler;

    private TextView actionBarTitle, actionBarAdd;
    private LinearLayoutManager layoutManager;

    private TextView mRemarkTextView;
    private CustomCalendarView customCalendarView;

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
    private BirthHomeAdapter birthHomeAdapter;
    private static int presentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置字体大小不随系统字体大小的改变而改变
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

        customCalendarView = findViewById(R.id.calendar);
        mRemarkTextView = findViewById(R.id.remark_text_view);
    }

    /**
     * [初始化数据]
     */
    private void initData() {
        navigation.setSelectedItemId(R.id.navigation_home);
        showPanel(SHOW_HOME);

        ArrayList<BirthBean> birthBeans = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            birthBeans.add(new BirthBean("李刚", new BornDay(1999, 9, 23), false ,false,new int[]{0,0}));
            birthBeans.add(new BirthBean("东方红叶", new BornDay(2009, 9, 30), false,true,new int[]{0,0}));
            birthBeans.add(new BirthBean("钟晓珊", new BornDay(2001, 10, 3), false,true,new int[]{0,0}));
            birthBeans.add(new BirthBean("Peter", new BornDay(2010, 11, 23), false,false,new int[]{0,0}));
            birthBeans.add(new BirthBean("Mr.Li", new BornDay(2004, 12, 26), true,true,new int[]{0,0}));
            birthBeans.add(new BirthBean("Sweven Tears", new BornDay(2008, 1, 23), true,false,new int[]{0,0}));
            birthBeans.add(new BirthBean("小落", new BornDay(2015, 3, 23), true,false,new int[]{0,0}));
            birthBeans.add(new BirthBean("小乔", new BornDay(2022, 6, 23), false,false,new int[]{0,0}));
        }

        layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        birthHomeRecycler.setLayoutManager(layoutManager);
        new PagerSnapHelper().attachToRecyclerView(birthHomeRecycler);
        birthHomeRecycler.addItemDecoration(new SpaceItemDecoration(70));

        initBirthCard(birthBeans);

        // 设置标注日期
        List<Date> markDates = new ArrayList<>();
        markDates.add(new Date());
        customCalendarView.setMarkDates(markDates);

    }

    /**
     * 初始化生日卡片
     */
    private void initBirthCard(List<BirthBean> birthBean) {
        if (birthBean.size() < 1) {
            Calendar cal = Calendar.getInstance();
            BornDay now = new BornDay(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1), cal.get(Calendar.DATE));
            birthBean.add(new BirthBean("无记录", now));
        }
        layoutManager.scrollToPositionWithOffset(1, 0);
        birthHomeAdapter = new BirthHomeAdapter(MainActivity.this, birthBean);
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
            actionBarTitle.setText("我的");
            actionBarAdd.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * [初始化所有的监听器]
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        // 日历监听事件
        customCalendarView.setOnCalendarViewListener((view, date) -> {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
            ToastUtil.showShort(MainActivity.this, format.format(date));
        });

        // home页面右上角添加生日信息监听
        actionBarAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddBirthActivity.class);
            startActivityForResult(intent, AddBirthActivity.REQUEST);
        });

        // home页面右上角图标的变化
        actionBarAdd.setOnTouchListener((view, motionEvent) -> {
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
        });

        // 启动生日详情界面
        birthHomeAdapter.setBirthCardListener((intent,position) -> {
            startActivityForResult(intent, BirthDetailActivity.REQUEST);
            presentPosition=position;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddBirthActivity.REQUEST && resultCode == AddBirthActivity.RESULT) {
            assert data != null;
            String name = data.getStringExtra("name");
            int[] nextBirth = data.getIntArrayExtra("nextBirth");
            int age = data.getIntExtra("age", 0);
            boolean isLockScreen = data.getBooleanExtra("isLockScreen", true);
            int[] clockTime = data.getIntArrayExtra("clockTime");
            BirthBean bean = new BirthBean();
            bean.setName(name);
            BornDay born = BornDay.getSolarBornDay(nextBirth[0], nextBirth[1], nextBirth[2], age);
            bean.setBirthday(born);
            bean.setLockScreen(isLockScreen);
            bean.setClockTime(clockTime);
            insertBirthCard(bean);
        } else if (requestCode == BirthDetailActivity.REQUEST && resultCode == BirthDetailActivity.RESULT) {
            assert data != null;
            Bundle bundle = data.getBundleExtra("allMessage");
            String name = bundle.getString("name");
            int[] birthday = bundle.getIntArray("birthday");
            int[] nextBirth = bundle.getIntArray("nextBirth");
            int age = bundle.getInt("age");
            int[] clockTime = bundle.getIntArray("clockTime");
            boolean isLunarBirth = bundle.getBoolean("isLunarBirth");
            boolean isLockScreen = bundle.getBoolean("isLockScreen");

            assert birthday!=null;
            BornDay day=new BornDay(birthday[0],birthday[1],birthday[2]);
            BirthBean bean = new BirthBean(name,day,isLunarBirth,isLockScreen,clockTime);
            updateBirthCard(bean);
        }
    }

    private void updateBirthCard(BirthBean bean) {
        birthHomeAdapter.updateItem(bean,presentPosition);
    }

    private void insertBirthCard(BirthBean bean) {
        birthHomeAdapter.insertItem(bean);
    }
}


