package com.zlzhang.serialport.state;

/**
 * Created by zhilaizhang on 17/8/20.
 */

public class AddressState implements IDataState {
    private DataPackageContext mDataPackageContext;
    private int mAddressIndex;
    private byte[] mAddress;

    public AddressState(DataPackageContext dataPackageContext){
        mDataPackageContext = dataPackageContext;
        mAddressIndex = 0;
        mAddress = new byte[2];
    }

    @Override
    public void setData(byte data) {
        if(mAddressIndex == 0){
            mAddress[mAddressIndex] = data;
            mAddressIndex++;
            mDataPackageContext.setState(mDataPackageContext.getAddressState());
        } else {
            mAddress[mAddressIndex] = data;
            mAddressIndex = 0;
            mDataPackageContext.setAddress(mAddress);
            mDataPackageContext.setState(mDataPackageContext.getPackageState());
        }
    }

    @Override
    public void packageData() {

    }
}
