package com.zlzhang.serialport.state;

import com.zlzhang.serialport.util.SerialPortUtil;

/**
 * Created by zhilaizhang on 17/8/20.
 */

public class MagicNum2State implements IDataState {

    private DataPackageContext mDataPackageContext;

    public MagicNum2State(DataPackageContext dataPackageContext){
        mDataPackageContext = dataPackageContext;
    }

    @Override
    public void setData(byte data) {
        if (data == SerialPortUtil.MAGIC_NUM2) {
            mDataPackageContext.setState(mDataPackageContext.getMagicNum3State());
        } else {
            mDataPackageContext.setState(mDataPackageContext.getMagicNum1State());
        }
    }

    @Override
    public void packageData() {

    }
}
