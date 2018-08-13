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

import android.content.Context;


import java.util.List;

/**
 * ble scan manager
 */
public abstract class BLSManager {

    private static BLSManager sInstance      = null;
    private static Context     sContext       = null;
    protected      boolean     bGlobalScan    = false;
    protected      boolean     bGlobalRunning = false;

    public static BLSManager getInstance(Context context) {
        if (sInstance == null) {
//            sInstance = new BLSManagerImpl(context);
            sContext = context;
        }
        return sInstance;
    }

    public void toggleGlobalScan(boolean enable) {
        bGlobalScan = enable;
    }

    public abstract void stopGlobalScan();

    public boolean isGlobalScan() {
        return bGlobalScan;
    }

    public static Context getContext() {
        return sContext;
    }

//    public abstract boolean startBLEScan(
//            SmtListener<List<RSSIRecord>> smtListener, long scanSeconds, int scanType);
//
//    public abstract boolean startBLEScan(
//            SmtListener<List<RSSIRecord>> smtListener, long scanSeconds);


    public abstract boolean validateBLEState();

    public void release() {
        sInstance = null;
        sContext = null;
    }

}
