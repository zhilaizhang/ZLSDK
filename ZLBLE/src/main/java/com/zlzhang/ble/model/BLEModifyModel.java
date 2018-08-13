package com.zlzhang.ble.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhilaizhang on 17/2/16.
 */

public class BLEModifyModel implements Serializable{

    private int bleType;

    private String macAddress;

    private String serviceUUID;

    private List<String> serviceUUIDs;

    private List<String> characteristicUUID;

    public String getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = serviceUUID;
        serviceUUIDs = new ArrayList<>();
        serviceUUIDs.add(serviceUUID);
    }

    public void setServiceUUIDs(List<String> serviceUUIDs){
        this.serviceUUIDs = serviceUUIDs;
    }

    public List<String> getServiceUUIDs() {
        return serviceUUIDs;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public List<String> getCharacteristicUUID() {
        return characteristicUUID;
    }

    public void setCharacteristicUUID(List<String> characteristicUUID) {
        this.characteristicUUID = characteristicUUID;
    }

    public int getBleType() {
        return bleType;
    }

    public void setBleType(int bleType) {
        this.bleType = bleType;
    }
}
