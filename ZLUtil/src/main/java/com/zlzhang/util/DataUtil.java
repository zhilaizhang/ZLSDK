package com.zlzhang.util;

/**
 * Created by zhilaizhang on 17/7/8.
 * decimal-binary conversion
 */

public class DataUtil {

    /**
     * 将short转成byte[2]
     *
     * @param a
     * @return
     */
    public static byte[] short2Byte(short a) {
        byte[] b = new byte[2];
        b[0] = (byte) (a >> 8);
        b[1] = (byte) (a);
        return b;
    }

    /**
     * 将byte[2]转换成short
     *
     * @param b
     * @return
     */
    public static short byte2Short(byte[] b) {
        return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
    }

    /**
     * byte[8]转long
     *
     * @param b
     * @return
     */
    public static long byte2Long(byte[] b) {
        return
                ((long) (b[0] & 0xff) << 56) |
                        (((long) b[1] & 0xff) << 48) |
                        (((long) b[2] & 0xff) << 40) |
                        (((long) b[3] & 0xff) << 32) |

                        (((long) b[4] & 0xff) << 24) |
                        (((long) b[5] & 0xff) << 16) |
                        (((long) b[6] & 0xff) << 8) |
                        ((long) b[7] & 0xff);
    }

    /**
     * byte[8]转double
     *
     * @param arr
     * @return
     */
    public static double byte2Double(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    /**
     * double转byte[8]
     *
     * @param d
     * @return
     */
    public static byte[] double2Bytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
    }

    /**
     * long转byte[8]
     *
     * @param a
     * @return
     */
    public static byte[] long2Byte(long a) {
        byte[] b = new byte[4 * 2];

        b[0] = (byte) ((a >> 56) & 0xff);
        b[1] = (byte) ((a >> 48) & 0xff);
        b[2] = (byte) ((a >> 40) & 0xff);
        b[3] = (byte) ((a >> 32) & 0xff);

        b[4] = (byte) ((a >> 24) & 0xff);
        b[5] = (byte) ((a >> 16) & 0xff);
        b[6] = (byte) ((a >> 8) & 0xff);
        b[7] = (byte) ((a >> 0) & 0xff);

        return b;
    }

    /**
     * byte数组转int
     *
     * @param b
     * @return
     */
    public static int byte2Int(byte[] b) {
        return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16)
                | ((b[2] & 0xff) << 8) | (b[3] & 0xff);
    }

    /**
     * int转byte数组
     *
     * @param a
     * @return
     */
    public static byte[] int2Byte(int a) {
        byte[] b = new byte[4];
        b[0] = (byte) (a >> 24);
        b[1] = (byte) (a >> 16);
        b[2] = (byte) (a >> 8);
        b[3] = (byte) (a);
        return b;
    }

    /**
     * 将byte数组转为16进制数
     *
     * @param b
     * @return
     */
    public static String bytes2Hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * 将byte转为16进制数
     *
     * @param b
     * @return
     */
    public static String byte2Hex(byte b) {
        byte[] bytes = new byte[1];
        bytes[0] = b;
        return bytes2Hex(bytes);
    }

    /**
     * 将int转为16进制数
     *
     * @param intData
     * @return
     */
    public static String int2Hex(int intData) {
        byte[] intBytes = int2Byte(intData);
        return bytes2Hex(intBytes);
    }

    /**
     * 将long转为16进制数
     *
     * @param longData
     * @return
     */
    public static String long2Hex(long longData) {
        byte[] longBytes = long2Byte(longData);
        return bytes2Hex(longBytes);
    }

    /**
     * 将short转为16进制数
     *
     * @param shortData
     * @return
     */
    public static String short2Hex(short shortData) {
        byte[] shortBytes = short2Byte(shortData);
        return bytes2Hex(shortBytes);
    }

    /**
     * @param b
     * @return
     */
    public static String byte2Bit(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) +
                (byte) ((b >> 6) & 0x1) +
                (byte) ((b >> 5) & 0x1) +
                (byte) ((b >> 4) & 0x1) +
                (byte) ((b >> 3) & 0x1) +
                (byte) ((b >> 2) & 0x1) +
                (byte) ((b >> 1) & 0x1) +
                (byte) ((b >> 0) & 0x1);
    }

    public static byte[] byte2BitArray(byte b){
        byte[] bytes = new byte[8];
        bytes[0] = (byte) ((b >> 7) & 0x1);
        bytes[1] = (byte) ((b >> 6) & 0x1);
        bytes[2] = (byte) ((b >> 5) & 0x1);
        bytes[3] = (byte) ((b >> 4) & 0x1);
        bytes[4] = (byte) ((b >> 3) & 0x1);
        bytes[5] = (byte) ((b >> 2) & 0x1);
        bytes[6] = (byte) ((b >> 1) & 0x1);
        bytes[7] = (byte) ((b >> 0) & 0x1);
        return bytes;
    }

    /**
     * @param shortData
     * @return
     */
    public static String short2Bit(short shortData) {
        byte[] bytes = short2Byte(shortData);
        String highBytes = byte2Bit(bytes[0]);
        String lowBytes = byte2Bit(bytes[1]);
        return highBytes + lowBytes;
    }

    public static byte[] short2Bit(short shortData, int length){
        byte[] bytes = short2Byte(shortData);
        byte[] highBytes = byte2BitArray(bytes[0]);
        byte[] lowBytes = byte2BitArray(bytes[1]);
        byte[] tempBytes = new byte[16];
        for(int i = 0; i < 8; i++){
            tempBytes[i] = highBytes[i];
            tempBytes[8 + i] = lowBytes[i];
        }
        byte[] shortBits = new byte[length];
        int j = 0;
        for(int i = 16 - length; i < length; i++){
            shortBits[j++] = tempBytes[i];
        }
        return shortBits;
    }

    /**
     *
     * @param byteStr
     * @return
     */
    public static byte Bit2Byte(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {//4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }


    /**
     * 将字母转换成数字
     *
     * @param input
     */
    public static void letterToNum(String input) {
        for (byte b : input.getBytes()) {
            System.out.print(b - 96);
        }
    }

    /**
     * 将数字转换成字母
     *
     * @param input
     */
    public static void numToLetter(String input) {
        for (byte b : input.getBytes()) {
            System.out.print((char) (b + 48));
        }
    }
}
