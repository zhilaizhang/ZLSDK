

package com.zlzhang.ble.conn;

/**
 * Created by zhilaizhang on 11/11/15.
 */
public interface BLEConnectListener {
    String TAG = "BLEConnectListener";

    void onConnectionSucceed(String macAddress);

    void onConnectionFailed(int error);

}
