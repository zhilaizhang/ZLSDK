package com.zlzhang.ble.conn;

import android.bluetooth.BluetoothDevice;

/**
 * Created by zhilaizhang on 17/2/16.
 */

public interface IBLEConnector {

    void connect(BLEConnectListener bleListener, String macAddress, int connectType);

    void connect(BLEConnectListener bleListener, BluetoothDevice device, int connectType);

    void disConnect();

    boolean isConnected(String address);

    boolean isConnected(BluetoothDevice device);

    void release();
}
