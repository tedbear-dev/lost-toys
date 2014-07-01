package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;

import com.oracle.lostToys.beacon.BeaconPlugin;

import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class PrefCheckBean {
    public PrefCheckBean() {
    }

    public String vibrateAction() {
        BeaconPlugin.vibrate();
        return null;
    }
    
    public String test(){
        BeaconPlugin.test("Beacon plugin loaded and operational.");        
        return null;
    }
}
