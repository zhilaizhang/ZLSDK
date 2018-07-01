package com.zlzhang.serialport.state;

import com.zlzhang.serialport.util.SerialPortUtil;

/**
 * Created by zhilaizhang on 17/8/20.
 */

public class MagicNum3State implements IDataState {

    private DataPackageContext mDataPackageContext;
    public MagicNum3State(DataPackageContext dataPackageContext){
        mDataPackageContext = dataPackageContext;
    }

    @Override
    public void setData(byte data) {
        if(data == SerialPortUtil.MAGIC_NUM3){
            mDataPackageContext.setState(mDataPackageContext.getTotalLengthState());
        } else {
            mDataPackageContext.setState(mDataPackageContext.getMagicNum1State());
        }
    }

    @Override
    public void packageData() {

    }
}
