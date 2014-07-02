package com.oracle.lostToys.data;

public class Beacon {

    private int major;
    private int minor;
    private int rssi;
    private double accuracy;
    private String proximityUUID;
    private int proximity;
    private String regionID;

    public Beacon() {
        super();
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMajor() {
        return major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMinor() {
        return minor;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setProximityUUID(String proximityUUID) {
        this.proximityUUID = proximityUUID;
    }

    public String getProximityUUID() {
        return proximityUUID;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }

    public int getProximity() {
        return proximity;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
    }

    public String getRegionID() {
        return regionID;
    }
}
