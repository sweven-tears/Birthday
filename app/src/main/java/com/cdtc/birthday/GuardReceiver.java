package com.cdtc.birthday;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.cdtc.birthday.utils.LogUtil;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class GuardReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        if (action.equals("android.intent.action.USER_PRESENT")) {
            LogUtil.d("GUARDRECEIVER", "android.intent.action.USER_PRESENT");
            if (LockScreenActivity.sLockScreenActivity != null) {
                LockScreenActivity.sLockScreenActivity.finish();
            }
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            LogUtil.d("GUARDRECEIVER", "android.intent.action.BOOT_COMPLETED");
            Intent sIntent = new Intent(context, LongRunningService.class);
            context.startService(sIntent);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            Intent lockScreen = new Intent(context, LockScreenActivity.class);
            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            String isTop = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            if (!isTop.equals("com.cdtc.birthday.LockScreenActivity")) {
                LogUtil.d("GUARDRECEIVER", isTop);
                context.startActivity(lockScreen);
            }
        } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
            LogUtil.d("GUARDRECEIVER", "ScreenOnReceiver");
        } else if (action.equals("com.cdtc.birthday.LongRunningService.OVER_TIME")) {
            LogUtil.d("GUARDRECEIVER", "Big Dad`s birthday !");
            Intent notifyIntent = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, notifyIntent, 0);
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle("爸爸的生日到了！")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("赶快打个电话祝福一下吧！"))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0, 800, 500, 800})
                    .setLights(Color.GREEN, 1000, 1000)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build();
            manager.notify(1, notification);
        }
    }
}
