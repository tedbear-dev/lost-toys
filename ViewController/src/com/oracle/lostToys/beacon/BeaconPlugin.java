package com.oracle.lostToys.beacon;

import com.oracle.lostToys.EL;

import com.oracle.lostToys.bean.MainBean;

import com.sun.util.logging.Level;

import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class BeaconPlugin {
    
    public BeaconPlugin() {
        super();
    }
    
    public static void vibrate(){

        try {
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
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,BeaconPlugin.class, "startMonitoring", ex);   
        }        
    }

    public static void test(String msg){
        
        try {
            AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
                "com.oracle.lostToys.main",
                "window.beaconPlugin.test",
                new Object[] {msg}
            );
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,BeaconPlugin.class, "test", ex);   
        }
    }
    
    public static void startMonitoring(){
        
        try {
            AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
                "com.oracle.lostToys.main",
                "window.beaconPlugin.defineClasses",
                new Object[] {
                    "com.oracle.lostToys.beacon.BeaconListCallback",
                    "com.oracle.lostToys.data.Beacon"
                }
            );
            
            String device = (String)EL.eval("deviceScope.device.name");
                    
            AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
                "com.oracle.lostToys.main",
                "window.beaconPlugin.startMonitoring",
                new Object[] {
                    EL.eval("preferenceScope.application.preferences.familyUUID"),
                    "lost-toys",
                    (device.indexOf("Simulator") >= 0) ? Boolean.TRUE : Boolean.FALSE // Is this the simulator? If so, use fake data
                }
            );
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,BeaconPlugin.class, "startMonitoring", ex);   
        }
    }
}
