package com.zlzhang.serialport;

import android.util.Log;


import com.zlzhang.serialport.state.DataPackageContext;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhilaizhang on 17/4/11.
 */

public class DataPackage {

    private static final String TAG = "DataPackage";

    private OnPackageDataListener mOnPackageDataListener;
    private boolean isServer;
    private ConcurrentLinkedQueue mPackageQueue;
    private DataPackageContext mDataPackageContext;
    private boolean isSetData = false;


    public DataPackage(OnPackageDataListener onPackageDataListener){
        mOnPackageDataListener = onPackageDataListener;
        mPackageQueue = new ConcurrentLinkedQueue();
        mDataPackageContext = new DataPackageContext(new DataPackageContext.OnDataPackageListener() {
            @Override
            public void onPackageSucceed(byte[] address, byte[] contents, int length) {
                mOnPackageDataListener.onPackageSucceed(address, contents, length);
            }
        });
    }

    public void isServer(boolean isServer){
        this.isServer = isServer;
    }

    public void setData(byte[] data, int size){
        if (!isSetData) {
            isSetData = true;
            pollData();
        }
        for (int i = 0; i < size; i++) {
            mPackageQueue.add(data[i]);
        }
    }

    private void pollData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!mPackageQueue.isEmpty()) {
                        byte data = (byte) mPackageQueue.poll();
                        setData(data);
                    }
                }
            }
        }).start();
    }

    public void setData(byte data){
        Log.d(TAG, "test data: " + data);
        mDataPackageContext.setData(data);
    }

    public void onDestroy(){
        mOnPackageDataListener = null;
    }

    public interface OnPackageDataListener{
        void onPackageSucceed(byte[] address, byte[] content, int length);
        void onPackageFailed();
    }
}
