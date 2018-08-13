package com.zlzhang.ble;



/**
 * Created by Administrator on 2016/8/2.
 */
public class IDSBeaconDTO {
//    private IDSBeacon idsBeacon;
    private String    address;
    private String    name;
    private String    ummKey;

//    public IDSBeacon getIdsBeacon() {
//        return idsBeacon;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public void setIdsBeacon(IDSBeacon idsBeacon) {
//        this.idsBeacon = idsBeacon;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUmmKey() {
        return ummKey;
    }

    public void setUmmKey(String ummKey) {
        this.ummKey = ummKey;
    }
}
