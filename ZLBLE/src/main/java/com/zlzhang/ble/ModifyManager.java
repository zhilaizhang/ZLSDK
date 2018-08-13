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
 * @date   - 2016-05-26 14:07
 */

package com.zlzhang.ble;


import android.content.Context;


public abstract class ModifyManager {

    private static ModifyManager mModifyManager;
    protected      boolean       bGlobalScan    = false;
    protected      boolean       bGlobalRunning = false;

    public static ModifyManager getInstance(Context context){
        if (mModifyManager == null) {
//            mModifyManager = new BLEModifyImpl(context);
        }
        return mModifyManager;
    }

    protected abstract void  startScan(long starSn, long millTime);

    protected abstract void stopScan();

    protected abstract void connect(String macAddress);

    protected abstract void disConnect();

    protected abstract void modify();

    protected abstract boolean isConnected(String address);

    protected  void release(){
        mModifyManager = null;
    }

}
