package com.zlzhang.serialport.state;

/**
 * Created by zhilaizhang on 17/8/20.
 */

public class PackageState implements IDataState {

    private DataPackageContext mDataPackageContext;
    private int mContentLength;
    private int mContentIndex;
    private byte[] mContents;

    public PackageState(DataPackageContext dataPackageContext){
        mDataPackageContext = dataPackageContext;
        mContentIndex = 0;
    }

    @Override
    public void setData(byte data) {
        if(mContentIndex == 0){
            mContentLength = mDataPackageContext.getContentLength();
            mContents = new byte[mContentLength];
            mContents[mContentIndex++] = data;
        } else if(mContentIndex == mContentLength - 1){
            mContents[mContentIndex] = data;
            mContentIndex = 0;
            mDataPackageContext.setContents(mContents);
            mDataPackageContext.setState(mDataPackageContext.getMagicNum1State());
        } else {
            mContents[mContentIndex++] = data;
        }
    }

    @Override
    public void packageData() {

    }
}
