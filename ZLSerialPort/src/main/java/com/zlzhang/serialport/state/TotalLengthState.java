package com.zlzhang.serialport.state;

/**
 * Created by zhilaizhang on 17/8/20.
 */

public class TotalLengthState implements IDataState {

    private  DataPackageContext mDataPackageContext;

    public TotalLengthState(DataPackageContext dataPackageContext){
        mDataPackageContext = dataPackageContext;
    }

    @Override
    public void setData(byte data) {
        int contentLength = data - 6;
        if(contentLength > 0){
            mDataPackageContext.setContentLength(contentLength);
            mDataPackageContext.setState(mDataPackageContext.getAddressState());
        } else {
            mDataPackageContext .setState(mDataPackageContext.getMagicNum1State());
        }
    }

    @Override
    public void packageData() {

    }
}
