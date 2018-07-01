package com.zlzhang.serialport;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhilaizhang on 17/3/16.
 */

public class SerialPort {
    private static final String TAG = "SerialPort";

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;//文件描述
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    /**
     *获得一个窗口
     * @param device 设备
     * @param baudrate 波特率
     * @param flags 标志
     * @throws SecurityException
     * @throws IOException
     */
    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {

        /* Check access permission */ //检查权限
        if (!device.canRead() || !device.canWrite()) {
            try {
                //如果丢失权限，就再获取权限
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
                //写命令
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                LogHelper.d(TAG, "test e: " + e.getMessage());
                throw new SecurityException();
            }
        }
        //打开设备，这里面调用jni 的open方法
        mFd = open(device.getAbsolutePath(), baudrate, flags);
        if (mFd == null) {
//            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }


    // Getters and setters
    public FileInputStream getInputStream() {
        return mFileInputStream;
    }

    public FileOutputStream getOutputStream() {
        return mFileOutputStream;
    }

    //============== JNI=========================================
    /**
     * 打开串口设备的方法
     * @param path 设备的绝对路径
     * @param baudrate 波特率
     * @param flags 标志
     * @return
     */
    private native static FileDescriptor open(String path, int baudrate, int flags);
//    //关闭设备
    public native void close();
    //加载库文件
    static {
        System.loadLibrary("zlSerialPort");
    }
}
