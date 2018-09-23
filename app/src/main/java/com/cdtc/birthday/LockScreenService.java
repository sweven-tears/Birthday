package com.cdtc.birthday;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockScreenService extends Service {
    private GuardReceiver mGuardReceiver;

    public LockScreenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("LOCKSCREENSERVICE","onCreate()");
        mGuardReceiver=new GuardReceiver();
        IntentFilter screenOffFilter=new IntentFilter();
        screenOffFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mGuardReceiver,screenOffFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGuardReceiver);
        startService(new Intent(this,LockScreenService.class));
    }
}
