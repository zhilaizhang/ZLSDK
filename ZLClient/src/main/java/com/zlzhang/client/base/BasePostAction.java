package com.zlzhang.client.base;

import android.util.Pair;

import java.io.OutputStream;


/**
 * Created by zhilaizhang on 17/9/29.
 */

public abstract class BasePostAction extends BaseAction {

    public BasePostAction(String url) {
        super(url);
        this.sURL = BASE_URL + url;
    }

    @Override
    protected void initURLConnection() throws Exception {
        String params = initBody();
        openConnection();
        mHttpURLConnection.setRequestMethod("POST");
        mHttpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
        mHttpURLConnection.setReadTimeout(READ_TIMEOUT);
        mHttpURLConnection.setDoOutput(true);
        mHttpURLConnection.setDoInput(true);
        mHttpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        mHttpURLConnection.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));
        OutputStream os = mHttpURLConnection.getOutputStream();
        os.write(params.getBytes());
        os.flush();
        os.close();
    }


    protected String initBody() {
        if (mParams != null) {
            StringBuilder builder = new StringBuilder();
            for (Pair<String, String> param : mParams) {
                builder.append(param.first);
                builder.append("=").append(param.second);
                builder.append("&");
            }
            return builder.toString();
        }
        return null;
    }


    @Override
    protected void doException(Exception exception, int errCode) {
        setError(errCode);
        setData(null);
    }
}
