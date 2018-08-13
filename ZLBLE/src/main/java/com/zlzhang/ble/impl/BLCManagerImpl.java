///*
// * ==============================BEGIN_COPYRIGHT===============================
// * =======Wangluo Electronic Technology Co., LTD. PROPRIETARY INFORMATION======
// * This software and its associated IndoorStar products are supplied under the
// * terms of a license agreement or nondisclosure agreement (NDA) with Wangluo
// * Electronic Technology Co., LTD., Shanghai and may not be copied or disclosed
// * except in accordance with the terms of that agreement.
// * Copyright (c) 2013 - 2015
// * Wangluo Electronic Technology Co., LTD., Shanghai. All Rights Reserved.
// * ===============================END_COPYRIGHT================================
// *
// * @author - IDS R&D Group - Patrick Zhang
// * @date   - 2016-04-14 11:22
// */
//
//package com.zlzhang.ble.impl;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothManager;
//import android.bluetooth.BluetoothProfile;
//import android.content.Context;
//import android.os.Handler;
//
//import com.zlzhang.ble.BLCManager;
//import com.zlzhang.ble.conn.BleConnectionCompat;
//import com.zlzhang.ble.model.BLEType;
////import com.zlzhang.ble.util.BLSUtils;
//import com.zlzhang.ble.conn.BLEConnectListener;
//import com.zlzhang.ble.conn.BLEModifyListener;
//import com.zlzhang.ble.conn.ConnectError;
//import com.zlzhang.ble.model.BLEModifyModel;
//import com.zlzhang.util.LogHelper;
//import com.zlzhang.util.TextUtil;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//import java.util.UUID;
//
//public class BLCManagerImpl extends BLCManager {
//
//    private final static String LOG_TAG = "[BLS]";
//    private final static long SERVICE_DISCOVER_TIME = 12 * 1000L;
//
//    private BluetoothAdapter mBluetoothAdapter;
//    private BluetoothManager mBluetoothManager;
//    private Handler mHandler;
//    private Context mContext;
//    private String mBluetoothDeviceAddress;
//    private String mReConnectAddress;
//    private BluetoothGatt mBluetoothGatt;
//    private BLEModifyModel mBLEModifyModel;
//    private BleConnectionCompat mBleConnectionCompat;
//
//    private BLEConnectListener mBLEConnectListener;
//    private BLEModifyListener mBLEModifyListener;
//
//    private Queue<BluetoothGattCharacteristic> characteristicReadQueue = new LinkedList<>();
//
//    private int mBleType;
//
//    BluetoothDevice device;
//    private int mConnectionState = STATE_DISCONNECTED;
//    private byte[] mDatas;
//    private int totalLength;
//    private int repeateNum;
//
//    private int mDataLength;
//    private boolean isConnected;
//
//    private boolean isRunning;
//    private int mCityId;
//    private int mMallId;
//    private int mMeasurePower;
//    private long mStarSn;
//    private long mNewStarSn;
//    private short mAdInterval;
//    private byte mTxPower;
//    private byte mRfPower;
//    private byte[] mAdMode;
//    private byte[] mAdContent;
//    private int mAdIndex;
//    private int mDataIndex;
//    private boolean isServiceDiscovered;
//
//    public BLCManagerImpl(Context context) {
//        super(context);
//        mContext = context;
//        mBleType = BLEType.BLE2.getKey();
//        init();
//    }
//
//    public BLCManagerImpl(Context context, int bleType){
//        super(context);
//        mContext = context;
//        mBleType = bleType;
//        init();
//    }
//
//    private void init(){
//        initHandler();
//        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
//        mBleConnectionCompat = new BleConnectionCompat(mContext);
//        if (mBluetoothManager != null) {
//            mBluetoothAdapter = mBluetoothManager.getAdapter();
//        }
//    }
//
//    private void initHandler() {
//        mHandler = new Handler();
//    }
//
//    @Override
//    public void connect(BLEConnectListener connectListener, String macAddress, int connectType) {
//        LogHelper.d(LOG_TAG, "test connect");
//        mBLEConnectListener = connectListener;
//        mReConnectAddress = macAddress;
//        if (mBluetoothDeviceAddress != null && macAddress.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
//            LogHelper.d(LOG_TAG, "test Re-use GATT connection");
//            if (mBluetoothGatt.connect()) {
//                mConnectionState = STATE_CONNECTING;
//                if (connectListener != null) {
//                    connectListener.onConnectionSucceed(mBluetoothDeviceAddress);
//                }
//                return;
//            }
//        }
//        device = mBluetoothAdapter.getRemoteDevice(macAddress);
//        if (device == null) {
//            LogHelper.d(LOG_TAG, "test Device not found.");
//            if (connectListener != null) {
//                connectListener.onConnectionFailed(ConnectError.Invalidmac.getCode());
//            }
//            return;
//        }
//        LogHelper.d(LOG_TAG, "test Create a new GATT connection.");
////        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
//
//        mBluetoothGatt = mBleConnectionCompat.connectGatt(device, false, mGattCallback);
//
//        mBluetoothDeviceAddress = macAddress;
//        mConnectionState = STATE_CONNECTING;
//    }
//
//    @Override
//    public void connect(BLEConnectListener bleListener, BluetoothDevice device, int connectType) {
//    }
//
//
//    private void reConnect(){
//        if (mBluetoothGatt != null) {
//            mBluetoothGatt.close();
//            mBluetoothGatt = null;
//        }
//        if (mBLEConnectListener != null && !TextUtil.isTextEmpty(mBluetoothDeviceAddress)) {
//            connect(mBLEConnectListener, mBluetoothDeviceAddress, 0);
//        }
//    }
//
//    @Override
//    public void disConnect() {
//        LogHelper.d("test", "test disConnect");
//        if (mBluetoothGatt != null) {
//            mBluetoothGatt.disconnect();
//            mBluetoothGatt.close();
//            mBluetoothGatt = null;
//            mBluetoothDeviceAddress = null;
//        }
//    }
//
//    @Override
//    public boolean isConnected(String address) {
//        return isConnected;
//    }
//
//    @Override
//    public boolean isConnected(BluetoothDevice device) {
//        return isConnected;
//    }
//
//
//    @Override
//    public void release() {
//
//    }
//
//    @Override
//    public boolean modifyDeviceConfig(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, String UUID, int major, int minor, int rfPower, int adInterval, long sn) {
//        return false;
//    }
//    ServiceDiscoverRunnable mCurrentServiceDiscoverRunnable;
//    private void discoverServices(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn) {
//        mBLEModifyListener = bleModifyListener;
//        mBLEModifyModel = bleModifyModel;
//        mStarSn = starSn;
//        if (mBluetoothGatt != null) {
//            isServiceDiscovered = false;
//            mBluetoothGatt.discoverServices();
//            if (mCurrentServiceDiscoverRunnable != null) {
//                removeServiceDiscoverRunnable();
//                mCurrentServiceDiscoverRunnable = new ServiceDiscoverRunnable();
//                mHandler.postDelayed(mCurrentServiceDiscoverRunnable, SERVICE_DISCOVER_TIME);
//            }
//
//        }
//    }
//
//    @Override
//    public boolean modifyDeviceCityAndMallId(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, int measurePower, int cityId, int mallId) {
//        mBLEModifyListener = bleModifyListener;
//        mBLEModifyModel = bleModifyModel;
//        mCityId = cityId;
//        mMallId = mallId;
//        mMeasurePower = measurePower;
//        mStarSn = starSn;
//        if (mBluetoothGatt != null) {
//            mBluetoothGatt.discoverServices();
//        }
//        return false;
//    }
//
//    @Override
//    public boolean modifyDeviceCityId(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, int cityId) {
//        mCityId = cityId;
//        discoverServices(bleModifyListener, bleModifyModel, starSn);
//        return false;
//    }
//
//    @Override
//    public boolean modifyDeviceMallId(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, int mallId) {
//        mMallId = mallId;
//        discoverServices(bleModifyListener, bleModifyModel, starSn);
//        return false;
//    }
//
//    @Override
//    public boolean modifyDeviceStarSn(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long oldStarSn, long newStarSn) {
//        mStarSn = oldStarSn;
//        mNewStarSn = newStarSn;
//        discoverServices(bleModifyListener, bleModifyModel, oldStarSn);
//        return false;
//    }
//
//    @Override
//    public boolean modifyDeviceAdInterval(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, short adInterval) {
//        mBleType = bleModifyModel.getBleType();
//        mAdInterval = adInterval;
//        discoverServices(bleModifyListener, bleModifyModel, starSn);
//        return false;
//    }
//
//    @Override
//    public boolean modifyDeviceTxPower(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, byte txPower) {
//        mTxPower = txPower;
//        discoverServices(bleModifyListener, bleModifyModel, starSn);
//        return false;
//    }
//
//    @Override
//    public boolean modifyDeviceAdMode(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, byte[] adMode) {
//        mAdMode = adMode;
//        discoverServices(bleModifyListener, bleModifyModel, starSn);
//        return false;
//    }
//
//    @Override
//    public boolean modifyDeviceMeasurePower(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, byte measurePower) {
//        mMeasurePower = measurePower;
//        discoverServices(bleModifyListener, bleModifyModel, starSn);
//        return false;
//    }
//
//    @Override
//    public boolean modifyDeviceRFPower(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, byte rfPower) {
//        mRfPower = rfPower;
//        mBleType = bleModifyModel.getBleType();
//        discoverServices(bleModifyListener, bleModifyModel, starSn);
//        return false;
//    }
//
//    @Override
//    public boolean modifyDeviceAdContent(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, int adIndex,
//                                         int dataIndex, int keyIndex, byte[] adContent) {
//        mAdContent = adContent;
//        mAdIndex = adIndex;
//        mDataIndex = dataIndex;
//        discoverServices(bleModifyListener, bleModifyModel, starSn);
//        return false;
//    }
//
//    private boolean containServices(BluetoothGattService gattService){
//        if (mBLEModifyModel != null && mBLEModifyModel.getServiceUUIDs() != null) {
//            String uuid = gattService.getUuid().toString().toLowerCase();
//            for (String s : mBLEModifyModel.getServiceUUIDs()) {
//                if (uuid.contains(s)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean containCharacs(BluetoothGattCharacteristic character){
//        if (mBLEModifyModel != null && mBLEModifyModel.getCharacteristicUUID() != null) {
//            String uuid = character.getUuid().toString().toLowerCase();
//            for (String s : mBLEModifyModel.getCharacteristicUUID()) {
//                if (uuid.contains(s)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private void displayGattServices(BluetoothGatt gatt) {
//        LogHelper.d(LOG_TAG, "test displayGattServices");
//        List<BluetoothGattService> gattServices = gatt.getServices();
//        if (gattServices == null || gattServices.size() == 0) {
//            mBLEModifyListener.onServicesDiscovered(133);
//            return;
//        }
//        for (BluetoothGattService gattService : gattServices) {
//            if (containServices(gattService)) {
//                List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = gattService.getCharacteristics();
//                if (bluetoothGattCharacteristics == null) {
//                    continue;
//                }
//                for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattCharacteristics) {
//                    String characterUUID = bluetoothGattCharacteristic.getUuid().toString();
//                    List<String> chaUUIDs = mBLEModifyModel.getCharacteristicUUID();
//                    if (chaUUIDs != null) {
//                        for (String chaUUID : chaUUIDs) {
//                            if (characterUUID.contains(chaUUID)) {
//                                LogHelper.d("test", "test add bluetoothGattCharacteristic");
//                                characteristicReadQueue.add(bluetoothGattCharacteristic);
////                                readDevice(gatt, bluetoothGattCharacteristic);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        if (characteristicReadQueue == null || characteristicReadQueue.size() == 0) {
//            mBLEModifyListener.onServicesDiscovered(-1);
//        } else {
//            modifyDevice(gatt);
//        }
//    }
//
//    private void modifyDevice(final BluetoothGatt gatt) {
//        BluetoothGattCharacteristic bluetoothGattCharacteristic = null;
//        bluetoothGattCharacteristic = characteristicReadQueue.poll();
//        LogHelper.d("test", "test bluetoothGattCharacteristic: " + bluetoothGattCharacteristic);
//        if (bluetoothGattCharacteristic == null) {
//            mBLEModifyListener.onCharacteristicWrite(-1);
//            disConnect();
//            return;
//        }
//        String chaUUID = bluetoothGattCharacteristic.getUuid().toString();
//        byte[] charaBytes = getCharaBytes(chaUUID);
//        if (charaBytes != null) {
//            bluetoothGattCharacteristic.setValue(charaBytes);
//            gatt.writeCharacteristic(bluetoothGattCharacteristic);
//        } else {
//            mBLEModifyListener.onCharacteristicWrite(-2);
//            disConnect();
//        }
//    }
//
//    private void readDevice(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
//        if (gatt != null && characteristic != null) {
//            gatt.readCharacteristic(characteristic);
//        }
//    }
//
//    private byte[] getCharaBytes(String chaUUID){
//        byte[] charaBytes = null;
//        if(mBleType == BLEType.BLE2.getKey()){
//            switch (chaUUID) {
//                case "ee0cf411-8786-40ba-ab96-99b91ac981d8":
//                    LogHelper.d("test", "test mNewStarSn");
//                    charaBytes = BLSUtils.getLongBytes(mStarSn, mNewStarSn);
//                    break;
//                case "ee0cf401-8786-40ba-ab96-99b91ac981d8":
//                    LogHelper.d("test", "test mAdInterval");
//                    charaBytes = BLSUtils.getShortBytes(mStarSn, mAdInterval);
//                    break;
//                case "ee0cf402-8786-40ba-ab96-99b91ac981d8":
//                    LogHelper.d("test", "test mTxPower");
//                    charaBytes = BLSUtils.getByteBytes(mStarSn, mTxPower);
//                    break;
//                case "ee0cf403-8786-40ba-ab96-99b91ac981d8":
//                    LogHelper.d("test", "test mAdMode");
//                    charaBytes = BLSUtils.getBytesBytes(mStarSn, mAdMode);
//                    break;
//                case "ee0cf412-8786-40ba-ab96-99b91ac981d8":
//                    LogHelper.d("test", "test mCityId");
//                    charaBytes = BLSUtils.getIntBytes(mStarSn, mCityId);
//                    break;
//                case "ee0cf413-8786-40ba-ab96-99b91ac981d8":
//                    LogHelper.d("test", "test mMallId");
//                    charaBytes = BLSUtils.getIntBytes(mStarSn, mMallId);
//                    break;
//                case "ee0cf414-8786-40ba-ab96-99b91ac981d8":
//                    LogHelper.d("test", "test mMeasurePower");
//                    charaBytes = BLSUtils.getByteBytes(mStarSn, (byte) mMeasurePower);
//                    break;
//                case "ee0cf421-8786-40ba-ab96-99b91ac981d8":
//                    LogHelper.d("test", "test AdContent");
//                    charaBytes = generateAdContentBytes();
//                    break;
//            }
//        } else if(mBleType == BLEType.BLE1.getKey()){
//            switch (chaUUID){
//                case "0000c101-0000-1000-8000-00805f9b34fb":
//                    charaBytes = BLSUtils.getAdIntervalSnByte(mStarSn, mAdInterval);
//                    break;
//                case "0000c102-0000-1000-8000-00805f9b34fb":
//                    charaBytes = BLSUtils.getRfPowerSnByte(mStarSn, mTxPower);
//                    break;
//            }
//        }
//        return charaBytes;
//    }
//
//    private byte[] generateAdContentBytes() {
//        byte[] adContentBytes = new byte[19];
//        byte[] tempBytes = BLSUtils.getBytesBytes(mStarSn, mAdContent);
//        adContentBytes[0] = (byte) mAdIndex;
//        adContentBytes[1] = (byte) mDataIndex;
//        for (int i = 0; i < tempBytes.length; i++) {
//            adContentBytes[2 + i] = tempBytes[i];
//        }
//        return adContentBytes;
//    }
//
//    private void pollCharacter() {
//        repeateNum = 0;
//        if (characteristicReadQueue.size() > 0) {
//            BluetoothGattCharacteristic bluetoothGattCharacteristic = characteristicReadQueue.poll();
//            if (bluetoothGattCharacteristic.getUuid().toString().equals("0000dcb1-0000-1000-8000-00805f9b34fb")) {
//                totalLength = (int) bluetoothGattCharacteristic.getValue()[0];
//                isRunning = true;
//                BluetoothGattCharacteristic nextCharac = characteristicReadQueue.poll();
//                mBluetoothGatt.readCharacteristic(nextCharac);
//            } else if (bluetoothGattCharacteristic != null) {
//                isRunning = true;
//                mBluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
//            }
//        }
//    }
//
//
//    BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @Override
//        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            super.onCharacteristicWrite(gatt, characteristic, status);
//            if (mBLEModifyListener != null) {
//                mBLEModifyListener.onCharacteristicWrite(status);
//            }
//            LogHelper.d(LOG_TAG, "test onCharacteristicWrite status: " + status);
//        }
//
//        @Override
//        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
//            super.onServicesDiscovered(gatt, status);
//            LogHelper.d(LOG_TAG, "test onServicesDiscovered status: " + status);
//            isServiceDiscovered = true;
//            removeServiceDiscoverRunnable();
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                displayGattServices(gatt);
//            }
//            if (mBLEModifyListener != null) {
//                mBLEModifyListener.onServicesDiscovered(status);
//            }
//        }
//
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            super.onConnectionStateChange(gatt, status, newState);
//            LogHelper.d(LOG_TAG, "test status: " + status + " newState: " + newState);
//            if (newState == BluetoothProfile.STATE_CONNECTED && status == 0) {
//                isConnected = true;
//                if (mBLEConnectListener != null) {
//                    mBLEConnectListener.onConnectionSucceed(mBluetoothDeviceAddress);
//                }
//            } else {
//                isRunning = false;
//                isConnected = false;
//                mBLEConnectListener.onConnectionFailed(newState);
////                reConnect();
//            }
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            super.onCharacteristicRead(gatt, characteristic, status);
//            LogHelper.d(LOG_TAG, "test onCharacteristicRead status: " + status);
//            LogHelper.d(LOG_TAG, "test onCharacteristicRead uuid: " + characteristic.getUuid());
//            if (characteristic.getUuid().toString().equals("0000dcb2-0000-1000-8000-00805f9b34fb")) {  //初始化数组
//                mDatas = new byte[totalLength];
//                mDataLength = 0;
//            }
//            if (!characteristic.getUuid().toString().equals("0000dcb1-0000-1000-8000-00805f9b34fb")) {
//                for (byte b : characteristic.getValue()) {
//                    LogHelper.d(LOG_TAG, "tst read: " + b);
//                    if (mDataLength < totalLength) {
//                        mDatas[mDataLength++] = b;
//                    }
//                    if (mDataLength == totalLength) {
//                        mDataLength = 0;
//                        isRunning = false;
//                        break;
//                    }
//                }
//            }
////            pollCharacter();
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            super.onCharacteristicChanged(gatt, characteristic);
//            LogHelper.d(LOG_TAG, "test onCharacteristicChanged: uuid: " + characteristic.getUuid().toString());
//            if (characteristic != null) {
//                if (characteristic.getService() != null) {
//                    List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = characteristic.getService().getCharacteristics();
//                    if (bluetoothGattCharacteristics != null) {
//                        int number = 0;
//                        if (bluetoothGattCharacteristics.get(0) != null && bluetoothGattCharacteristics.get(0).getValue() != null) {
//                            number = bluetoothGattCharacteristics.get(0).getValue()[0];
//                            number = (int) Math.ceil(number * 1.0 / 20);
//                        }
//                        for (int i = 0; i < number + 1; i++) {
//                            characteristicReadQueue.add(bluetoothGattCharacteristics.get(i));
//                        }
//                    }
//                }
//            }
//
//            //注意:由于解析数据时,会导致isRunning一直处于true的状态,导致pollCharacter()不能被执行
//            //所以这里加这段代码来防止不能进入pollCharacter()
//            if (isRunning && characteristic.getUuid().toString().equals("0000dcb1-0000-1000-8000-00805f9b34fb")) {
//                repeateNum++;
//            }
//            if (repeateNum >= 3 && characteristicReadQueue.size() > 0) {
//                isRunning = false;
//            }
//            //注意
//
//            if (characteristicReadQueue.size() > 0 && !isRunning) {
////                pollCharacter();
//            }
//        }
//
//
//        @Override
//        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//            super.onDescriptorRead(gatt, descriptor, status);
//            LogHelper.d(LOG_TAG, "test onDescriptorRead: ");
//        }
//
//        @Override
//        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//            super.onDescriptorWrite(gatt, descriptor, status);
//            LogHelper.d(LOG_TAG, "test onDescriptorWrite: ");
//        }
//    };
//
//    /**
//     * 注册通知，为了读取数据
//     * @param gatt
//     */
//    private void registerNotification(BluetoothGatt gatt) {
//        LogHelper.d(LOG_TAG, "test registerNotification");
//        List<BluetoothGattService> gattServices = gatt.getServices();
//        for (BluetoothGattService gattService : gattServices) {
//            if (containServices(gattService)) {
//                List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = gattService.getCharacteristics();
//                for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattCharacteristics) {
//                    if (containCharacs(bluetoothGattCharacteristic)) {
//                        LogHelper.d(LOG_TAG, "test Characteristicuuid: " + bluetoothGattCharacteristic.getUuid());
//                        boolean b = gatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
//                        if (bluetoothGattCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG)) != null) {
//                            BluetoothGattDescriptor desc = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
//                            desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                            if (!gatt.writeDescriptor(desc)) {
//                                LogHelper.d(LOG_TAG, "Couldn't write RX client descriptor value!");
//                            }
//                        } else {
//                            LogHelper.d(LOG_TAG, "Couldn't get RX client descriptor!");
//                        }
//                    }
//                }
//            }
//
//        }
//    }
//
//    private void removeServiceDiscoverRunnable(){
//        if (mCurrentServiceDiscoverRunnable != null && mHandler != null) {
//            mHandler.removeCallbacks(mCurrentServiceDiscoverRunnable);
//            mCurrentServiceDiscoverRunnable = null;
//        }
//    }
//
//    class ServiceDiscoverRunnable implements Runnable{
//
//        @Override
//        public void run() {
//            if (!isServiceDiscovered) {
//                mBLEModifyListener.onServicesDiscovered(-1);
//            }
//        }
//    }
//}
