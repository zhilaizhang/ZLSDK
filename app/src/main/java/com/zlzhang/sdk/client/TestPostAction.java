package com.zlzhang.sdk.client;

import android.util.Pair;

import com.zlzhang.client.base.BasePostAction;

import java.util.List;

/**
 * Created by zhangzhilai on 2018/8/13.
 */

public class TestPostAction extends BasePostAction {

    private String username;
    private String password;

    public TestPostAction(String url, String username, String password){
        super(url);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void doResponse(String response) throws Exception {
        setData(response);
    }

    @Override
    public void setParam(List<Pair> params) {
        mParams.add(new Pair("username", username));
        mParams.add(new Pair("password", password));
    }
}
