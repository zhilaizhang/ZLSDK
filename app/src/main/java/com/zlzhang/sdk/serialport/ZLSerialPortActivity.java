package com.zlzhang.sdk.serialport;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.zlzhang.sdk.R;
import com.zlzhang.serialport.SerialPortManager;
import com.zlzhang.serialport.listener.OnSerialPortListener;

/**
 * Created by zhangzhilai on 2018/7/1.
 */

public class ZLSerialPortActivity extends Activity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialport);
        mContext = this;

    }


    public void openSerialPort(View view) {
        SerialPortManager.getInstance(mContext).configSerialPort(new OnSerialPortListener() {
            @Override
            public void onOpened(int status) {

            }

            @Override
            public void onClosed() {

            }

            @Override
            public void onRead(byte[] address, byte[] content, int size) {

            }

            @Override
            public void onReadRaw(byte[] rawData, int size) {

            }

            @Override
            public void onWrite(byte[] content) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }




}
