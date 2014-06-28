package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;

import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class PrefCheckBean {
    public PrefCheckBean() {
    }

    public String vibrate() {
        AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
            "com.oracle.lostToys.main",
            "window.beaconPlugin.vibrate",
            new Object[] {
                new Integer(
                    Integer.parseInt(
                        EL.eval("preferenceScope.application.preferences.soundID").toString()
                    )
                )
            }
        );
        return null;
    }
    
    public String test(){

        AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
            "com.oracle.lostToys.main",
            "window.beaconPlugin.test",
            new Object[] {"Beacon plugin loaded and operational."}
        );
        
        return null;
    }
}
