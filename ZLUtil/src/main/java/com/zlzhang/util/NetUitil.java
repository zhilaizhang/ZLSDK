package com.zlzhang.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by zhilaizhang on 17/7/8.
 */

public class NetUitil {
    public static boolean networkIsAvailable(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        return (info != null && info.isAvailable());
    }

    public static  void ping(final String ip, final OnPingListener onPingListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                try {
                    String ping = "ping -c 1 -w 100 " + ip;
//                    String ping = "ping -i 0.2 www.baidu.com";

                    Process p = Runtime.getRuntime().exec(ping);
                    // ping的状态
                    int status = p.waitFor();
                    if (status == 0) {
                        result = "success";
                        onPingListener.onPingSucceed();
                    } else {
                        result = "failed";
                        onPingListener.onPingFailed();
                    }
                } catch (IOException e) {
                    result = "IOException";
                    onPingListener.onPingFailed();
                } catch (InterruptedException e) {
                    result = "InterruptedException";
                    onPingListener.onPingFailed();
                } finally {
                    LogHelper.d("----result---", "result = " + result);
                }
            }
        }).start();
    }

    public interface OnPingListener{
        void onPingSucceed();
        void onPingFailed();
    }
}
