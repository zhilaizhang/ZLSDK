package com.zlzhang.serialport;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


import com.zlzhang.serialport.listener.OnSerialPortListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhilaizhang on 17/3/20.
 */

public class ZLSerialPort {

    private static final String TAG = "ZLSerialPort";
    private final static int BUFFER_LENGTH = 256;
    private final static int PACKAGE_SUCCEED = 1;

    private static ZLSerialPort mIdsSerialPort;
    private SerialPort mSerialPort;
    private OnSerialPortListener mOnSerialPortListener;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private boolean isPause;
    private DataPackage mDataPackage;
    private boolean isDeviceOpened;

    private Handler mHandler;

    public static ZLSerialPort getInstance() {
        if (mIdsSerialPort == null) {
            mIdsSerialPort = new ZLSerialPort();
        }
        return mIdsSerialPort;
    }

    public ZLSerialPort() {
        isDeviceOpened = false;
        initHandler();
        mDataPackage = new DataPackage(new DataPackage.OnPackageDataListener() {
            @Override
            public void onPackageSucceed(final byte[] address, final byte[] content, final int length) {
                mAddressBytes = address;
                mContentBytes = content;
                mContentLength = length;
                Message message = new Message();
                message.what = PACKAGE_SUCCEED;
                mHandler.sendMessage(message);
            }

            @Override
            public void onPackageFailed() {

            }
        });
    }

    private byte[] mAddressBytes;
    private byte[] mContentBytes;

    private int mContentLength;


    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PACKAGE_SUCCEED:
//                        LogHelper.d(TAG, "test thread1 : " + Thread.currentThread().getId());
//                        if (crcContent(mContentBytes, mContentLength - 1)) {  //进行crc校验
                            mOnSerialPortListener.onRead(mAddressBytes, mContentBytes, mContentLength);
//                        }
                        break;
                }
            }
        };
    }

    public void setOnSerialPortListener(OnSerialPortListener onSerialPortListener) {
        mOnSerialPortListener = onSerialPortListener;
    }

    public void setAddress(byte address) {
//        mCurrentAddress = address;
    }

    public void isServer(boolean isServer) {
        mDataPackage.isServer(isServer);
    }

    private boolean crcContent(byte[] content, int length) {
//        int crc = CheckoutUtil.crc8(content, 0, length);
//        Log.d(TAG, "test crc: " + crc);
//        if (crc == content[length]) {
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    public void openDevice(Context context, String path) {
        try {
            FileOutputStream fo = new FileOutputStream(path);
            fo.write("1".getBytes());
            fo.close();
            isDeviceOpened = true;
        } catch (IOException e) {
            isDeviceOpened = false;
            if (mOnSerialPortListener != null) {
                mOnSerialPortListener.onOpened(SerialPortStatus.OPEN_DEVICE_ERROR);
            }
        }
    }

    public void closeDevice(Context context, String path) {
        try {
            FileOutputStream fo = new FileOutputStream(path);
            fo.write("0".getBytes());
            fo.close();
        } catch (IOException e) {
//            LogHelper.e("test", "closeDevice failed");
        }
    }


    public void openSerialPort(File file, int baudrate, int flags) {
        boolean isOpenSucceed;
        if (isDeviceOpened) {
            try {
                mSerialPort = new SerialPort(file, baudrate, flags);
                isOpenSucceed = true;
            } catch (IOException e) {
                e.printStackTrace();
                isOpenSucceed = false;
            }
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
            final int status;
            if (isOpenSucceed) {
                status = SerialPortStatus.OPEN_OK;
            } else {
                status = SerialPortStatus.OPEN_SERIALPORT_ERROR;
            }
            mOnSerialPortListener.onOpened(status);
        }
    }

    public void readData() {
        if (mSerialPort != null) {
            mReadThread = new ReadThread();
            mReadThread.start();
            isPause = false;
        }
    }

    public void writeData(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            if (mSerialPort != null) {
                try {
                    if (mOutputStream != null) {
                        if (mOnSerialPortListener != null) {
                            mOnSerialPortListener.onWrite(bytes);
                        }
                        byte[] buffer = new byte[BUFFER_LENGTH + 3];
                        System.arraycopy(bytes, 0, buffer, 0, 3);
                        int length = bytes.length - 3;
                        int index = 3;
                        while (length >= BUFFER_LENGTH) {
                            System.arraycopy(bytes, index, buffer, 3, BUFFER_LENGTH);
                            printData(buffer);
                            mOutputStream.write(buffer);
                            length -= BUFFER_LENGTH;
                            index += BUFFER_LENGTH;
                        }
                        if (length > 0) {
                            buffer = new byte[length + 3];
                            System.arraycopy(bytes, 0, buffer, 0, 3);
                            System.arraycopy(bytes, index, buffer, 3, length);
                            printData(buffer);
                            mOutputStream.write(buffer);
                        }
                        mOutputStream.flush();
                    } else {
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private void printData(byte[] bytes) {
        String ss = "";
        for (int i = 0; i < bytes.length; i++) {
            ss += bytes[i] + ",";
        }
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
            mOnSerialPortListener.onClosed();
        }
        isPause = true;
        if (mReadThread != null) {
            mReadThread = null;
        }
    }

    public void onPauseRead() {
        isPause = true;
    }

    public void onResumeRead() {
        isPause = false;
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted() && !isPause) {
                int size;
                try {
                    byte[] buffer = new byte[256];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        mDataPackage.setData(buffer, size);
                        String ss = "";
                        for (int i = 0; i < size; i++) {
                            ss += buffer[i] + ",";
                        }
                    }
                    if (mOnSerialPortListener != null) {
                        mOnSerialPortListener.onReadRaw(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

}
