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
// * @date   - 2016-04-14 11:27
// */
//
//package com.zlzhang.ble.impl;
//
//import android.annotation.TargetApi;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothManager;
//import android.bluetooth.BluetoothProfile;
//import android.bluetooth.le.BluetoothLeScanner;
//import android.bluetooth.le.ScanCallback;
//import android.bluetooth.le.ScanFilter;
//import android.bluetooth.le.ScanResult;
//import android.bluetooth.le.ScanSettings;
//import android.content.Context;
//import android.os.Build;
//
//import com.zlzhang.ble.BLECentral;
//import com.zlzhang.ble.conn.BLEConnectListener;
//import com.zlzhang.ble.scan.BLEScanListener;
//import com.zlzhang.util.LogHelper;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//import java.util.UUID;
//
//@TargetApi(Build.VERSION_CODES.LOLLIPOP)
//public class LollipopCentral extends AbstractBLECentral implements BLECentral {
//
//    private static final String TAG = "JellyBeanCentral";
//
//    private static final int    CONNECT_PARK                 = 0x100;
//    public               String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
//
//    public String CLIENT_CHARACTERISTIC_CONFIG1 = "00002901-0000-1000-8000-00805f9b34fb";
//
//    private int mConnectionState = STATE_DISCONNECTED;
//
//    private static final int STATE_DISCONNECTED = 0;
//    private static final int STATE_CONNECTING   = 1;
//    private static final int STATE_CONNECTED    = 2;
//
//    private BluetoothAdapter     mBluetoothAdapter;
//    private BluetoothLeScanner   mBluetoothLeScanner;
//    private ScanSettings.Builder mScanSettingsBuilder;
//
//    private boolean bEnabled = false;
//    private Context            mContext;
//    private BluetoothGatt      mBluetoothGatt;
//    private BLEConnectListener mBLEConnectListener;
//
//    private String mBluetoothDeviceAddress;
//    private Queue<BluetoothGattCharacteristic> characteristicReadQueue = new LinkedList<>();
//
//    private byte[]  mDatas;
//    private int     mDataLength;
//    private boolean isConnected;
//
//    private ScanCallback mScanCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//            super.onScanResult(callbackType, result);
//            if (result != null) {
//                mListener.didDiscoverDevice(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
//            }
//        }
//    };
//
//    public LollipopCentral(Context context,
//                           BLEScanListener bleCentralListener) {
//        LogHelper.d(TAG, "test did LollipopCentral");
//        this.mListener = bleCentralListener;
//        this.mContext = context;
//        init();
//    }
//
//    private void init() {
//        final BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
//        if (bluetoothManager != null) {
//            mBluetoothAdapter = bluetoothManager.getAdapter();
//            if (mBluetoothAdapter != null) {
//                if (mBluetoothAdapter.isEnabled()) {
//                    setState(SmtManagerState.BLECentralStatePoweredOn);
//                    bEnabled = true;
//                    mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
//                    mScanSettingsBuilder = new ScanSettings.Builder();
//                } else {
//                    setState(SmtManagerState.BLECentralStatePoweredOff);
//                }
//            } else {
//                setState(SmtManagerState.BLECentralStateUnknown);
//            }
//        } else {
//            setState(SmtManagerState.BLECentralStateUnsupported);
//        }
//    }
//
//    @Override
//    public boolean scanForPeripheralsWithServices(UUID[] serviceUUIDs) {
//        return scanForPeripheralsWithServices();
//    }
//
//    @Override
//    public boolean scanForPeripheralsWithServices() {
//        if (mScanSettingsBuilder == null || mBluetoothLeScanner == null) {
//            init();
//        }
//        List<ScanFilter> scanFilters = new ArrayList<>();
//        ScanSettings scanSettings = mScanSettingsBuilder.build();
//        mBluetoothLeScanner.startScan(scanFilters, scanSettings, mScanCallback);
//        return true;
//    }
//
//    @Override
//    public void stopScan() {
//        if (mBluetoothLeScanner != null) {
//            mBluetoothLeScanner.stopScan(mScanCallback);
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
//
//    @Override
//    public void connect(BLEConnectListener bleListener, String macAddress, int connectType) {
//        mBLEConnectListener = bleListener;
//        if (mBluetoothDeviceAddress != null
//                && macAddress.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
//            LogHelper.d(TAG, "test Re-use GATT connection");
//            if (isConnected) {
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
//    @Override
//    public boolean isEnabled() {
//        return bEnabled;
//    }
//
//    @Override
//    public boolean isConnected(String address) {
//        if (isConnected) {
//            return true;
//        } else {
//            return false;
//        }
////        if (mBluetoothDeviceAddress != null
////                && address.equals(mBluetoothDeviceAddress)
////                && mBluetoothGatt != null && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
////            if (isConnected) {
////                return true;
////            } else {
////                return false;
////            }
////        }
////        return false;
//    }
//
//    @Override
//    public void disConnect() {
//        if (mBluetoothGatt != null) {
//            mBluetoothGatt.disconnect();
//        }
//    }
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
//    int                   totalLength   = 0;
//    BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @Override
//        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            super.onCharacteristicWrite(gatt, characteristic, status);
//            LogHelper.d(TAG, "test onCharacteristicWrite status: " + status);
////            mBLEConnectListener.onCharacteristicWrite(gatt, characteristic, status);
//        }
//
//        @Override
//        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
//            super.onServicesDiscovered(gatt, status);
//            LogHelper.d(TAG, "test onServicesDiscovered status: " + status);
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                registerNotification(gatt);
////                mBLEConnectListener.onServicesDiscovered(gatt, status);
//            }
//        }
//
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            super.onConnectionStateChange(gatt, status, newState);
//            LogHelper.d(TAG, "test status: " + status + " newState: " + newState);
////            mBLEConnectListener.onConnectionStateChange(gatt, status, newState);
//            if (newState == BluetoothProfile.STATE_CONNECTED && status == 0) {
//                mBluetoothGatt.discoverServices();
//                isConnected = true;
//            } else {
//                isRunning = false;
//                isConnected = false;
//                disConnect();
//            }
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            super.onCharacteristicRead(gatt, characteristic, status);
//            LogHelper.d(TAG, "test onCharacteristicRead status: " + status);
//            LogHelper.d(TAG, "test onCharacteristicRead uuid: " + characteristic.getUuid());
//            if (characteristic.getUuid().toString().equals("0000dcb2-0000-1000-8000-00805f9b34fb")) {  //初始化数组
//                mDatas = new byte[totalLength];
//                mDataLength = 0;
//            }
//            if (!characteristic.getUuid().toString().equals("0000dcb1-0000-1000-8000-00805f9b34fb")) {
//                for (byte b : characteristic.getValue()) {
//                    if (mDataLength < totalLength) {
//                        mDatas[mDataLength++] = b;
//                    }
//                    if (mDataLength == totalLength) {
//                        mDataLength = 0;
//                        isRunning = false;
////                        mBLEConnectListener.onDataSucceed(mDatas);
//                        break;
//                    }
//                }
//            }
//            pollCharacter();
////            mBLEConnectListener.onCharacteristicRead(gatt, characteristic, status);
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            super.onCharacteristicChanged(gatt, characteristic);
//            LogHelper.d(TAG, "test onCharacteristicChanged: uuid: " + characteristic.getUuid().toString());
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
//                pollCharacter();
//            }
//        }
//
//
//        @Override
//        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//            super.onDescriptorRead(gatt, descriptor, status);
//            LogHelper.d(TAG, "test onDescriptorRead: ");
////            mBLEConnectListener.onDescriptorRead(gatt, descriptor, status);
//        }
//
//        @Override
//        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//            super.onDescriptorWrite(gatt, descriptor, status);
//            LogHelper.d(TAG, "test onDescriptorWrite: ");
//        }
//    };
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
//    boolean isRunning  = false;
//    int     repeateNum = 0;
//
//    private void registerNotification(BluetoothGatt gatt) {
//        LogHelper.d(TAG, "test registerNotification");
//        List<BluetoothGattService> gattServices = gatt.getServices();
//        for (BluetoothGattService gattService : gattServices) {
//            if (gattService.getUuid().toString().toLowerCase().contains("dcba")) {
//                List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = gattService.getCharacteristics();
//                for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattCharacteristics) {
//                    if (bluetoothGattCharacteristic.getUuid().toString().contains("dcb1")) {
//                        LogHelper.d(TAG, "test Characteristicuuid: " + bluetoothGattCharacteristic.getUuid());
//                        boolean b = gatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
//                        if (bluetoothGattCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG)) != null) {
//                            BluetoothGattDescriptor desc = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
//                            desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                            if (!gatt.writeDescriptor(desc)) {
//                                LogHelper.d(TAG, "Couldn't write RX client descriptor value!");
//                            }
//                        } else {
//                            LogHelper.d(TAG, "Couldn't get RX client descriptor!");
//                        }
//                    }
//                }
//            }
//
//        }
//    }
//}
