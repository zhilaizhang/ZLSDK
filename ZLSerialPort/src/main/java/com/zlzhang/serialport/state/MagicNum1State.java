package com.zlzhang.serialport.state;

import com.zlzhang.serialport.util.SerialPortUtil;

/**
 * Created by zhilaizhang on 17/8/20.
 */

public class MagicNum1State implements IDataState {

    private DataPackageContext mDataPackageContext;

    public MagicNum1State(DataPackageContext dataPackageContext ){
        mDataPackageContext = dataPackageContext;
    }

    @Override
    public void setData(byte data) {
        if(data == SerialPortUtil.MAGIC_NUM1){
            mDataPackageContext.setState(mDataPackageContext.getMagicNum2State());
        } else {
            mDataPackageContext.setState(this);
        }
    }

    @Override
    public void packageData() {

    }
}
