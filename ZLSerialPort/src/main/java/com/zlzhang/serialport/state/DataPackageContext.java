package com.zlzhang.serialport.state;

/**
 * Created by zhilaizhang on 17/8/20.
 */

public class DataPackageContext {

    private MagicNum1State mMagicNum1State;
    private MagicNum2State mMagicNum2State;
    private MagicNum3State mMagicNum3State;
    private TotalLengthState mTotalLengthState;
    private AddressState mAddressState;
    private PackageState mPackageState;
    private IDataState mCurrentState;

    private OnDataPackageListener mOnDataPackageListener;

    private int mContentLength;
    private byte[] mAddress;
    private byte[] mContents;

    public DataPackageContext(OnDataPackageListener onDataPackageListener) {
        mMagicNum1State = new MagicNum1State(this);
        mMagicNum2State = new MagicNum2State(this);
        mMagicNum3State = new MagicNum3State(this);
        mTotalLengthState = new TotalLengthState(this);
        mAddressState = new AddressState(this);
        mPackageState = new PackageState(this);
        mCurrentState = mMagicNum1State;
        mOnDataPackageListener = onDataPackageListener;
    }

    public void setData(byte data){
        mCurrentState.setData(data);
    }

    public void setContentLength(int contentLength) {
        mContentLength = contentLength;
    }

    public int getContentLength() {
        return mContentLength;
    }

    public void setAddress(byte[] address) {
        mAddress = address;
    }

    public void setContents(byte[] contents){
        mContents = contents;
        mOnDataPackageListener.onPackageSucceed(mAddress, mContents, mContentLength);
    }

    public void setState(IDataState dataState) {
        mCurrentState = dataState;
    }

    public MagicNum1State getMagicNum1State() {
        return mMagicNum1State;
    }

    public MagicNum2State getMagicNum2State() {
        return mMagicNum2State;
    }

    public MagicNum3State getMagicNum3State() {
        return mMagicNum3State;
    }

    public TotalLengthState getTotalLengthState() {
        return mTotalLengthState;
    }

    public AddressState getAddressState() {
        return mAddressState;
    }

    public PackageState getPackageState() {
        return mPackageState;
    }

    public void unRegisterListener(){
        mOnDataPackageListener = null;
    }

    public interface OnDataPackageListener{
        void onPackageSucceed(byte[] address, byte[] contents, int length);
    }
}
