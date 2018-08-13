package com.zlzhang.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by zhilaizhang on 17/7/8.
 */

public class FileUtil {

    private static boolean flag;

    public static boolean isFileExist(String fileName) {
        if (fileName != null) {
            File file = new File(fileName);
            return file.exists();
        } else {
            return false;
        }
    }

    public static void deleteFile(String fileName) {
        if (fileName != null) {
            File file = new File(fileName);
            if (file != null && file.exists()) {
                if (file.isFile()) {
                    deleteFiles(fileName);
                } else {
                    deleteDirectory(fileName);
                }
            }
        }
    }

    public static boolean deleteFiles(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        if (files != null) for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFiles(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String         s      = reader.readLine();
                reader.close();
                return s;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getAllFile(String path){
        File file = new File(path);
        String allS = "";
        if (file.exists() && file.isFile()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String         s      = null;
                while((s = reader.readLine()) != null){
                    allS += s + "\n";
                }
                reader.close();
                return allS;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static long setRawDataToFile(String strPath, String data) {
        long wrSize = 0;
        if (data != null && data.length() > 0 && strPath != null) {
            if (strPath.endsWith(File.separator)) {
                return wrSize;
            }
            File file = new File(strPath);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    return wrSize;
                }
            }

            try {
                boolean bFileWritable = true;
                if (file.exists() && file.isFile()) {
                    bFileWritable = file.delete();
                }
                if (bFileWritable) {
                    PrintWriter pw = new PrintWriter(new FileOutputStream(file));
                    pw.println(data);
                    pw.close();
                    wrSize = file.length();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wrSize;
    }
}
