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
// * @date   - 2015-05-10 16:50
// */
//
//package com.zlzhang.ble.impl;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//
//import com.zlzhang.ble.BLECentral;
//import com.zlzhang.ble.conn.BLEConnectListener;
//import com.zlzhang.ble.scan.BLEScanListener;
//
//import java.util.UUID;
//
//public class StandardCentral extends AbstractBLECentral implements BLECentral {
//    private BluetoothAdapter mBluetoothAdapter;
//    private Context          mContext;
//    private boolean bEnabled = false;
//
//    // Create a BroadcastReceiver for ACTION_FOUND
//    private final BroadcastReceiver mReceiver =
//            new BroadcastReceiver() {
//                public void onReceive(Context context, Intent intent) {
//                    String action = intent.getAction();
//                    // When discovery finds a device
//                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                        // Get the BluetoothDevice object from the Intent
//                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                        if (intent.getExtras() != null) {
//                            mListener.didDiscoverDevice(device, intent.getExtras().getShort(
//                                    BluetoothDevice.EXTRA_RSSI), null);
//                        }
//                    }
//                }
//            };
//
//
//    public StandardCentral(Context context, BLEScanListener blsScanListener) {
//        this.mListener = blsScanListener;
//        this.mContext = context;
//        // Register the BroadcastReceiver
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        this.mContext.registerReceiver(mReceiver,
//                filter); // Don't forget to unregister during onDestroy
//        if (Build.VERSION.SDK_INT < 18) {
//            mBluetoothAdapter =
//                    (BluetoothAdapter) context.getSystemService(Context.BLUETOOTH_SERVICE);
//        } else {
//            final BluetoothManager bluetoothManager =
//                    (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
//            mBluetoothAdapter = bluetoothManager.getAdapter();
//        }
//
//        if (mBluetoothAdapter != null) {
//            if (mBluetoothAdapter.isEnabled()) {
//                setState(SmtManagerState.BLECentralStatePoweredOn);
//                bEnabled = true;
//            } else {
//                setState(SmtManagerState.BLECentralStatePoweredOff);
//            }
//        } else {
//            setState(SmtManagerState.BLECentralStateUnsupported);
//        }
//
//    }
//
//    @Override
//    public boolean scanForPeripheralsWithServices(UUID[] serviceUUIDs) {
//        return mBluetoothAdapter != null && mBluetoothAdapter.startDiscovery();
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
//            mBluetoothAdapter.cancelDiscovery();
//        }
//    }
//
//    @Override
//    public void release() {
//        this.mContext.unregisterReceiver(mReceiver);
//    }
//
//    @Override
//    public void connect(BLEConnectListener bleListener, String address, int connectType) {
//
//    }
//
//
//    @Override
//    public void read() {
//    }
//
//    @Override
//    public void write() {
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return bEnabled;
//    }
//
//    @Override
//    public boolean isConnected(String address) {
//        return false;
//    }
//
//    @Override
//    public void disConnect() {
//
//    }
//
//}
