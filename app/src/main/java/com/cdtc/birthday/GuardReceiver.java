package com.cdtc.birthday;

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
            LogUtil.d("GUARDRECEIVER","android.intent.action.USER_PRESENT");
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            LogUtil.d("GUARDRECEIVER","android.intent.action.BOOT_COMPLETED");
//            Intent mIntent = new Intent(context, MainActivity.class);
//            context.startActivity(mIntent);
            Intent sIntent = new Intent(context, LongRunningService.class);
            context.startService(sIntent);
        }else if (Intent.ACTION_SCREEN_OFF.equals(action)){
            LogUtil.d("Log+","ScreenOffReceiver");
            Intent lockScreen=new Intent(context,LockScreenActivity.class);
            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    |Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(lockScreen);
        }
    }
}
