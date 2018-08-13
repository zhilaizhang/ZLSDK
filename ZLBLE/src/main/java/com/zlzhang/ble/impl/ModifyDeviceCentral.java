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
// * @date   - 2016-05-25 22:53
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
//import android.util.Log;
//
//import com.ids.bls.BLECentral;
//import com.zlzhang.ble.conn.BLEModifyListener;
//import com.zlzhang.ble.conn.BLEConnectListener;
//import com.zlzhang.ble.conn.ConnectError;
//import com.ids.smt.cmn.SmtManagerState;
//import com.ids.util.android.LogHelper;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//import java.util.UUID;
//
///**
// * Created by zhilaizhang on 5/25/16.
// */
//public class ModifyDeviceCentral implements BLECentral {
//
//    private static final String TAG = "JellyBeanCentral";
//
//    public static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
//    private static final int CONNECT_PARK = 0x100;
//
//    private static final int STATE_DISCONNECTED = 0;
//    private static final int STATE_CONNECTING = 1;
//    private static final int STATE_CONNECTED = 2;
//    private final BLEConnectListener mListener;
//
//    private int mConnectionState = STATE_DISCONNECTED;
//
//    private BluetoothAdapter mBluetoothAdapter;
//    private boolean bEnabled = false;
//    private Context mContext;
//    private BLEModifyListener mBLEModifyListener;
//    private BluetoothGatt mBluetoothGatt;
//    private BluetoothGattService mBluetoothGattService;
//    private BluetoothManager mBluetoothManager;
//    private Handler mHandler;
//    private String mMacAddress;
//    private String mBluetoothDeviceAddress;
//    private String mUuid;
//    private int mMajor;
//    private int mMinor;
//    private int mRfPower;
//    private int mAdInterval;
//    private long mSn;
//    private int mConnectType;
//    private int mResultStatus;
//    private int mCount;
//
//    private Queue<BluetoothGattCharacteristic> characteristicReadQueue = new LinkedList<>();
//
//    public ModifyDeviceCentral(Context context,
//                               BLEConnectListener bleConnectListener) {
//        mListener = bleConnectListener;
//        this.mContext = context;
//        mHandler = new Handler();
//        final BluetoothManager bluetoothManager =
//                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
//        mBluetoothManager = bluetoothManager;
//        if (bluetoothManager != null) {
//            mBluetoothAdapter = bluetoothManager.getAdapter();
//        }
//    }
//
//
//    public boolean modify(BLEModifyListener bleModifyListener, String macAddress, String UUID, int major, int minor, int rfPower, int adInterval, long sn) {
//        mCount = 0;
//        mResultStatus = 0;
//        mBLEModifyListener = bleModifyListener;
//        mMacAddress = macAddress;
//        mUuid = UUID;
//        mMajor = major;
//        mMinor = minor;
//        mRfPower = rfPower;
//        mAdInterval = adInterval;
//        mSn = sn;
//
//        if (mBluetoothDeviceAddress != null
//                && macAddress.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
//            LogHelper.d(TAG, "test Re-use GATT connection");
//            if (mBluetoothGatt.connect()) {
//                mConnectionState = STATE_CONNECTING;
//                return true;
//            }
//        }
//
//        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
//
//        if (device == null) {
//            Log.w(TAG, "test Device not found.  Unable to modify.");
//            return false;
//        }
//        LogHelper.d(TAG, "Create a new GATT connection.");
//        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
//        mBluetoothDeviceAddress = macAddress;
//        mConnectionState = STATE_CONNECTING;
//        return true;
//    }
//
//    @Override
//    public boolean scanForPeripheralsWithServices(UUID[] serviceUUIDs) {
//        return false;
//    }
//
//    @Override
//    public boolean scanForPeripheralsWithServices() {
//        return false;
//    }
//
//    @Override
//    public void stopScan() {
//        if (mBluetoothAdapter != null) {
////            LogHelper.i("[JellyBeanCentral]", String.format("Stop scanning: id-%s", mCallback.toString()));
////            mBluetoothAdapter.stopLeScan(mCallback);
//        }
//    }
//
//    @Override
//    public void release() {
//        if (mBluetoothGatt == null) {
//            return;
//        }
//        mBluetoothGatt.close();
//        mBluetoothGatt = null;
//    }
//
//    @Override
//    public void connect(BLEConnectListener bleListener, String macAddress, int connectType) {
//        if (mBluetoothDeviceAddress != null
//                && macAddress.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
//            LogHelper.d(TAG, "test Re-use GATT connection");
//            if (mBluetoothGatt.connect()) {
//                mConnectionState = STATE_CONNECTING;
//                return;
//            }
//        }
//
//        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
//
//        if (device == null) {
//            LogHelper.w(TAG, "test Device not found.  Unable to modify.");
//            return;
//        }
//        LogHelper.d(TAG, "Create a new GATT connection.");
//        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
//        mBluetoothDeviceAddress = macAddress;
//        mConnectionState = STATE_CONNECTING;
//    }
//
//
//    BluetoothDevice device;
//
//    @Override
//    public boolean isEnabled() {
//        return bEnabled;
//    }
//
//    @Override
//    public boolean isConnected(String address) {
//        if (mBluetoothDeviceAddress != null
//                && address.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
//            if (mBluetoothGatt.connect()) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void disConnect() {
//        if (mBluetoothGatt != null) {
//            mBluetoothGatt.disconnect();
//        }
//    }
//
//
//    @Override
//    public void read() {
//    }
//
//    @Override
//    public void write() {
//
//    }
//
//    @Override
//    public SmtManagerState getState() {
//        return null;
//    }
//
//    byte[] adIntervalSnByte;
//    byte[] rfPowerSnByte;
//    byte[] enByte;
//    byte[] ummByte;
//    private final int MODIFY_MM = 1;
//    private final int MODIFY_AR = 2;
//    private final int MODIFY_ALL = 3;
//    private int mCurrentModifyType;
//
//    private void modifyDevice(final BluetoothGatt gatt, int modifyType) {
//        mCurrentModifyType = modifyType;
//        BluetoothGattCharacteristic bluetoothGattCharacteristic = null;
//        if (modifyType == MODIFY_AR || modifyType == MODIFY_ALL) {
//            bluetoothGattCharacteristic = characteristicReadQueue.poll();
//            byte[] adIntervalSnByte = getAdIntervalSnByte();
//            bluetoothGattCharacteristic.setValue(adIntervalSnByte);
//        } else if (modifyType == MODIFY_MM) {
//            bluetoothGattCharacteristic = characteristicReadQueue.poll();
//            byte[] ummByte = getUmmByte();
//            bluetoothGattCharacteristic.setValue(ummByte);
//        }
//        if (bluetoothGattCharacteristic != null) {
//            gatt.writeCharacteristic(bluetoothGattCharacteristic);
//        } else {
////            mBLEModifyListener.onModifyFailed(ConnectError.CharNotFound);
//            disConnect();
//        }
//    }
//
//
////    private void modifyAdIntervalAndRfPower(final BluetoothGatt gatt, BluetoothGattCharacteristic adIntervalCha,
////                                            final BluetoothGattCharacteristic rfPowerCha) {
////        byte[] snByte           = getLongBytes(mSn);
////        byte[] adIntervalSnByte = new byte[10];
////        byte[] adIntervalByte   = getShortBytes((short) mAdInterval);
////        System.arraycopy(adIntervalByte, 0, adIntervalSnByte, 0, 2);
////        for (int i = 0; i < 8; i++) {
////            adIntervalSnByte[i + 2] = snByte[7 - i];
////        }
////        adIntervalCha.setValue(adIntervalSnByte);
////
////        boolean flag1 = gatt.writeCharacteristic(adIntervalCha);
////
////        final byte[] rfPowerSnByte = new byte[9];
////        rfPowerSnByte[0] = (byte) mRfPower;
////        for (int i = 0; i < 8; i++) {
////            rfPowerSnByte[i + 1] = snByte[7 - i];
////        }
////        rfPowerCha.setValue(rfPowerSnByte);
////
////        mHandler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                boolean flag2 = gatt.writeCharacteristic(rfPowerCha);
////            }
////        }, 500);
////    }
//
//    private void modifyUMMAndEnable(final BluetoothGatt gatt, BluetoothGattCharacteristic ummCha,
//                                    final BluetoothGattCharacteristic enableCha) {
//        byte[] snByte = getLongBytes(mSn);
//        byte[] ummByte = new byte[20];
//        byte[] uuidByte = getUUIDBytes(UUID.fromString(mUuid));
//
//        for (int i = 0; i < 16; i++) {
//            ummByte[i] = uuidByte[i];
//        }
//        byte[] major = getShortBytes((short) mMajor);
//        byte[] minor = getShortBytes((short) mMinor);
//        ummByte[16] = major[1];
//        ummByte[17] = major[0];
//        ummByte[18] = minor[1];
//        ummByte[19] = minor[0];
//        ummCha.setValue(ummByte);
//
//        byte[] enByte = new byte[9];
//        enByte[0] = (byte) 1;
//        for (int i = 0; i < 8; i++) {
//            enByte[i + 1] = snByte[7 - i];
//        }
//        enableCha.setValue(enByte);
//        boolean flag5 = gatt.writeCharacteristic(ummCha);
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                boolean flag4 = gatt.writeCharacteristic(enableCha);
//            }
//        }, 500);
//    }
//
//
//    private byte[] getAdIntervalSnByte() {
//        byte[] snByte = getLongBytes(mSn);
//        adIntervalSnByte = new byte[10];
//        byte[] adIntervalByte = getShortBytes((short) mAdInterval);
//        System.arraycopy(adIntervalByte, 0, adIntervalSnByte, 0, 2);
//        for (int i = 0; i < 8; i++) {
//            adIntervalSnByte[i + 2] = snByte[7 - i];
//        }
//        return adIntervalByte;
//    }
//
//    private byte[] getRfPowerSnByte() {
//        byte[] snByte = getLongBytes(mSn);
//        rfPowerSnByte = new byte[9];
//        rfPowerSnByte[0] = (byte) mRfPower;
//        for (int i = 0; i < 8; i++) {
//            rfPowerSnByte[i + 1] = snByte[7 - i];
//        }
//        return rfPowerSnByte;
//    }
//
//    private byte[] getUmmByte() {
//        ummByte = new byte[20];
//        byte[] uuidByte = getUUIDBytes(UUID.fromString(mUuid));
//
//        for (int i = 0; i < 16; i++) {
//            ummByte[i] = uuidByte[i];
//        }
//        byte[] major = getShortBytes((short) mMajor);
//        byte[] minor = getShortBytes((short) mMinor);
//        ummByte[16] = major[1];
//        ummByte[17] = major[0];
//        ummByte[18] = minor[1];
//        ummByte[19] = minor[0];
//        return ummByte;
//    }
//
//    public byte[] getEnByte() {
//        byte[] snByte = getLongBytes(mSn);
//        enByte = new byte[9];
//        enByte[0] = (byte) 1;
//        for (int i = 0; i < 8; i++) {
//            enByte[i + 1] = snByte[7 - i];
//        }
//        return enByte;
//    }
//
//    private void displayGattServices(final BluetoothGatt gatt, List<BluetoothGattService> gattServices) {
//        for (BluetoothGattService gattService : gattServices) {
//            if (gattService.getUuid().toString().contains("f5") || gattService.getUuid().toString().contains("f6")) {
//                mBluetoothGattService = gattService;
//                List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = mBluetoothGattService.getCharacteristics();
//                BluetoothGattCharacteristic adIntervalCha = bluetoothGattCharacteristics.get(0);
//                final BluetoothGattCharacteristic rfPowerCha = bluetoothGattCharacteristics.get(1);
//                final BluetoothGattCharacteristic ummCha = bluetoothGattCharacteristics.get(4);
//                final BluetoothGattCharacteristic enableCha = bluetoothGattCharacteristics.get(3);
//
//
//                if (mMinor == -100 && mMajor == -100) {
//                    characteristicReadQueue.add(adIntervalCha);
//                    characteristicReadQueue.add(rfPowerCha);
//                    modifyDevice(gatt, MODIFY_AR);
//                } else if (mRfPower == -100 && mAdInterval == -100) {
//                    characteristicReadQueue.add(ummCha);
//                    characteristicReadQueue.add(enableCha);
//                    modifyDevice(gatt, MODIFY_MM);
//                } else {
//                    characteristicReadQueue.add(adIntervalCha);
//                    characteristicReadQueue.add(rfPowerCha);
//                    characteristicReadQueue.add(ummCha);
//                    characteristicReadQueue.add(enableCha);
//                    modifyDevice(gatt, MODIFY_ALL);
//                }
//
////                if (mMinor == -100 && mMajor == -100) {
////                    modifyAdIntervalAndRfPower(gatt, adIntervalCha, rfPowerCha);
////                } else if (mRfPower == -100 && mAdInterval == -100) {
////                    modifyUMMAndEnable(gatt, ummCha, enableCha);
////                } else {
////                    modifyAdIntervalAndRfPower(gatt, adIntervalCha, rfPowerCha);
////                    mHandler.postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                            modifyUMMAndEnable(gatt, ummCha, enableCha);
////                        }
////                    }, 500);
////                }
//            }
//        }
//    }
//
//    BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @Override
//        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            super.onCharacteristicWrite(gatt, characteristic, status);
//            if (status == 0) {
//                if (characteristicReadQueue.size() != 0) {
//                    BluetoothGattCharacteristic nextCharacteristic = characteristicReadQueue.poll();
//                    byte[] bytes = null;
//                    if (nextCharacteristic.getUuid().toString().equals("0000c101-0000-1000-8000-00805f9b34fb")) {
//                        bytes = getAdIntervalSnByte();
//                    } else if (nextCharacteristic.getUuid().toString().equals("0000c102-0000-1000-8000-00805f9b34fb")) {
//                        bytes = getRfPowerSnByte();
//                    } else if (nextCharacteristic.getUuid().toString().equals("0000c104-0000-1000-8000-00805f9b34fb")) {
//                        bytes = getEnByte();
//                    } else if (nextCharacteristic.getUuid().toString().equals("0000c105-0000-1000-8000-00805f9b34fb")) {
//                        bytes = getUmmByte();
//                    }
//                    if (bytes != null) {
//                        nextCharacteristic.setValue(bytes);
//                        gatt.writeCharacteristic(nextCharacteristic);
//                    }
//                } else {
////                    mBLEModifyListener.onCharacWriteSucceed();
//                    disConnect();
//                }
//            } else {
////                mBLEModifyListener.onModifyFailed(ConnectError.CharNotFound);
//                disConnect();
//            }
//            LogHelper.d(TAG, "test onCharacteristicWrite");
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            super.onCharacteristicRead(gatt, characteristic, status);
//        }
//
//        @Override
//        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
//            super.onServicesDiscovered(gatt, status);
//            LogHelper.d(TAG, "test onServicesDiscovered status");
//            displayGattServices(gatt, gatt.getServices());
//        }
//
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            super.onConnectionStateChange(gatt, status, newState);
//            LogHelper.d(TAG, "test status: " + status + " newState: " + newState);
//            if (newState == BluetoothProfile.STATE_CONNECTED && status == 0) {
//                mBluetoothGatt.discoverServices();
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            super.onCharacteristicChanged(gatt, characteristic);
//            LogHelper.d(TAG, "test onCharacteristicChanged value: " + characteristic.getValue()[0]);
//            //TODO  后面的参数用来做一些判断
////            if (characteristic.getValue()[0] == 1) {
////                if (mBleConnectListener != null) {
//////                    mBleListener.onDeviceNotify(true);
////                } else {
//////                    mBleListener.onDeviceNotify(false);
////                }
////                gatt.disconnect();
////            }
//        }
//
//        @Override
//        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//            super.onDescriptorRead(gatt, descriptor, status);
//        }
//
//    };
//
//    private byte[] getUUIDBytes(UUID uuid) {
//        byte[] data = new byte[16];
//        long msb = uuid.getMostSignificantBits();
//        long lsb = uuid.getLeastSignificantBits();
//        for (int i = 0; i < 8; i++) {
//            data[7 - i] = (byte) (msb & 0xff);
//            msb >>= 8;
//        }
//        for (int i = 0; i < 8; i++) {
//            data[15 - i] = (byte) (lsb & 0xff);
//            lsb >>= 8;
//        }
//        return data;
//    }
//
//    public static byte[] getShortBytes(short data) {
//        byte[] bytes = new byte[2];
//        bytes[0] = (byte) (data & 0xff);
//        bytes[1] = (byte) ((data & 0xff00) >> 8);
//        return bytes;
//    }
//
//    public static byte[] getLongBytes(long data) {
//        byte[] bytes = new byte[8];
//        bytes[0] = (byte) (data & 0xff);
//        bytes[1] = (byte) ((data >> 8) & 0xff);
//        bytes[2] = (byte) ((data >> 16) & 0xff);
//        bytes[3] = (byte) ((data >> 24) & 0xff);
//        bytes[4] = (byte) ((data >> 32) & 0xff);
//        bytes[5] = (byte) ((data >> 40) & 0xff);
//        bytes[6] = (byte) ((data >> 48) & 0xff);
//        bytes[7] = (byte) ((data >> 56) & 0xff);
//        return bytes;
//    }
//}
