package com.cdtc.birthday;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.cdtc.birthday.utils.LogUtil;
import com.coolerfall.daemon.Daemon;

import java.util.Calendar;
import java.util.TimeZone;

public class LongRunningService extends Service {
    private int month,day,hour,minuete,seconds;

    public LongRunningService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("ALARMSERVICE", "onCreate()");
        Daemon.run(LongRunningService.this,LongRunningService.class
                ,Daemon.INTERVAL_ONE_MINUTE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setAlarm();
        return START_STICKY;
    }

    /** 设置Alarm */
    private void setAlarm(){
        LogUtil.d("ALARMSERVICE", "onStartCommand()");
        month=8;
        day=28;
        hour=23;
        minuete=50;
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minuete);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long systemTime=System.currentTimeMillis();
        long selectTime=calendar.getTimeInMillis();
        long time=selectTime-systemTime;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this, GuardReceiver.class);
        i.setAction("com.cdtc.birthday.LongRunningService.OVER_TIME");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        try{
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
            }else{
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy() {
        LogUtil.d("ALARMSERVICE", "onDestroy()");
        startService(new Intent(this,LongRunningService.class));
        super.onDestroy();
    }
}
