package com.zlzhang.client.util;

import com.zlzhang.client.base.Action;

/**
 * Created by zhangzhilai on 2017/11/24.
 */

public class ActionUtil {

    public static void changeEnvironment(boolean isTest){
        if (isTest) {
            Action.BASE_URL = "";//测试
        } else {
            Action.BASE_URL = "";//shang-20170904-正式
        }
    }


    public static void setUrlAndPort(String url, int port){
        Action.AP_STA_URL = "http://" + url;
        Action.AP_STA_PORT = port;
    }


}
