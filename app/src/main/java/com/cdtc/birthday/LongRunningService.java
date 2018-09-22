package com.cdtc.birthday;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class LongRunningService extends Service {
    public LongRunningService() {
    }

    @Override
    public void onCreate() {
        LogUtil.d("ALARMSERVICE", "onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("ALARMSERVICE", "onStartCommand()");
        startForeground(1, new Notification());

        new Thread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(LongRunningService.this, "常驻后台测试", Toast.LENGTH_SHORT).show();
            }
        }).start();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int fiveSeconds = 5 * 1000;  //5秒的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + fiveSeconds;
        Intent i = new Intent(this, LongRunningService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        try{
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
            }else{
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return START_STICKY;
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
