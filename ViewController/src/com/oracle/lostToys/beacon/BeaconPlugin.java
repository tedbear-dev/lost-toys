package com.oracle.lostToys.beacon;

import com.oracle.lostToys.EL;

import oracle.adfmf.framework.api.AdfmfContainerUtilities;

public class BeaconPlugin {
    public BeaconPlugin() {
        super();
    }
    
    public static void vibrate(){
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
    }

    public static void test(String msg){
        AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
            "com.oracle.lostToys.main",
            "window.beaconPlugin.test",
            new Object[] {msg}
        );
    }
}
