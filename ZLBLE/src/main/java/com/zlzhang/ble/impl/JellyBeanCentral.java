/*
 * ==============================BEGIN_COPYRIGHT===============================
 * =======Wangluo Electronic Technology Co., LTD. PROPRIETARY INFORMATION======
 * This software and its associated IndoorStar products are supplied under the
 * terms of a license agreement or nondisclosure agreement (NDA) with Wangluo
 * Electronic Technology Co., LTD., Shanghai and may not be copied or disclosed
 * except in accordance with the terms of that agreement.
 * Copyright (c) 2013 - 2015
 * Wangluo Electronic Technology Co., LTD., Shanghai. All Rights Reserved.
 * ===============================END_COPYRIGHT================================
 *
 * @author - IDS R&D Group - Patrick Zhang
 * @date   - 2015-05-10 16:50
 */

package com.zlzhang.ble.impl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

import com.ids.bls.BLECentral;
import com.zlzhang.ble.conn.BLEConnectListener;
import com.zlzhang.ble.scan.BLEScanListener;
import com.ids.smt.cmn.SmtManagerState;
import com.ids.util.android.LogHelper;

import java.util.UUID;

public class JellyBeanCentral extends AbstractBLECentral implements BLECentral {

    private static final String TAG = "JellyBeanCentral";

    public static        UUID CLIENT_UUID  = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final int  CONNECT_PARK = 0x100;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter.LeScanCallback mCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice bluetoothDevice, int rssi,
                                     byte[] bytes) {
                    mListener.didDiscoverDevice(bluetoothDevice, rssi, bytes);
                }
            };
    private boolean                         bEnabled  = false;
    private Context         mContext;
    private BluetoothGatt   mBluetoothGatt;
    //    private BLEConnectListener mBleConnectListener;
    private BluetoothDevice mDevice;

    private Handler mHandler;
    private String  mMacAddress;
    private String  mBluetoothDeviceAddress;
    private int     mReTry;


    public JellyBeanCentral(Context context,
                            BLEScanListener bleCentralListener) {
        LogHelper.d("test", "test JellyBeanCentral");
        this.mListener = bleCentralListener;
        this.mContext = context;
        mHandler = new Handler();
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
            if (mBluetoothAdapter != null) {
                if (mBluetoothAdapter.isEnabled()) {
                    setState(SmtManagerState.BLECentralStatePoweredOn);
                    bEnabled = true;
                } else {
                    setState(SmtManagerState.BLECentralStatePoweredOff);
                }
            } else {
                setState(SmtManagerState.BLECentralStateUnknown);
            }
        } else {
            setState(SmtManagerState.BLECentralStateUnsupported);
        }

    }

    @Override
    public boolean scanForPeripheralsWithServices(UUID[] serviceUUIDs) {
        if (mBluetoothAdapter != null) {
            LogHelper.i("[JellyBeanCentral]", String.format("Start scanning: id-%s", mCallback.toString()));
            return mBluetoothAdapter.startLeScan(mCallback);
        } else {
            return false;
        }
    }

    @Override
    public boolean scanForPeripheralsWithServices() {
        return false;
    }

    @Override
    public void stopScan() {
        if (mBluetoothAdapter != null) {
            LogHelper.i("[JellyBeanCentral]", String.format("Stop scanning: id-%s", mCallback.toString()));
            mBluetoothAdapter.stopLeScan(mCallback);
        }
    }

    @Override
    public void release() {
    }


    @Override
    public void connect(BLEConnectListener bleListener, String macAddress, int connectType) {
    }


    @Override
    public boolean isEnabled() {
        return bEnabled;
    }

    @Override
    public boolean isConnected(String address) {
        return true;
    }

    @Override
    public void disConnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    @Override
    public void read() {
    }

    @Override
    public void write() {

    }

    BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
//            mBleConnectListener.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
//            mBleConnectListener.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            LogHelper.d(TAG, "test status: " + status + " newState: " + newState);

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

    };
}
