package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;

import com.oracle.lostToys.beacon.BeaconPlugin;

import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class PreferencesBean {
    
    public PreferencesBean() {
    }

    public String vibrateAction() {
        BeaconPlugin.vibrate();
        return null;
    }
    
    public String test(){
        BeaconPlugin.test("Beacon plugin loaded and operational.");        
        return null;
    }
    
    public Boolean getDebug(){
        return (Boolean)EL.eval("preferenceScope.application.preferences.debugMode");
    }

    public int getHugProximity(){
        return Integer.parseInt(EL.eval("preferenceScope.application.preferences.hugProximity").toString());
    }

    public String getFamilyUUID(){
        return (String)EL.eval("preferenceScope.application.preferences.familyUUID");        
    }

    public int getSoundID(){
        return Integer.parseInt(EL.eval("preferenceScope.application.preferences.soundID").toString());
    }
}
