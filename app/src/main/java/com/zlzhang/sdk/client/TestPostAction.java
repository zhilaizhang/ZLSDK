package com.zlzhang.sdk.client;

import android.util.Pair;

import com.zlzhang.client.base.BasePostAction;

import java.util.List;

/**
 * Created by zhangzhilai on 2018/8/13.
 */

public class TestPostAction extends BasePostAction {

    public TestPostAction(String url){
        super(url);
    }

    @Override
    protected void doResponse(String response) throws Exception {
        setData(response);
    }

    @Override
    public void setParam(List<Pair> params) {
        mParams.add(new Pair("email", ""));
        mParams.add(new Pair("password", ""));
        mParams.add(new Pair("project", ""));
        mParams.add(new Pair("os", ""));
        mParams.add(new Pair("version", ""));
        mParams.add(new Pair("installation_id", ""));
    }
}
