//package com.zlzhang.ble.util;
//
//import com.ids.bls.IDSBeaconDTO;
//import com.ids.device.util.SNEncryptor;
//import com.ids.device.util.SNGenerator;
//import com.ids.model.dev.type.StarVersion;
//import com.ids.model.map.RSSIRecord;
//import com.ids.model.pgm.sdk.impl.IDSBeacon;
//import com.ids.model.pgm.sdk.impl.IDSRssi;
//import com.ids.util.android.DataUtil;
//import com.ids.util.android.LogHelper;
//
//import java.io.IOException;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.Hashtable;
//import java.util.LinkedList;
//import java.util.List;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//
///**
// * Created by sagnitude on 2016/10/9.
// */
//public class BLSUtils {
//
//    private static final String TAG = "BLSUtils";
//
//    public static String ummKey(IDSBeacon beacon) {
//        return (beacon.getProximityUuid() + "," + beacon.getMajor() + "," + beacon.getMinor()).toLowerCase();
//    }
//
//    public static String ummKey(String uuid, int major, int minor) {
//        return (uuid + "," + major + "," + minor).toLowerCase();
//    }
//
//    public static IDSRssi parse(byte[] scanRecord, int rssi) {
//        IDSRssi idsRssi = null;
//        if (scanRecord == null) {
//            return idsRssi;
//        }
//        int     index   = 0;
//        int     maxLen  = scanRecord.length < 31 ? scanRecord.length : 31;
//        idsRssi = new IDSRssi();
//        byte[] rssiData    = null;
//        byte[] serviceUUID = null;
//        byte[] beaconUUID  = null;
//        while (index < maxLen) {
//            int l = scanRecord[index];
//            if (l == 0 || index + l >= maxLen) {
//                break;
//            } else {
//                int type = scanRecord[index + 1];
//                if (type == 0x02 && l == 3) {
//                    serviceUUID = new byte[2];
//                    serviceUUID[0] = scanRecord[index + 2];
//                    serviceUUID[1] = scanRecord[index + 3];
//                } else if (type == 0x06 && l == 17) {
//                    beaconUUID = new byte[16];
//                    for (int i = 0; i < 16; i++) {
//                        beaconUUID[i] = scanRecord[index + 2 + i];
//                    }
//                }
//            }
//            index += (l + 1);
//        }
//        rssiData = new byte[18];
//        if (serviceUUID != null && beaconUUID != null) {
//            rssiData[0] = serviceUUID[0];
//            rssiData[1] = serviceUUID[1];
//            for (int i = 0; i < 16; i++) {
//                rssiData[i + 2] = beaconUUID[i];
//            }
//        } else {
//            return null;
//        }
//        idsRssi.setData(rssiData);
//        idsRssi.setRssi(rssi);
//        return idsRssi;
//    }
//
//    public static List<IDSBeaconDTO> filtBeacons(Hashtable<String, List<IDSBeaconDTO>> beacons) {
//        if (beacons != null) {
//            List<IDSBeaconDTO> list = new LinkedList<IDSBeaconDTO>();
//
//            for (String sn : beacons.keySet()) {
//                List<IDSBeaconDTO> rssiLocArray = beacons.get(sn);
//
//                // RSSIRecord rssi = karlmanFilter(rssiLocArray);
//                IDSBeaconDTO rssi = avgFilter3(rssiLocArray);
//                IDSBeaconDTO first = rssiLocArray.get(0);
//
//                if (rssi != null && rssi.getIdsBeacon() != null) {
//                    rssi.setName(first.getName());
//                    rssi.setAddress(first.getAddress());
//                    rssi.setUmmKey(sn);
//                    rssi.getIdsBeacon().setMajor(first.getIdsBeacon().getMajor());
//                    rssi.getIdsBeacon().setMinor(first.getIdsBeacon().getMinor());
//                    rssi.getIdsBeacon().setBluetoothAddress(first.getIdsBeacon().getBluetoothAddress());
//                    list.add(rssi);
//                }
//            }
//
//            Collections.sort(list, new Comparator<IDSBeaconDTO>() {
//                public int compare(IDSBeaconDTO mp1, IDSBeaconDTO mp2) {
//                    // signal strength to decide it
//                    return (mp2.getIdsBeacon().getRssi() > mp1.getIdsBeacon().getRssi()) ? 1 : ((mp2.getIdsBeacon().getRssi() < mp1.getIdsBeacon().getRssi()) ? -1 : 0);
//                }
//            });
//
//            return list;
//        } else {
//            return null;
//        }
//    }
//
//    public static List<RSSIRecord> filtRSSIs(Hashtable<Long, List<RSSIRecord>> rssis) {
//        if (rssis != null) {
//            List<RSSIRecord> list = new LinkedList<RSSIRecord>();
//
//            for (long sn : rssis.keySet()) {
//                List<RSSIRecord> rssiLocArray = rssis.get(sn);
//
//                // RSSIRecord rssi = karlmanFilter(rssiLocArray);
//                RSSIRecord rssi = avgFilter2(rssiLocArray);
//
//                if (rssi != null) {
//                    if (rssiLocArray.size() > 0) {
//                        rssi.setData(rssiLocArray.get(0).getData());
//                    }
//                    rssi.setStarSn(sn);
//                    rssi.setDate(new Date());
//                    list.add(rssi);
//                }
//            }
//
//            Collections.sort(list, new Comparator<RSSIRecord>() {
//                public int compare(RSSIRecord mp1, RSSIRecord mp2) {
//                    // signal strength to decide it
//                    return (mp2.getRssi() > mp1.getRssi()) ? 1 : ((mp2.getRssi() < mp1.getRssi()) ? -1 : 0);
//                }
//            });
//
//            return list;
//        } else {
//            return null;
//        }
//    }
//
//    private final static int RSSI_ERROR_THRESHOLD = 15;
//
//    // with min and max values
//    private final static RSSIRecord avgFilter2(List<RSSIRecord> rssiLocArray) {
//        int max1 = Integer.MIN_VALUE;
//        int max2 = Integer.MIN_VALUE;
//        int max1Index = -1;
//        int max2Index = -1;
//        int sumRSSI = 0;
//        for (RSSIRecord rssiRecord : rssiLocArray) {
//            sumRSSI += rssiRecord.getRssi();
//        }
//
//        if (rssiLocArray.size() > 2) {
//            int avgRSSI = 0;
//            avgRSSI = sumRSSI / rssiLocArray.size();
//
//            for (int i = 0; i < rssiLocArray.size(); i++) {
//                RSSIRecord rssiRecord = rssiLocArray.get(i);
//                int v = Math.abs(avgRSSI - rssiRecord.getRssi());
//                if (v > RSSI_ERROR_THRESHOLD) {
//                    if (v > max1) {
//                        max2 = max1;
//                        max2Index = max1Index;
//                        max1 = v;
//                        max1Index = i;
//                    } else if (v > max2) {
//                        max2 = v;
//                        max2Index = i;
//                    }
//                }
//            }
//        }
//
//        int rssi = 0;
//        int minRSSI = Integer.MAX_VALUE;
//        int maxRSSI = Integer.MIN_VALUE;
//        int count = 0;
//        for (int i = 0; i < rssiLocArray.size(); i++) {
//            RSSIRecord rssiRecord = rssiLocArray.get(i);
//            rssi += rssiRecord.getRssi();
//            count++;
//            if (minRSSI > rssiRecord.getRssi()) {
//                minRSSI = rssiRecord.getRssi();
//            }
//            if (maxRSSI < rssiRecord.getRssi()) {
//                maxRSSI = rssiRecord.getRssi();
//            }
//        }
//
//        if (count > 0) {
//            rssi /= count;
//            int error = maxRSSI - minRSSI;
//            // error = DISTANCE(rssi + error / 2) - DISTANCE(rssi - error / 2);
//
//            if (error == 0) {
//                error = 1; // just one rssi record, we still need to make it valid
//            }
//
//            RSSIRecord bestRSSI = new RSSIRecord();
//            bestRSSI.setRssi(rssi);
//            return bestRSSI;
//        } else {
//            return null;
//        }
//    }
//
//    // with min and max values
//    private final static IDSBeaconDTO avgFilter3(List<IDSBeaconDTO> rssiLocArray) {
//        int max1 = Integer.MIN_VALUE;
//        int max2 = Integer.MIN_VALUE;
//        int max1Index = -1;
//        int max2Index = -1;
//        int sumRSSI = 0;
//        for (IDSBeaconDTO rssiRecord : rssiLocArray) {
//            sumRSSI += rssiRecord.getIdsBeacon().getRssi();
//        }
//
//        if (rssiLocArray.size() > 2) {
//            int avgRSSI = 0;
//            avgRSSI = sumRSSI / rssiLocArray.size();
//
//            for (int i = 0; i < rssiLocArray.size(); i++) {
//                IDSBeaconDTO rssiRecord = rssiLocArray.get(i);
//                if (rssiRecord.getIdsBeacon() == null) {
//                    continue;
//                }
//                int v = Math.abs(avgRSSI - rssiRecord.getIdsBeacon().getRssi());
//                if (v > RSSI_ERROR_THRESHOLD) {
//                    if (v > max1) {
//                        max2 = max1;
//                        max2Index = max1Index;
//                        max1 = v;
//                        max1Index = i;
//                    } else if (v > max2) {
//                        max2 = v;
//                        max2Index = i;
//                    }
//                }
//            }
//        }
//
//        int rssi = 0;
//        int minRSSI = Integer.MAX_VALUE;
//        int maxRSSI = Integer.MIN_VALUE;
//        int count = 0;
//        for (int i = 0; i < rssiLocArray.size(); i++) {
//            IDSBeaconDTO rssiRecord = rssiLocArray.get(i);
//            rssi += rssiRecord.getIdsBeacon().getRssi();
//            count++;
//            if (minRSSI > rssiRecord.getIdsBeacon().getRssi()) {
//                minRSSI = rssiRecord.getIdsBeacon().getRssi();
//            }
//            if (maxRSSI < rssiRecord.getIdsBeacon().getRssi()) {
//                maxRSSI = rssiRecord.getIdsBeacon().getRssi();
//            }
//        }
//
//        if (count > 0) {
//            rssi /= count;
//            int error = maxRSSI - minRSSI;
//            // error = DISTANCE(rssi + error / 2) - DISTANCE(rssi - error / 2);
//
//            if (error == 0) {
//                error = 1; // just one rssi record, we still need to make it valid
//            }
//
//            IDSBeaconDTO bestRSSI = new IDSBeaconDTO();
//            IDSBeacon beacon = new IDSBeacon();
//            bestRSSI.setIdsBeacon(beacon);
//            bestRSSI.getIdsBeacon().setRssi(rssi);
//            return bestRSSI;
//        } else {
//            return null;
//        }
//    }
//
//    public static byte[] getCityIdAndMallIdByte(int cityId, int mallId, int measuredPower, long starSn) {
//        byte[] mcmsBytes = null;
//        SNEncryptor snEncryptor = new SNEncryptor();
//        snEncryptor.setCityId(cityId);
//        snEncryptor.setMallId(mallId);
//        snEncryptor.setMeasuredPower(measuredPower);
//        snEncryptor.setStarSn(starSn);
//        snEncryptor.setStarVersion(StarVersion.IDS_BEACON_40.getValue());
//        try {
//            snEncryptor.encrypt(5);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            LogHelper.d(TAG, "test NoSuchAlgorithmException" + e.getMessage());
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//            LogHelper.d(TAG, "test NoSuchPaddingException" + e.getMessage());
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//            LogHelper.d(TAG, "test InvalidKeyException" + e.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//            LogHelper.d(TAG, "test IOException" + e.getMessage());
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//            LogHelper.d(TAG, "test IllegalBlockSizeException" + e.getMessage());
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//            LogHelper.d(TAG, "test BadPaddingException" + e.getMessage());
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//            LogHelper.d(TAG, "test InvalidAlgorithmParameterException" + e.getMessage());
//        } catch (Exception e) {
//            LogHelper.d(TAG, "test Exception" + e.getMessage());
//        }
//        byte[] beaconByte = snEncryptor.getBeaconUUID();
//        if (beaconByte != null) {
//            mcmsBytes = new byte[17];
//            for (int i = 0; i < 16; i++) {
//                byte b = beaconByte[i];
//                if (b < 0) {
//                    mcmsBytes[i] = (byte) (256 + b);
//                } else {
//                    mcmsBytes[i] = b;
//                }
//            }
//            mcmsBytes[16] = 5;
//        }
//        return mcmsBytes;
//    }
//
//    private static byte[] parseBytes(byte[] tempBytes){
//        byte[] resultBytes = null;
//        if (tempBytes != null) {
//            int length = tempBytes.length;
//            resultBytes = new byte[length];
//            for (int i = 0; i < length; i++) {
//                byte b = tempBytes[i];
//                if(b < 0){
//                    resultBytes[i] = (byte)(256 + b);
//                } else {
//                    resultBytes[i] = b;
//                }
//                LogHelper.d(TAG, "test byte: " + b);
//            }
//        }
//        return resultBytes;
//    }
//
////    public static byte[] getCityByte(long starSn, int cityId){
////        SNEncryptor snEncryptor = new SNEncryptor();
////        snEncryptor.encryptInt(0, starSn, cityId);
////        byte[] tempBytes = snEncryptor.getEncryptBytes();
////        return parseBytes(tempBytes);
////    }
////
////    public static byte[] getMallIdByte(long starSn, int mallId){
////        SNEncryptor snEncryptor = new SNEncryptor();
////        snEncryptor.encryptInt(0, starSn, mallId);
////        byte[] tempBytes = snEncryptor.getEncryptBytes();
////        return parseBytes(tempBytes);
////    }
////
////    public static byte[] getStarSnByte(long oldStarSn, long newStarSn){
////        SNEncryptor snEncryptor = new SNEncryptor();
////        snEncryptor.encryptLong(0, oldStarSn, newStarSn);
////        byte[] tempBytes = snEncryptor.getEncryptBytes();
////        return parseBytes(tempBytes);
////    }
////    public static byte[] getAdIntervalByte(long starSn, short adInterval){
////        SNEncryptor snEncryptor = new SNEncryptor();
////        snEncryptor.encryptShort(0, starSn, adInterval);
////        byte[] tempBytes = snEncryptor.getEncryptBytes();
////        return parseBytes(tempBytes);
////    }
////    public static byte[] getTxPowerByte(long starSn, byte txPower){
////        SNEncryptor snEncryptor = new SNEncryptor();
////        byte[] bytes = new byte[1];
////        bytes[0] = txPower;
////        snEncryptor.encryptBytes(0, starSn, bytes);
////        byte[] tempBytes = snEncryptor.getEncryptBytes();
////        return parseBytes(tempBytes);
////    }
////    public static byte[] getAdModeByte(long starSn, byte[] adMode){
////        SNEncryptor snEncryptor = new SNEncryptor();
////        snEncryptor.encryptBytes(0, starSn, adMode);
////        byte[] tempBytes = snEncryptor.getEncryptBytes();
////        return parseBytes(tempBytes);
////    }
////    public static byte[] getMeasurePowerByte(long starSn, byte measurePower){
////        SNEncryptor snEncryptor = new SNEncryptor();
////        byte[] bytes = new byte[1];
////        bytes[0] = measurePower;
////        snEncryptor.encryptBytes(0, starSn, bytes);
////        byte[] tempBytes = snEncryptor.getEncryptBytes();
////        return parseBytes(tempBytes);
////    }
//
//    public static byte[] getIntBytes(long starSn, int intData){
//        SNEncryptor snEncryptor = new SNEncryptor();
//        snEncryptor.encryptInt(0, starSn, intData);
//        byte[] tempBytes = snEncryptor.getEncryptBytes();
//        return parseBytes(tempBytes);
//    }
//    public static byte[] getLongBytes(long starSn, long longData){
//        SNEncryptor snEncryptor = new SNEncryptor();
//        snEncryptor.encryptLong(0, starSn, longData);
//        byte[] tempBytes = snEncryptor.getEncryptBytes();
//        return parseBytes(tempBytes);
//    }
//    public static byte[] getShortBytes(long starSn, short shortData){
//        SNEncryptor snEncryptor = new SNEncryptor();
//        snEncryptor.encryptShort(0, starSn, shortData);
//        byte[] tempBytes = snEncryptor.getEncryptBytes();
//        return parseBytes(tempBytes);
//    }
//
//    public static byte[] getBytesBytes(long starSn, byte[] bytesData){
//        SNEncryptor snEncryptor = new SNEncryptor();
//        snEncryptor.encryptBytes(5, starSn, bytesData);
//        byte[] tempBytes = snEncryptor.getEncryptBytes();
//        return parseBytes(tempBytes);
//    }
//
//    public static byte[] getByteBytes(long starSn, byte byteData){
//        byte[] bytes = new byte[1];
//        bytes[0] = byteData;
//        return getBytesBytes(starSn, bytes);
//    }
//
//    public static byte[] getAdIntervalSnByte(long sn, short adInterval) {
//        byte[] adIntervalSnByte;
//        byte[] snByte = DataUtil.long2Byte(sn);
//        adIntervalSnByte = new byte[10];
//        byte[] adIntervalByte = DataUtil.short2Byte(adInterval);
//        adIntervalSnByte[0] = adIntervalByte[1];
//        adIntervalSnByte[1] = adIntervalByte[0];
//        for (int i = 0; i < 8; i++) {
//            adIntervalSnByte[i + 2] = snByte[i];
//        }
//        return adIntervalSnByte;
//    }
//
//    public static byte[] getRfPowerSnByte(long sn, byte rfPower) {
//        byte[] rfPowerSnByte;
//        byte[] snByte = DataUtil.long2Byte(sn);
//        rfPowerSnByte = new byte[9];
//        rfPowerSnByte[0] = rfPower;
//        for (int i = 0; i < 8; i++) {
//            rfPowerSnByte[i + 1] = snByte[i];
//        }
//        return rfPowerSnByte;
//    }
//}
