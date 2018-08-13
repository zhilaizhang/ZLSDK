package com.zlzhang.ble.conn;


import com.zlzhang.ble.model.BLEModifyModel;

/**
 * Created by zhilaizhang on 17/2/16.
 */

public interface IBLEModifier {

    boolean modifyDeviceConfig(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, String UUID, int major,
                               int minor, int rfPower, int adInterval, long sn);

    boolean modifyDeviceCityAndMallId(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, int measurePower, int cityId, int mallId);

    boolean modifyDeviceCityId(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, int cityId);

    boolean modifyDeviceMallId(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, int mallId);

    boolean modifyDeviceStarSn(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long oldStarSn, long newStarSn);

    boolean modifyDeviceAdInterval(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, short adInterval);

    boolean modifyDeviceTxPower(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, byte txPower);

    boolean modifyDeviceAdMode(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, byte[] adMode);

    boolean modifyDeviceMeasurePower(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, byte measurePower);

    boolean modifyDeviceRFPower(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, byte rfPower);

    boolean modifyDeviceAdContent(BLEModifyListener bleModifyListener, BLEModifyModel bleModifyModel, long starSn, int adIndex, int dataIndex, int keyIndex,  byte[] adContent);

}
