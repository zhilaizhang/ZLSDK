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

package com.zlzhang.ble;



import com.zlzhang.ble.conn.BLEConnectListener;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-17
 * Time: 上午9:56
 * To change this template use File | Settings | File Templates.
 */
public interface BLECentral {

    boolean scanForPeripheralsWithServices(UUID[] serviceUUIDs);

    boolean scanForPeripheralsWithServices();

    void stopScan();

    void release();

    void connect(BLEConnectListener bleListener, String address, int connectType);

    boolean isConnected(String address);

    void disConnect();

    boolean isEnabled();

    void read();

    void write();

//    SmtManagerState getState();
}
