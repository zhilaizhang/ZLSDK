///*
// * ==============================BEGIN_COPYRIGHT===============================
// * =======Wangluo Electronic Technology Co., LTD. PROPRIETARY INFORMATION======
// * This software and its associated IndoorStar products are supplied under the
// * terms of a license agreement or nondisclosure agreement (NDA) with Wangluo
// * Electronic Technology Co., LTD., Shanghai and may not be copied or disclosed
// * except in accordance with the terms of that agreement.
// * Copyright (c) 2013 - 2015
// * Wangluo Electronic Technology Co., LTD., Shanghai. All Rights Reserved.
// * ===============================END_COPYRIGHT================================
// *
// * @author - IDS R&D Group - Patrick Zhang
// * @date   - 2016-05-25 23:13
// */
//
//package com.zlzhang.ble.impl;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.Context;
//
//import com.zlzhang.ble.ModifyManager;
//import com.zlzhang.ble.conn.BLEModifyListener;
//import com.zlzhang.ble.scan.BLEScanListener;
//import com.zlzhang.util.LogHelper;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.Executors;
//import java.util.concurrent.RejectedExecutionException;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//
//import static com.zlzhang.ble.util.BLSUtils.filtRSSIs;
//
//
///**
// * Created by zhilaizhang on 5/25/16.
// */
//public class BLEModifyImpl extends ModifyManager implements BLEScanListener{
//
//    private final static String LOG_TAG = "[BLS]";
//
//    private Context mContext;
//
//    private final static int  PARKING_SCAN             = 0x100;
//    private final static int  NORMAL_SCAN              = 0x101;
//    private final static long DEFAULT_BLE_SCAN_SECONDS = 1;
//    private long mBLEScanMilliSeconds;
//    private int  mScanType;
//
//    private          SmtListener<List<RSSIRecord>>     mSmtListenerBLE;
//    private ModifyDeviceCentral mBleCentral;
//    private          SmtManagerState                   mBLECentralState;
//    private volatile Hashtable<Long, List<RSSIRecord>> mRSSILocs;
//    private volatile Hashtable<Long, SNEncryptor>      mSnEncryptors;
//
//    private static ScheduledExecutorService mBlsScheduler =
//            // In theory there would be only 2 locating threads in parallel at max
//            Executors.newScheduledThreadPool(2);
//
//    private ScheduledFuture mBLELocatingHandler = null;
//    private volatile boolean mIsBLELocating;
//
//    public BLEModifyImpl(Context context) {
//        mContext = context;
//        mBLECentralState = SmtManagerState.BLECentralStateUnsupported;
////        mBleCentral = new ModifyDeviceCentral(context, this);
//    }
//
//    private void cancelScheduledTask(ScheduledFuture<?> scheduler) {
//        if (scheduler != null) {
//            if (!scheduler.isDone()) {
//                // This attempt will fail if the task has already completed,
//                // has already been cancelled, or
//                // could not be cancelled for some other reason.
//                if (!scheduler.cancel(true)) // mayInterruptIfRunning == true
//                {
//                    LogHelper.e(LOG_TAG, String.format("Task (%s) cancellation failed!",
//                            scheduler.toString()));
//                }
//            }
//        }
//    }
//
//    @Override
//    public void didDiscoverDevice(BluetoothDevice device, int rssi, byte[] bytes) {
//        if (mIsBLELocating) {
//            Date now = new Date();
//            if (device != null) {
//                RSSIRecord rssiRecord = new RSSIRecord();
//                rssiRecord.setRssi(rssi);
//                rssiRecord.setDate(now);
//
//                int    bytelength = bytes.length;
//                String str        = device.getAddress();
//
//                byte[] addressByte = str.getBytes();
//                for (int i = 0; i < addressByte.length; i++) {
//                    bytes[bytelength - i - 1] = addressByte[i];
//                }
//                rssiRecord.setData(bytes);
//
//                long sn;
//                if (device.getName() != null && device.getName().equals("IDS")) {
//                    sn = handleIdsEncDevices(device, rssiRecord);
//                    if (sn == 0) {
//                        // bad decryption procedure
//                        sn = handleGeneralDevices(device, rssiRecord);
//                    }
//                } else {
//                    sn = handleGeneralDevices(device, rssiRecord);
//                }
//
//                rssiRecord.setStarSn(sn);
//
//                ScanInfo scanInfo = null;
////                if (blsDelegate != null) {
////                    scanInfo = blsDelegate.getScanInfo();
////                }
////                if (scanInfo != null && scanInfo.getType() == ScanType.IBEACON.getValue()) {
////                    // try to overwrite the sn
////                    sn = DevMgr.getBeaconSn(rssiRecord, scanInfo);
////                    LogHelper.d(TAG, "test sn: " + sn);
////                    rssiRecord.setStarSn(sn);
////                }
//
//                if (sLockForRssi.getLock()) {
//                    try {
//                        if (mRSSILocs.containsKey(sn)) {
//                            List<RSSIRecord> value = mRSSILocs.get(sn);
//                            value.add(rssiRecord);
//                        } else {
//                            List<RSSIRecord> value = new ArrayList<RSSIRecord>();
//                            value.add(rssiRecord);
//                            mRSSILocs.put(sn, value);
//                        }
//                    } catch (Exception e) {
//                        LogHelper.w(LOG_TAG, e.getMessage());
//                    } finally {
//                        sLockForRssi.putLock();
//                    }
//                }
//
//                if (mSmtListenerBLE != null) {
//                    mSmtListenerBLE.didDiscoverStar(SNGenerator.SnToString(sn), rssiRecord.getRssi());
//                }
//            }
//        }
//    }
//
//    public boolean modifyDeviceConfig(BLEModifyListener bleModifyListener, String macAddress, String UUID, int major,
//                                      int minor, int rfPower, int adInterval, long sn) {
//        return mBleCentral.modify(bleModifyListener, macAddress, UUID, major, minor, rfPower, adInterval, sn);
//    }
//
//    private long handleIdsEncDevices(BluetoothDevice device, RSSIRecord rssiRecord) {
//        SNEncryptor snEncryptor = new SNEncryptor();
//        byte[]      scanRecord  = rssiRecord.getData();
//        snEncryptor.parse(scanRecord);
//        long sn = snEncryptor.getStarSn();
//        if (sn > 0) {
//            if (sLockForIdsEnc.getLock()) {
//                try {
//                    if (!mSnEncryptors.containsKey(sn)) {
//                        mSnEncryptors.put(sn, snEncryptor);
//                    }
//                } catch (Exception e) {
//                    LogHelper.w(LOG_TAG, e.getMessage());
//                } finally {
//                    sLockForIdsEnc.putLock();
//                }
//            }
//        }
//        return sn;
//    }
//
//
//    private long handleGeneralDevices(BluetoothDevice device, RSSIRecord rssiRecord) {
//        String snStr = null;
//        if (device.getName() != null) {
//            snStr = SNGenerator.encode(device.getName());
//        } else if (device.getAddress() != null) {
//            snStr = device.getAddress();
//        } else {
//            // fake sn
//            snStr = UUID.randomUUID().toString();
//        }
//
//        if ((snStr != null && (snStr.length() == 0 || snStr.equals("JJJJJJJJ")))
//                || (device != null && device.getName() != null && device.getName().equals("IDS"))) {
//            IDSBeacon beacon = DevMgr.fromRssi(rssiRecord);
//            LogHelper.d("test", "test beacon uuid: " + beacon.getProximityUuid());
//            if (beacon != null && beacon.getProximityUuid().
//                    equalsIgnoreCase("a3fce438-627c-42b7-ab72-dc6e55e137ac")) {
//                // TODO: only for debugging purpose, our systems do not know such names
//                snStr = "IDS" + beacon.getMinor();
//            }
//        }
//
//        if (snStr != null && snStr.length() != 8) {
//            snStr = snStr.substring(0, (snStr.length() > 8) ? 8 : snStr.length());
//        }
//
//        //TODO: zqp tried using bluetoothDevice.getBluetoothClass().getDeviceClass();
//        // to get the service uuid; however, always 0x1F00 that means MISC
//        return SNGenerator.StringToSn(snStr);
//
//    }
//
//    @Override
//    public void didUpdateState(SmtManagerState state) {
//        mBLECentralState = state;
//    }
//
//    private static final SimpleDateFormat mDateFormater =
//            new SimpleDateFormat("HH:mm:ss.SSS");
//    // new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//
//    private static final LockUtil sLockForBle    = new LockUtil();
//    private static final LockUtil sLockForRssi   = new LockUtil();
//    private static final LockUtil sLockForIdsEnc = new LockUtil();
//
//
//    @Override
//    public void startScan(long starSn, long millTime) {
//
//    }
//
//    @Override
//    public void stopScan() {
//
//    }
//
//    @Override
//    public void connect(String macAddress) {
//
//    }
//
//    @Override
//    public void disConnect() {
//        mBleCentral.disConnect();
//    }
//
//    @Override
//    public void modify() {
//
//    }
//
//
//    @Override
//    public boolean isConnected(String address) {
//        return mBleCentral.isConnected(address);
//    }
//
//    public boolean startBLEScan(SmtListener<List<RSSIRecord>> smtListener, long scanSeconds, int scanType) {
//        mScanType = scanType;
//        return startBLEScan(smtListener, scanSeconds * 1.f);
//    }
//
//    public boolean startBLEScan(SmtListener<List<RSSIRecord>> smtListener, long scanSeconds) {
//        mScanType = NORMAL_SCAN;
//        return startBLEScan(smtListener, scanSeconds * 1.f);
//    }
//
//    protected boolean startBLEScan(SmtListener<List<RSSIRecord>> smtListener, float scanSeconds) {
//        if (!checkBLECentralState(smtListener)) {
//            if (smtListener != null) {
//                // Call finish to give the app a chance to finalize  things
//                smtListener.didFinishLocate();
//            }
//            return false;
//        } else {
//            mBLEScanMilliSeconds =
//                    (long) ((scanSeconds > 0 ? scanSeconds : DEFAULT_BLE_SCAN_SECONDS) * 1000);
//            if (mIsBLELocating) {
////                String lastBLEStartTime = (mLastBLEStartTime == null) ?
////                        "null" : mDateFormater.format(mLastBLEStartTime);
////                String lastBLEStopTime = (mLastBLEStopTime == null) ?
////                        "null" : mDateFormater.format(mLastBLEStopTime);
////                String currTime = mDateFormater.format(new Date());
////                // LogHelper.w(LOG_TAG, "Failed to start BLE locating because of non-stop");
////                LogHelper.w(LOG_TAG, String.format("non-stop, lastStart[%s] lastStop[%s] current[%s]",
////                        lastBLEStartTime, lastBLEStopTime, currTime));
//                return false;
//            } else {
//                if (sLockForBle.getLock()) {
//                    try {
//                        mSmtListenerBLE = smtListener;
//                        doBLELocating();
//                    } catch (Exception e) {
//                        LogHelper.w(LOG_TAG, e.getMessage());
//                    } finally {
//                        sLockForBle.putLock();
//                    }
//                    return true;
//                } else {
//                    LogHelper.w(LOG_TAG, String.format("Start-S - Execution skipped!"));
//                    return false;
//                }
//            }
//        }
//    }
//
//
//    private boolean checkBLECentralState(SmtListener<List<RSSIRecord>> smtListener) {
//        switch (mBLECentralState) {
//            case BLECentralStateUnknown:
//            case BLECentralStateUnsupported:
//            case BLECentralStateUnauthorized:
//                if (smtListener != null) {
//                    smtListener.didError(new SmtException(mBLECentralState));
//                }
//                return false;
//            case BLECentralStatePoweredOff:
//            case BLECentralStateResetting:
//            case BLECentralStatePoweredOn:
//                return checkBluetoothPower(smtListener);
//            default:
//                return false;
//        }
//    }
//
//    private boolean checkBluetoothPower(SmtListener<List<RSSIRecord>> smtListener) {
//        if (!isAdapterEnabled()) {
//            mBLECentralState = SmtManagerState.BLECentralStatePoweredOff;
//            if (smtListener != null) {
//                smtListener.didError(new SmtException(mBLECentralState));
//            }
//            return false;
//        } else {
//            mBLECentralState = SmtManagerState.BLECentralStatePoweredOn;
//            return true;
//        }
//    }
//
//    private boolean bAdapterDefaultWorking = false;
//    private boolean bAdapterCentralWorking = false;
//
//    private boolean isAdapterEnabled() {
//        boolean enable1 = false;
//        boolean enable2 = false;
//        try {
//            if (BluetoothAdapter.getDefaultAdapter() != null) {
//                enable1 = BluetoothAdapter.getDefaultAdapter().isEnabled();
//            }
//        } catch (Exception e) {
//            LogHelper.e(LOG_TAG, e.getMessage());
//        }
//        try {
//            if (mBleCentral != null) {
//                enable2 = mBleCentral.isEnabled();
//            }
////    } catch (DeadObjectException de) {
////      de.printStackTrace();
////      // LogHelper.e(LogHelper._FUNC_(), Util.getStackTrackString(de));
//        } catch (Throwable th) {
//            LogHelper.e(LOG_TAG, th.getMessage());
//        }
//        if (!enable1) {
//            LogHelper.w(LOG_TAG, "Warning: ble default adapter disabled");
//        }
//        bAdapterDefaultWorking = enable1;
//        if (!enable2) {
//            LogHelper.w(LOG_TAG, "Warning: ble center disabled");
//        }
//        bAdapterCentralWorking = enable2;
//        return (enable1 || enable2);
//    }
//
////    private Date mLastBLEStartTime = null;
////    private Date mLastBLEStopTime  = null;
//
//    private void doBLELocating() {
//        mRSSILocs = new Hashtable<Long, List<RSSIRecord>>();
//        mSnEncryptors = new Hashtable<Long, SNEncryptor>();
//
////        mLastBLEStartTime = new Date();
//        LogCurr("StartScan-0");
//        mIsBLELocating = true;
//
//        LogCurr("StartScan-1");
//        if (!bGlobalScan || !bGlobalRunning) {
//            LogCurr("StartScan-Central-Calling");
//            PerfCounter.start("StartScan-Central");
//            boolean bStarted = mBleCentral.scanForPeripheralsWithServices(null);
//            if (bGlobalScan && !bGlobalRunning && bStarted) {
//                LogCurr("StartScan(G)-Central-Called");
//                bGlobalRunning = true;
//            }
//            PerfCounter.stopAndLog("StartScan-Central");
//            LogCurr("StartScan-Central-Called" + String.format("[%b]", bStarted));
//        }
//        LogCurr("StartScan-2");
//
//        //TODO: need to tell if bStarted to decide if stop it immediately?
//        try {
//            mBLELocatingHandler = mBlsScheduler.schedule(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            stopBLELocating();
//                        }
//                    }, mBLEScanMilliSeconds, TimeUnit.MILLISECONDS);
//        } catch (RejectedExecutionException ree) {
//            LogHelper.e(LogHelper._FUNC_(), ree.getMessage());
//        }
//    }
//
//    private void LogCurr(String tag) {
//        LogHelper.i(LOG_TAG, String.format("%s: %s", tag,
//                mDateFormater.format(System.currentTimeMillis())));
//    }
//
//    private void stopBLELocating() {
//        if (sLockForBle.getLock()) {
//            try {
//                if (mIsBLELocating) {
//
//                    LogCurr("StopScan-1");
//                    if (!bGlobalScan) {
//                        LogCurr("StopScan-Central-Calling");
//                        mBleCentral.stopScan();
//                        LogCurr("StopScan-Central-Called");
//                    }
//                    LogCurr("StopScan-2");
//
//                    List<RSSIRecord> bestRSSIs = null;
//                    if (sLockForRssi.getLock(true)) {
//                        try {
//                            bestRSSIs = filtRSSIs(mRSSILocs);
//                        } catch (Exception e) {
//                            LogHelper.w(LOG_TAG, e.getMessage());
//                        } finally {
//                            sLockForRssi.putLock();
//                        }
//                    }
//
//                    if (bestRSSIs == null || bestRSSIs.size() == 0) {
//                        // sometimes Android BLE HW returns 0 RSSIs within in non-stop scanning method
//                        try {
//                            if (bGlobalScan && bGlobalRunning) {
//                                LogCurr("StopScan(G)-Central-Calling");
//                                mBleCentral.stopScan();
//                                LogCurr("StopScan(G)-Central-Called");
//                                bGlobalRunning = false;
//                            }
//                        } catch (Exception e) {
//                            LogHelper.w(LOG_TAG, e.getMessage());
//                        }
//                    }
//
//                    LogHelper.i(LOG_TAG, String.format("1"));
//
//                    try {
//                        if (mSmtListenerBLE != null) {
//                            decideCityMall();
//                            mSmtListenerBLE.didLocate(bestRSSIs);
//                            mSmtListenerBLE.didFinishLocate();
//                        }
//                    } catch (Exception e) {
//                        LogHelper.e(LOG_TAG, e.getMessage());
//                    }
//
//                    LogHelper.i(LOG_TAG, String.format("2"));
//
//                    cancelScheduledTask(mBLELocatingHandler);
//                    mBLELocatingHandler = null;
//
//                    mIsBLELocating = false;
//
//                    LogHelper.i(LOG_TAG, String.format("3"));
//
////                    mLastBLEStopTime = new Date();
////                    String lastBLEStopTime = mDateFormater.format(mLastBLEStopTime);
////                    LogHelper.i(LOG_TAG, String.format("StopScan-Done: %s", lastBLEStopTime));
//                }
//            } catch (Exception e) {
//                LogHelper.w(LOG_TAG, e.getMessage());
//            } finally {
//                if (mIsBLELocating) {
//                    mIsBLELocating = false;
//                    LogHelper.e(LOG_TAG, String.format("Recover ble stopping"));
//                }
//                sLockForBle.putLock();
//            }
//        } else {
//            LogHelper.w(LOG_TAG, String.format("Stop - Execution skipped!"));
//        }
//    }
//
//    private void decideCityMall() {
//        if (sLockForIdsEnc.getLock(true)) {
//            try {
//                if (mSnEncryptors != null && mSnEncryptors.size() > 0) {
//                    HashMap<Long, Integer> counter = new HashMap<Long, Integer>();
//                    long                   key;
//                    for (SNEncryptor snEncryptor : mSnEncryptors.values()) {
//                        key = snEncryptor.getCityId();
//                        key = (key << 32) + snEncryptor.getMallId();
//                        if (counter.containsKey(key)) {
//                            int cnt = counter.get(key);
//                            counter.put(key, cnt + 1);
//                        } else {
//                            counter.put(key, 1);
//                        }
//                    }
//                    List<Map.Entry<Long, Integer>> counts =
//                            new ArrayList<Map.Entry<Long, Integer>>(counter.entrySet());
//                    Collections.sort(counts, new Comparator<Map.Entry<Long, Integer>>() {
//                        public int compare(Map.Entry<Long, Integer> o1,
//                                           Map.Entry<Long, Integer> o2) {
//                            return Integer.compare(o2.getValue(), o1.getValue());
//                        }
//                    });
//
//                    Long id = counts.get(0).getKey();
//                    if (id > 0 && mSmtListenerBLE != null) {
//                        int mallId = (int) (id & 0xFFFF);
//                        int cityId = (int) ((id >> 32) & 0xFFFF);
//                        mSmtListenerBLE.didFoundCityMall("", cityId, mallId, -50);
//                    }
//
//                }
//            } catch (Throwable th) {
//                LogHelper.w(LOG_TAG, th.getMessage());
//            } finally {
//                sLockForIdsEnc.putLock();
//            }
//        }
//    }
//
//
//    @Override
//    public void release() {
//        stopBLELocating();
//
//        cancelScheduledTask(mBLELocatingHandler);
//        mBLELocatingHandler = null;
//
//        if (mBleCentral != null) {
//            mBleCentral.release();
//            mBleCentral = null;
//        }
//        super.release();
//    }
//
//}
