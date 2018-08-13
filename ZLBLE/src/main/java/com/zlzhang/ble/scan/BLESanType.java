
package com.zlzhang.ble.scan;

/**
 * Created by zhilaizhang on 11/13/15.
 */
public enum BLESanType {
    GENERAL_SCAN(1, "GENERAL_SCAN"),
    PARKING_SCAN(2, "PARKING_SCAN"),
    SDK_SCAN(3, "SDK_SCAN"),
    INTERPHON_SCAN(4, "INTERPHON_SCAN");

    private int    value;
    private String name;

    BLESanType(int value, String scanType) {
        this.value = value;
        this.name = scanType;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
