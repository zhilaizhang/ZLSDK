

package com.zlzhang.ble.conn;

/**
 * Created by zhilaizhang on 5/25/16.
 */
public interface BLEModifyListener {
    void onServicesDiscovered(int status);

    void onCharacteristicWrite(int status);

    void onCharacteristicRead();

}
