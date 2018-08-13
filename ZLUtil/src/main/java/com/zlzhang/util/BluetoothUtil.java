package com.zlzhang.util;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by zhilaizhang on 17/7/8.
 * get bluetooth status and enable bluetooth
 */

public class BluetoothUtil {
    public static boolean bluetoothIsAvailable(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            return false;
        }
        return adapter.isEnabled();
    }

    public static boolean enableBluetooth(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            return false;
        }
        return adapter.enable();
    }
}
