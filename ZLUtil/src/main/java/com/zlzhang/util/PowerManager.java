package com.zlzhang.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by zhilaizhang on 17/7/8.
 */

public class PowerManager {

    private android.os.PowerManager powerManager;
    private android.os.PowerManager.WakeLock wakeLock;
    private Context context;

    public PowerManager(Context context){
        this.context = context;
    }

    public void startLight(){
        initLight(context);
    }

    public void stopLight(){
        context.unregisterReceiver(mBatInfoReceiver);
    }

    private void initLight(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(mBatInfoReceiver, filter);
        powerManager = (android.os.PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            LogHelper.d("test", "test onReceive");
            wakeLock = powerManager.newWakeLock(android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP | android.os.PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                    "bright");
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // 点亮屏幕
                wakeLock.acquire();
            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                wakeLock.acquire();
            }
        }
    };
}
