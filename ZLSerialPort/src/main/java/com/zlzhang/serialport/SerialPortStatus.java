package com.zlzhang.serialport;

/**
 * Created by zhilaizhang on 17/3/20.
 */

public class SerialPortStatus {

    public static final int OPEN_OK = 0x00;
    public static final int OPEN_DEVICE_ERROR = 0x01;
    public static final int OPEN_SERIALPORT_ERROR = 0x01;

    public static final int CLOSE_OK = 0x0;
    public static final int CLOSE_ERROR = 0x03;

}
