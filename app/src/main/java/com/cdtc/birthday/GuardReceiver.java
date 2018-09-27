package com.cdtc.birthday;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cdtc.birthday.utils.LogUtil;

public class GuardReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        if (action.equals("android.intent.action.USER_PRESENT")) {
            LogUtil.d("GUARDRECEIVER", "android.intent.action.USER_PRESENT");
            if (LockScreenActivity.sLockScreenActivity!=null){
                LockScreenActivity.sLockScreenActivity.finish();
            }
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            LogUtil.d("GUARDRECEIVER", "android.intent.action.BOOT_COMPLETED");
//            Intent mIntent = new Intent(context, MainActivity.class);
//            context.startActivity(mIntent);
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
        }
    }
}
