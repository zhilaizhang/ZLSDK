package com.zlzhang.ble.model;

/**
 * Created by zhilaizhang on 17/6/23.
 */

public enum BLEType {
    BLE1(1, "蓝牙1代"),
    BLE2(2, "蓝牙2代");

    private int key;

    private String value;
    BLEType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
