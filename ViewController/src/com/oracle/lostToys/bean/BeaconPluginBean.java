package com.oracle.lostToys.bean;

import com.oracle.lostToys.beacon.BeaconPlugin;

public class BeaconPluginBean {
    public BeaconPluginBean() {
        super();
    }
    
    public void vibrate(){
        BeaconPlugin.vibrate();
    }
    
    public void test(){
        BeaconPlugin.test("Beacon plugin loaded and initialized successfully.");
    }
}
