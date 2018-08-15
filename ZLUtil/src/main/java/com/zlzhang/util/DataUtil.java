package com.zlzhang.util;

import java.math.BigDecimal;
import java.util.List;

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
     * <p>List 转化为 数组</p>
     *
     * @param list  in
     * @param bytes out
     */
    public static void bytes2List(List<Byte> list, byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            list.add(bytes[i]);
        }
    }

    /**
     * <p>byte数组转化为int</p>
     *
     * @param source byte数组 数组低字节在前
     * @param begin
     * @return
     */
    public static int byte2Int(byte[] source, int begin) {
        int iOutcome = 0;
        iOutcome |= (source[begin++] & 0xFF);
        iOutcome |= ((source[begin++] & 0xFF) << 8);
        iOutcome |= ((source[begin++] & 0xFF) << 16);
        iOutcome |= ((source[begin++] & 0xFF) << 24);
        return iOutcome;
    }

    /**
     * <p>byte数组转化为int</p>
     *
     * @param source byte数组 数组低字节在前
     * @param begin
     * @return
     */
    public static int byte2IntLowBefore(byte[] source, int begin) {
        return byte2Int(source, begin);
    }


    /**
     * <p>byte数组转化为int</p>
     *
     * @param source byte数组 数组高字节在前
     * @param begin
     * @return
     */
    public static int byte2IntHighBefore(byte[] source, int begin) {
        int iOutcome = 0;
        iOutcome |= ((source[begin++] & 0xFF) << 24);
        iOutcome |= ((source[begin++] & 0xFF) << 16);
        iOutcome |= ((source[begin++] & 0xFF) << 8);
        iOutcome |= (source[begin++] & 0xFF);
        return iOutcome;
    }

    /**
     * 2个byte转int
     *
     * @param data
     * @return
     */
    public static int twoByteToInt(byte[] data) {
        return twoByteToInt(data[0], data[1]);
    }

    /**
     * <P>byte数组转化为byte----ron</P>
     *
     * @param b
     * @param index 低字节在前
     * @return
     */
    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * 2个byte转int
     *
     * @param
     * @return
     */
    public static int twoByteToInt(byte data0, byte data1) {
        int var = data1 << 8 | byte2UnsignedByte(data0);
        if (var < 0) {
            var += 65536;
        }
        return var;
    }

    /**
     * 获取无符号byte
     *
     * @param var0
     * @return
     */
    public static int byte2UnsignedByte(byte var0) {
        int var1 = var0;
        if (var0 < 0) {
            var1 = var0 + 256;
        }
        return var1;
    }

    /**
     * <p>byte数组转化为long</p>
     *
     * @param source byte数组 数组低字节在前
     * @return
     */
    public static long byte2long(byte[] source, int begin) {
        long iOutcome = 0;
        iOutcome |= (source[begin++] & 0xFF);
        iOutcome |= ((source[begin++] & 0xFF) << 8);
        iOutcome |= ((source[begin++] & 0xFF) << 16);
        iOutcome |= ((source[begin++] & 0xFF) << 24);
        iOutcome |= ((source[begin++] & 0xFF) << 32);
        iOutcome |= ((source[begin++] & 0xFF) << 40);
        iOutcome |= ((source[begin++] & 0xFF) << 48);
        iOutcome |= ((source[begin++] & 0xFF) << 56);
        return iOutcome;
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


    public static long byteToLong(byte[] b, int begin) {
        long s = 0;
        long s0 = b[begin++] & 0xff;// 最低位
        long s1 = b[begin++] & 0xff;
        long s2 = b[begin++] & 0xff;
        long s3 = b[begin++] & 0xff;
        long s4 = b[begin++] & 0xff;// 最低位
        long s5 = b[begin++] & 0xff;
        long s6 = b[begin++] & 0xff;
        long s7 = b[begin++] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * <p>int转化为byte</p>
     *
     * @param source
     * @return 数组低字节在前
     */
    public static byte[] int2byte(int source) {
        byte[] bLocalArr = new byte[4];
        long lSource = source & 0xFFFFFFFFL;
        int i = 0;
        bLocalArr[i++] = (byte) ((lSource >> 0) & 0xFF);
        bLocalArr[i++] = (byte) ((lSource >> 8) & 0xFF);
        bLocalArr[i++] = (byte) ((lSource >> 16) & 0xFF);
        bLocalArr[i++] = (byte) ((lSource >> 24) & 0xFF);
        return bLocalArr;
    }

    /**
     * <p>long转化为byte</p>
     *
     * @param source
     * @return 数组低字节在前
     */
    public static byte[] long2byte(long source) {
        byte[] bLocalArr = new byte[8];
        long lSource = source & 0xFFFFFFFFL;
        int i = 0;
        bLocalArr[i++] = (byte) ((lSource >> 0) & 0xFF);
        bLocalArr[i++] = (byte) ((lSource >> 8) & 0xFF);
        bLocalArr[i++] = (byte) ((lSource >> 16) & 0xFF);
        bLocalArr[i++] = (byte) ((lSource >> 24) & 0xFF);
        bLocalArr[i++] = (byte) ((lSource >> 32) & 0xFF);
        bLocalArr[i++] = (byte) ((lSource >> 40) & 0xFF);
        bLocalArr[i++] = (byte) ((lSource >> 48) & 0xFF);
        bLocalArr[i++] = (byte) ((lSource >> 56) & 0xFF);
        return bLocalArr;
    }

    /**
     * <p>short转化为字节数组</p>
     *
     * @param source
     * @return 数组 低字节在前
     */
    public static byte[] short2byte(short source) {
        int iSource = source & 0xFFFF;
        byte[] bb = new byte[2];
        bb[0] = (byte) ((iSource >> 0) & 0xFF);
        bb[1] = (byte) ((iSource >> 8) & 0xFF);
        return bb;
    }

    /**
     * <p>byte数组 转化为 short</p>
     *
     * @param bb    数组低字节在前
     * @param index
     * @return
     */
    public static short byte2short(byte[] bb, int index) {
        short sOutcome = 0;
        sOutcome |= ((bb[index++] & 0xFF) << 0);
        sOutcome |= ((bb[index++] & 0xFF) << 8);

        return sOutcome;
    }


    /**
     * <p>byte数组 转化为 short</p>
     *
     * @param bb    数组低字节在前
     * @param index
     * @return
     */
    public static short byte2shortLowBefore(byte[] bb, int index) {
        return byte2short(bb, index);
    }

    /**
     * <p>byte数组 转化为 short</p>
     *
     * @param bb    数组低字节在前
     * @param index
     * @return
     */
    public static short byte2shortHighBefore(byte[] bb, int index) {
        short sOutcome = 0;
        sOutcome |= ((bb[index++] & 0xFF) << 8);
        sOutcome |= ((bb[index++] & 0xFF) << 0);

        return sOutcome;
    }

    /**
     * <h3>把String转化为byte类型</h3>
     * <ul>
     * <li>由于str.getBytes 不包含结尾符号0</li>
     * </ul>
     *
     * @param str
     * @return
     */
    public static byte[] str2Byte(String str) {
        byte[] bb = str.getBytes();
        byte[] ids = new byte[bb.length + 1];
        System.arraycopy(bb, 0, ids, 0, bb.length);
        return ids;
    }

    public static char byte2Char(byte b) {
        char c = (char) b;
        return c;
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
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
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
     * <P>shang</P>
     * <P>安全无公害 数据类型格式化</P>
     *
     * @param values
     * @return
     */
    public static float BigDecimal(double values) {
        BigDecimal b = new BigDecimal(values);
        float onedecimals = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return onedecimals;
    }

    public static float BigDecimal(double values, int i) {
        BigDecimal b = new BigDecimal(values);
        float onedecimals = b.setScale(i, BigDecimal.ROUND_HALF_UP).floatValue();
        return onedecimals;
    }

    /**
     * @author yuhao
     * 取出byte的每一位
     */
    public static byte[] getBit(byte byData) {
        byte[] ints = new byte[8];
        byte one = (byte) 0x01;
        byte zero = (byte) 0x00;
        ints[0] = (byData & 0x01) == 0x01 ? one : zero;
        ints[1] = (byData & 0x02) == 0x02 ? one : zero;
        ints[2] = (byData & 0x04) == 0x04 ? one : zero;
        ints[3] = (byData & 0x08) == 0x08 ? one : zero;
        ints[4] = (byData & 0x10) == 0x10 ? one : zero;
        ints[5] = (byData & 0x20) == 0x20 ? one : zero;
        ints[6] = (byData & 0x40) == 0x40 ? one : zero;
        ints[7] = (byData & 0x80) == 0x80 ? one : zero;
        return ints;
    }

    /**
     * int 类型转为32位 bit
     * @param source
     * @return
     */
    public static byte[] int2Bit(int source) {
        byte[] bits = new byte[32];
        byte one = 1;
        byte zero = 0;
        byte[] bytes = int2byte(source);
        for (int i = 0; i < bytes.length; i++) {
            bits[i * 8 + 0] = (bytes[i] & 1) == 1 ? one : zero;
            bits[i * 8 + 1] = (bytes[i] & 2) == 2 ? one : zero;
            bits[i * 8 + 2] = (bytes[i] & 4) == 4 ? one : zero;
            bits[i * 8 + 3] = (bytes[i] & 8) == 8 ? one : zero;
            bits[i * 8 + 4] = (bytes[i] & 16) == 16 ? one : zero;
            bits[i * 8 + 5] = (bytes[i] & 32) == 32 ? one : zero;
            bits[i * 8 + 6] = (bytes[i] & 64) == 64 ? one : zero;
            bits[i * 8 + 7] = (bytes[i] & 128) == 128 ? one : zero;
        }
        return bits;
    }
}
