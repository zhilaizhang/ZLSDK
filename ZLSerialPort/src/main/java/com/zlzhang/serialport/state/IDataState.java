package com.zlzhang.serialport.state;

/**
 * Created by zhilaizhang on 17/8/20.
 */

public interface IDataState {

    void setData(byte data);

    void packageData();
}
