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
// * @date   - 2015-05-10 16:50
// */
//
//package com.zlzhang.ble.impl;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.Context;
//import android.os.Build;
//
//import com.zlzhang.ble.BLECentral;
//import com.zlzhang.ble.BLSManager;
//import com.zlzhang.ble.scan.BLESanType;
//import com.zlzhang.ble.scan.BLEScanListener;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.Executors;
//import java.util.concurrent.RejectedExecutionException;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//
//import static com.zlzhang.ble.util.BLSUtils.filtRSSIs;
//
//public class BLSManagerTestImpl extends BLSManager implements BLEScanListener {
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
//    private BLECentral mBleCentral;
//    private          SmtManagerState                   mBLECentralState;
//    private volatile Hashtable<Long, List<RSSIRecord>> mRSSILocs;
//    private volatile Hashtable<Long, SNEncryptor>      mSnEncryptors;
//    private volatile HashMap<String, IDSRssi>          mIDSRssiMap;
//
//    private static ScheduledExecutorService mBlsScheduler = Executors.newScheduledThreadPool(2); // In theory there would be only 2 locating threads in parallel at max
//
//    private              ScheduledFuture  mBLELocatingHandler = null;
//    private static final SimpleDateFormat mDateFormater       = new SimpleDateFormat("HH:mm:ss.SSS");
//
//    private static final LockUtil sLockForBle    = new LockUtil();
//    private static final LockUtil sLockForRssi   = new LockUtil();
//    private static final LockUtil sLockForIdsEnc = new LockUtil();
//
//    private boolean bAdapterDefaultWorking = false;
//    private boolean bAdapterCentralWorking = false;
//    private volatile boolean mIsBLELocating;
//    private List<IDSBeaconDTO> beaconDTOList = null;
//
//    private BLSManagerTestImpl() {
//    }
//
//    public BLSManagerTestImpl(Context context) {
//        mContext = context;
//        // Set up BLE location service
//        mBLECentralState = SmtManagerState.BLECentralStateUnsupported;
//        int sdk = Build.VERSION.SDK_INT;
//        if (sdk >= 17 && Build.BRAND.equals("techfaith") && Build.MODEL.equals("J6")) {
//            mBleCentral = new StandardCentral(context, this);
//        } else {
//            if (sdk > 17) {
//                mBleCentral = new JellyBeanCentral(context, this);
//            } else if (sdk == 17) {
//                if (Build.MODEL.equals("HUAWEI P6 S-U06") ||
//                        Build.MODEL.equals("IdeaTab S6000-F") /* sn might be null, just for testing*/) {
//                    mBleCentral = new StandardCentral(context, this);
//                }
//            }
//        }
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
//    public IDSRssi parse(byte[] scanRecord, int rssi) {
//        int     index   = 0;
//        int     maxLen  = scanRecord.length < 31 ? scanRecord.length : 31;
//        IDSRssi idsRssi = null;
//        if (scanRecord == null) {
//            return idsRssi;
//        }
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
//    @Override
//    public void didDiscoverDevice(BluetoothDevice device, int rssi, byte[] bytes) {
//        if (device == null) {
//            return;
//        }
//        if (mIsBLELocating) {
//            Date now = new Date();
//            if (mScanType == BLESanType.SDK_SCAN.getValue()) {
//                setRssiMap(device, rssi, bytes);
//            } else if (mScanType == BLESanType.GENERAL_SCAN.getValue()
//                    || mScanType == BLESanType.PARKING_SCAN.getValue() || mScanType == BLESanType.INTERPHON_SCAN.getValue()) {
//                if (mScanType == BLESanType.PARKING_SCAN.getValue() || mScanType == BLESanType.INTERPHON_SCAN.getValue()) {
//                    if (!filterUuid(parseUuids(bytes))) {
//                        return;
//                    }
//                }
//                setRssiMap(device, rssi, bytes);
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
//                // TODO: zqp debugging for wanda shi-jing-shan mall supports
//                IDSBeacon    beacon       = DevMgr.fromRssi(rssiRecord);
//                IDSBeaconDTO idsBeaconDTO = new IDSBeaconDTO();
//                idsBeaconDTO.setAddress(device.getAddress());
//                idsBeaconDTO.setIdsBeacon(beacon);
//                idsBeaconDTO.setName(device.getName());
//                beaconDTOList.add(idsBeaconDTO);
//            }
//        }
//    }
//
//    private void setRssiMap(BluetoothDevice device, int rssi, byte[] bytes) {
//        IDSRssi idsRssi = parse(bytes, rssi);
//        if (idsRssi != null) {
//            if (!mIDSRssiMap.containsKey(device.getAddress())) {
//                mIDSRssiMap.put(device.getAddress(), idsRssi);
//            } else {
//                if (mIDSRssiMap.get(device.getAddress()).getRssi() < rssi) {
//                    mIDSRssiMap.put(device.getAddress(), idsRssi);
//                }
//            }
//        }
//    }
//
//    //for parking
//    private boolean filterUuid(List<UUID> uuids) {
//        if (uuids == null || uuids.size() <= 0) {
//            return false;
//        }
//        for (int i = 0; i < uuids.size(); i++) {
//            if (String.valueOf(uuids.get(i)).contains("dcba")) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    //for parking
//    private List<UUID> parseUuids(byte[] advertisedData) {
//        List<UUID> uuids = new ArrayList<UUID>();
//
//        ByteBuffer buffer = ByteBuffer.wrap(advertisedData).order(ByteOrder.LITTLE_ENDIAN);
//        while (buffer.remaining() > 2) {
//            byte length = buffer.get();
//            if (length == 0) break;
//
//            byte type = buffer.get();
//            switch (type) {
//                case 0x02: // Partial list of 16-bit UUIDs
//                case 0x03: // Complete list of 16-bit UUIDs
//                    while (length >= 2) {
//                        uuids.add(UUID.fromString(String.format(
//                                "%08x-0000-1000-8000-00805F9B34FB", buffer.getShort())));
//                        length -= 2;
//                    }
//                    break;
//
//                case 0x06: // Partial list of 128-bit UUIDs
//                case 0x07: // Complete list of 128-bit UUIDs
//                    while (length >= 16) {
//                        long lsb = buffer.getLong();
//                        long msb = buffer.getLong();
//                        uuids.add(new UUID(msb, lsb));
//                        length -= 16;
//                    }
//                    break;
//
//                default:
//                    buffer.position(buffer.position() + length - 1);
//                    break;
//            }
//        }
//        return uuids;
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
//
//    @Override
//    public void stopGlobalScan() {
//        if (bGlobalScan && bGlobalRunning) {
//            if (sLockForBle.getLock(true)) {
//                try {
//                    LogCurr("StopScan(G)-Central-Calling");
//                    mBleCentral.stopScan();
//                    LogCurr("StopScan(G)-Central-Called");
//                    bGlobalRunning = false;
//                } catch (Exception e) {
//                    LogHelper.w(LOG_TAG, e.getMessage());
//                } finally {
//                    sLockForBle.putLock();
//                }
//            }
//        }
//    }
//
//
//    public boolean startBLEScan(SmtListener<List<RSSIRecord>> smtListener, float scanSeconds, int scanType) {
//        mScanType = scanType;
//        return startBLEScan(smtListener, scanSeconds * 1.f);
//    }
//
//    public boolean startBLEScan(SmtListener<List<RSSIRecord>> smtListener, long scanSeconds, int scanType) {
//        mScanType = scanType;
//        return startBLEScan(smtListener, scanSeconds * 1.f);
//    }
//
//    public boolean startBLEScan(SmtListener<List<RSSIRecord>> smtListener, long scanSeconds) {
//        mScanType = BLESanType.GENERAL_SCAN.getValue();
//        return startBLEScan(smtListener, scanSeconds * 1.f);
//    }
//
//    private boolean startBLEScan(SmtListener<List<RSSIRecord>> smtListener, float scanSeconds) {
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
//                return false;
//            } else {
//                if (sLockForBle.getLock()) {
//                    try {
//                        mSmtListenerBLE = smtListener;
//                        doScan();
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
//
//    private void doScan() {
//        mRSSILocs = new Hashtable<Long, List<RSSIRecord>>();
//        mIDSRssiMap = new HashMap<String, IDSRssi>();
//        mSnEncryptors = new Hashtable<Long, SNEncryptor>();
//        beaconDTOList = new ArrayList<IDSBeaconDTO>();
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
//                            stopBLEScan();
//                            mSmtListenerBLE.didResultBeaconList(beaconDTOList);
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
//    private void stopRSSIRecordScan() {
//        List<RSSIRecord> bestRSSIs = null;
//        if (sLockForRssi.getLock(true)) {
//            try {
//                bestRSSIs = filtRSSIs(mRSSILocs);
//            } catch (Exception e) {
//                LogHelper.w(LOG_TAG, e.getMessage());
//            } finally {
//                sLockForRssi.putLock();
//            }
//        }
//
//        if (bestRSSIs == null || bestRSSIs.size() == 0) {
//            // sometimes Android BLE HW returns 0 RSSIs within in non-stop scanning method
//            try {
//                if (bGlobalScan && bGlobalRunning) {
//                    LogCurr("StopScan(G)-Central-Calling");
//                    mBleCentral.stopScan();
//                    LogCurr("StopScan(G)-Central-Called");
//                    bGlobalRunning = false;
//                }
//            } catch (Exception e) {
//                LogHelper.w(LOG_TAG, e.getMessage());
//            }
//        }
//        LogHelper.i(LOG_TAG, String.format("1"));
//        try {
//            if (mSmtListenerBLE != null) {
////                decideCityMall();
//                decideCityMall(bestRSSIs);
//                mSmtListenerBLE.didLocate(bestRSSIs);
//                mSmtListenerBLE.didLocate(mIDSRssiMap);
//                mSmtListenerBLE.didFinishLocate();
//            }
//        } catch (Exception e) {
//            LogHelper.e(LOG_TAG, e.getMessage());
//        }
//    }
//
//    private void stopIDSRssiScan() {
//        if (mIDSRssiMap == null) {
//            try {
//                if (bGlobalScan && bGlobalRunning) {
//                    LogCurr("StopScan(G)-Central-Calling");
//                    mBleCentral.stopScan();
//                    LogCurr("StopScan(G)-Central-Called");
//                    bGlobalRunning = false;
//                }
//            } catch (Exception e) {
//                LogHelper.w(LOG_TAG, e.getMessage());
//            }
//        }
//
//        try {
//            if (mSmtListenerBLE != null) {
//                mSmtListenerBLE.didLocate(mIDSRssiMap);
//                mSmtListenerBLE.didFinishLocate();
//            }
//        } catch (Exception e) {
//            LogHelper.e(LOG_TAG, e.getMessage());
//        }
//    }
//
//    private void stopBLEScan() {
//        if (sLockForBle.getLock()) {
//            try {
//                if (mIsBLELocating) {
//                    LogCurr("StopScan-1");
//                    if (!bGlobalScan) {
//                        LogCurr("StopScan-Central-Calling");
//                        mBleCentral.stopScan();
//                        LogCurr("StopScan-Central-Called");
//                    }
//                    LogCurr("StopScan-2");
//                    if (mScanType == BLESanType.GENERAL_SCAN.getValue() || mScanType == BLESanType.PARKING_SCAN.getValue()) {
//                        stopRSSIRecordScan();
//                    } else {
//                        stopIDSRssiScan();
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
//    private void decideCityMall(List<RSSIRecord> rssiRecords) {
//        if (sLockForIdsEnc.getLock(true)) {
//            try {
//                if (rssiRecords != null && rssiRecords.size() > 0) {
//                    int strongestRssi = -1000;
//                    int cityId        = 0;
//                    int mallId        = 0;
//                    long sn = 0;
//                    for (RSSIRecord rssiRecord : rssiRecords) {
//                        SNEncryptor snEncryptor = new SNEncryptor();
//                        byte[]      scanRecord  = rssiRecord.getData();
//                        if (strongestRssi < rssiRecord.getRssi()) {
//                            strongestRssi = rssiRecord.getRssi();
//                            snEncryptor.parse(scanRecord);
//                            cityId = snEncryptor.getCityId();
//                            mallId = snEncryptor.getMallId();
//                            sn = snEncryptor.getStarSn();
//                            LogHelper.d(LOG_TAG, "test strongestRssi: " + strongestRssi + " mallId: " + mallId);
//                        }
//                    }
//                    if (mallId > 0 && mSmtListenerBLE != null) {
//                        mSmtListenerBLE.didFoundCityMall(SNGenerator.SnToString(sn), cityId, mallId, strongestRssi);
//                    }
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
//
//    private byte[] intToByte(int value) {
//        byte[] src = new byte[4];
//        src[3] = (byte) ((value >> 24) & 0xff);
//        src[2] = (byte) ((value >> 16) & 0xff);
//        src[1] = (byte) ((value >> 8) & 0xff);
//        src[0] = (byte) (value & 0xff);
//        return src;
//    }
//
//    public static int bytesToInt(byte[] src, int offset) {
//        int value;
//        value = (int) ((src[offset] & 0xFF)
//                | ((src[offset + 1] & 0xFF) << 8)
//                | ((src[offset + 2] & 0xFF) << 16)
//                | ((src[offset + 3] & 0xFF) << 24));
//        return value;
//    }
//
//
//    @Override
//    public boolean validateBLEState() {
//        if (checkBLECentralState(null)) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void release() {
//        stopBLEScan();
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
