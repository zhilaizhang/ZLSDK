package com.zlzhang.sdk.client;

import android.util.Pair;

import com.zlzhang.client.base.BaseGetAction;
import com.zlzhang.client.base.BasePostAction;

import java.util.List;

/**
 * Created by zhangzhilai on 2018/8/13.
 */

public class TestGetAction extends BaseGetAction {

    public TestGetAction(String url){
        super(url);
    }

    @Override
    protected void doResponse(String response) throws Exception {
        setData(response);
    }

    @Override
    public void setParam(List<Pair> params) {

    }
}
