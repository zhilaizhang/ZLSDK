package com.zlzhang.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhilaizhang on 17/7/8.
 */

public abstract class TimerUtil {
    private Timer mTimer;
    private TimerTask mTimerTask;
    private TimerUtil mTimerUtil;
    private long mDelayTime;
    private long mPeriodTime;

    public TimerUtil(long delayTime, long periodTime){
        mDelayTime = delayTime;
        mPeriodTime = periodTime;
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                doTask();
            }
        };
    }

    public void execute(){
        mTimer.schedule(mTimerTask, mDelayTime, mPeriodTime);
    }

    public void stop(){
        mTimer.cancel();
        mTimer.purge();
        mTimer = null;
        mTimerTask = null;
    }

    public abstract void doTask();
}
