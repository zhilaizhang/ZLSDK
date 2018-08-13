package com.zlzhang.ble;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/8/2.
 */
public class FileDoWith {
    private static FileDoWith fileDoWith = null;

    public static FileDoWith getInstall() {
        if (fileDoWith == null) {
            fileDoWith = new FileDoWith();
        }
        return fileDoWith;
    }

    private String filePath = Environment.getExternalStorageDirectory() + "/idsbeacon/";

    public void saveBeacon(String filename, String beaconStr) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(filePath, filename);
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(beaconStr.getBytes());
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
