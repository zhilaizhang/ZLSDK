package com.zlzhang.sdk.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zlzhang.client.base.Action;
import com.zlzhang.client.handler.ActionHandler;
import com.zlzhang.sdk.R;

import java.io.Serializable;

/**
 * Created by zhangzhilai on 2018/8/13.
 */

public class ClientActivity extends Activity {

    public static final String EMAIL_LOGINURL = "/api/user/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void postTest(View view) {
        Action.BASE_URL = "http://reg.hexun.com";
        String url = "/app/login.aspx";
        TestPostAction testPostAction = new TestPostAction(url, "18521095832", "zzl1234");

        testPostAction.execute(true, new ActionHandler.ActionAdapter(){
            @Override
            public void doActionResponse(int status, Serializable message) {
                super.doActionResponse(status, message);
                Log.d("test", "test " + message);
            }
        });
    }

    public void getTest(View view) {
        String url = " http://ipad-bjwb.bjd.com.cn/DigitalPublication/publish/Handler/APINewsList.ashx?date=20151031&startRecord=1&len=5&udid=1234567890&terminalType=Iphone&cid=213";
        TestGetAction testGettAction = new TestGetAction(url);
        testGettAction.execute(true, new ActionHandler.ActionAdapter(){
            @Override
            public void doActionResponse(int status, Serializable message) {
                super.doActionResponse(status, message);
                Log.d("test", "test " + message);
            }
        });
    }
}
