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
 * @date   - 2015-11-11 11:31
 */

package com.zlzhang.ble.scan;

import android.bluetooth.BluetoothDevice;


/**
 * Created by zhilaizhang on 11/11/15.
 */
public interface BLEScanListener {

    void didDiscoverDevice(BluetoothDevice bluetoothDevice, int rssi, byte[] bytes);

//    void didUpdateState(SmtManagerState state);
}
