package com.zlzhang.serialport.listener;

/**
 * Created by zhilaizhang on 17/4/12.
 */

public interface OnSerialPortListener {

    void onOpened(int status);

    void onClosed();

    void onRead(byte[] address, byte[] content, int size);

    void onReadRaw(byte[] rawData, int size);

    void onWrite(byte[] content);
}
