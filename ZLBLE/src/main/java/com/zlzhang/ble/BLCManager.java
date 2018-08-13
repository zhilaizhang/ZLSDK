

package com.zlzhang.ble;

import android.content.Context;

import com.zlzhang.ble.conn.IBLEConnector;
import com.zlzhang.ble.conn.IBLEModifier;
import com.zlzhang.ble.impl.BLCManagerImpl;


/**
 * Created by zhilaizhang on 4/15/16.
 * ble connect mannager
 */
public abstract class BLCManager implements IBLEConnector, IBLEModifier {

    private static BLCManager sInstance = null;
    protected static final int STATE_DISCONNECTED = 0;
    protected static final int STATE_CONNECTING = 1;
    protected static final int STATE_CONNECTED = 2;
    protected static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    protected Context mContext;

    public static BLCManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BLCManagerImpl(context);
        }
        return sInstance;
    }

    public BLCManager(Context context) {
        mContext = context;
    }


}
