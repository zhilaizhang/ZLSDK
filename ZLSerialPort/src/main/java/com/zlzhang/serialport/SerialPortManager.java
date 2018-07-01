package com.zlzhang.serialport;

import android.content.Context;
import android.util.Log;


import com.zlzhang.serialport.listener.OnSerialPortListener;
import com.zlzhang.serialport.util.SerialPortUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zhilaizhang on 17/4/12.
 */

public class SerialPortManager {
    private static final String OPEN_SACN_BATTERY = "sys/class/misc/mtgpio/scanpower";
    private static SerialPortManager mSerialPortManager;
    private static final long POLL_DELAY = 0;
    private Context mContext;

    private int mTotalDeviceNum = 3;
    private Timer mPollTimer;
    private TimerTask mPollTimerTask;
    private ZLSerialPort mZLSerialPort;
    private int mCurrentId;
    private long mPollPeriod = 500L;
    private boolean isPolling = false;
    private byte[] mMagicNum;
    private byte mChannel;
    private byte mSentAddress;
    private boolean[] mReiveAddressArray;

    private Map<Integer, Byte> mDeviceAlertMap;

    public static SerialPortManager getInstance(Context context) {
        if (mSerialPortManager == null) {
            mSerialPortManager = new SerialPortManager(context);
        }
        return mSerialPortManager;
    }

    public SerialPortManager(Context context) {
        mContext = context;
        mDeviceAlertMap = new HashMap<>();
        mCurrentId = 2;
        mTotalDeviceNum = 3;
        mChannel = SerialPortUtil.CHANNEL;
        mSentAddress = 1;
        mMagicNum = new byte[]{(byte) 0xC1, (byte) 0xD2, (byte) 0xE3};
        mZLSerialPort = ZLSerialPort.getInstance();
        mReceiveAddressList = new CopyOnWriteArrayList<>();
    }

    /**
     * 配置串口
     */
    public void configSerialPort(OnSerialPortListener onSerialPortListener) {
        mZLSerialPort.setOnSerialPortListener(onSerialPortListener);
        mZLSerialPort.openDevice(mContext, OPEN_SACN_BATTERY);
        mZLSerialPort.openSerialPort(new File("/dev/ttyMT1"), 115200, 0);
    }

    public void setSendPeriodTime(int periodTime){
        mPollPeriod = periodTime;
        if (isPolling) {
            startPoll();
        }
    }

    public void writeData(byte[] datas){
        mZLSerialPort.onPauseRead();
        mZLSerialPort.writeData(datas);
        mZLSerialPort.onResumeRead();
    }

    /**
     * 开启接收数据
     */
    public void startReceiveData(){
        mZLSerialPort.readData();
    }


    public void isServer(boolean isServer){
        mZLSerialPort.isServer(isServer);
    }

    /**
     * 开始轮询请求
     */
    public void startPoll() {
//        doPoll();
        stopPoll();
        isPolling = true;
        if (mPollTimer == null) {
            mPollTimer = new Timer();
            mPollTimerTask = new TimerTask() {
                @Override
                public void run() {
                    doPoll();
                }
            };
            mPollTimer.schedule(mPollTimerTask, POLL_DELAY, mPollPeriod);
        }
    }

    /**
     * 循环请求设备应答，间隔
     */
    private void doPoll() {
//        mCurrentId = 4;
        mCurrentId = getReceiveAddress();
        Log.d("test", "test mCurrentId: " + mCurrentId);
        if(mCurrentId == -1){
            return;
        }
        byte[] contents = new byte[11];
        contents[0] = 0x00;
        contents[1] = (byte) (mCurrentId);      //receive address
        contents[2] = mChannel;                 //receive CHANNEL
        contents[3] = mMagicNum[0];
        contents[4] = mMagicNum[1];
        contents[5] = mMagicNum[2];
        contents[6] = 0x08;
        contents[7] = 0x00;
        contents[8] = mSentAddress;
        contents[9] = 0;
        if (mDeviceAlertMap.get(mCurrentId) != null) {
            contents[9] = mDeviceAlertMap.get(mCurrentId);
        }
        int crc = 0x0D;
        contents[10] = (byte) crc;
        mZLSerialPort.writeData(contents);
        Log.d("test", "test mCurrentId: " + mCurrentId);
    }

    private  int index = 0;

//    private int getReceiveAddress(){
//        if(index >= mTotalDeviceNum){
//            index = 0;
//        }
//        if (mTotalDeviceNum == 0 || mCurrentAddressList == null) {
//            return -1;
//        }
//       return mCurrentAddressList.get(index++);
//    }

    private int getReceiveAddress(){
        if(index >= mTotalDeviceNum){
            index = 0;
        }
        if (mTotalDeviceNum == 0 || mReceiveAddressList.size() == 0) {
            return -1;
        }
        return mReceiveAddressList.get(index++);
    }


    /**
     * channel为16进制
     * @param channel
     */
    public void setChannel(byte channel){
        mChannel = channel;
        SerialPortUtil.CHANNEL = channel;
    }

    public void setSentAddress(byte sentAddress){
        mSentAddress = sentAddress;
        mZLSerialPort.setAddress(sentAddress);
    }

    private List<Byte> mCurrentAddressList;
    public void setReceiveAddress(boolean[] receiveAddress){
        mCurrentAddressList = new ArrayList<>();
        int num = 0;
        for (int i = 0; i < receiveAddress.length; i++) {
            if (receiveAddress[i]) {
                mCurrentAddressList.add((byte) (i + 1));
                num++;
            }
        }
        mTotalDeviceNum = num;
    }

    private List<Integer> mReceiveAddressList;
    public void setReceiveAddress(int address){
        if (!mReceiveAddressList.contains(address)) {
            mReceiveAddressList.add(address);
        }
        mTotalDeviceNum = mReceiveAddressList.size();
    }

    public void setReceiveAddress(List<Integer> addressList){
        if (addressList == null || addressList.size() == 0) {
            return;
        }
        for (Integer address : addressList) {
            setReceiveAddress(address);
        }
    }

    public void removeReceiveAddress(int address){
        for (int i = 0; i < mReceiveAddressList.size(); i++) {
            if (address == mReceiveAddressList.get(i)) {
                mReceiveAddressList.remove(i);
            }
        }
        mTotalDeviceNum = mReceiveAddressList.size();
    }

    public void removeReceiveAddress(List<Integer> addressList){
        if (addressList == null || addressList.size() == 0) {
            return;
        }
        for (Integer address : addressList) {
            removeReceiveAddress(address);
        }
    }

    /**
     * 在startPoll之前调用
     * @param magicNum
     */
    public void setMagicNum(byte[] magicNum){
        mMagicNum = magicNum;
        SerialPortUtil.MAGIC_NUM1 = magicNum[0];
        SerialPortUtil.MAGIC_NUM2 = magicNum[1];
        SerialPortUtil.MAGIC_NUM3 = magicNum[2];
    }

    /**
     * 停止轮询
     */
    public void stopPoll() {
        if (mPollTimer != null) {
            isPolling = false;
            mPollTimer.cancel();
            mPollTimer = null;
            mPollTimerTask = null;
        }
    }

    /**
     * 设置设备的报警状态
     * @param id
     * @param alert
     */
    public void letDeviceAlert(int id, byte alert) {
        mDeviceAlertMap.put(id, alert);
    }

    public void closeSerialPort(){
        mZLSerialPort.closeSerialPort();
        mZLSerialPort.closeDevice(mContext, OPEN_SACN_BATTERY);
        stopPoll();
    }
}
