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


import com.zlzhang.ble.scan.BLEScanListener;

public abstract class AbstractBLECentral {
//    protected SmtManagerState mState;
    protected BLEScanListener mListener;

//    public SmtManagerState getState() {
//        return mState;
//    }
//
//    protected void setState(SmtManagerState state) {
//        mState = state;
//        mListener.didUpdateState(state);
//    }
}
