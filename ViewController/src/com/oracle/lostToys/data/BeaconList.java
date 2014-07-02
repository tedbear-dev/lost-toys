package com.oracle.lostToys.data;

import com.oracle.lostToys.EL;
import com.oracle.lostToys.beacon.BeaconPlugin;
import com.oracle.lostToys.bean.EditToyBean;
import com.oracle.lostToys.bean.FindToyBean;

import com.sun.util.logging.Level;

import java.util.ArrayList;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.ProviderChangeListener;
import oracle.adfmf.java.beans.ProviderChangeSupport;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class BeaconList {
    
    private ProviderChangeSupport providerChangeSupport = new ProviderChangeSupport(this);

    private ArrayList beacons;
    
    public BeaconList() {
        
        super();
        
        beacons = new ArrayList();
    }

    public void startBeaconUpdate(){
        // Trace.log(Utility.FrameworkLogger,Level.SEVERE,BeaconList.class, "startBeaconListUpdate", "updating beacon list");   
        beacons = new ArrayList();
    }
    
    public void addBeacon(Beacon beacon){
        // Trace.log(Utility.FrameworkLogger,Level.SEVERE,BeaconList.class, "addBeacon", "adding a beacon");   
        beacons.add(beacon);
    }
    
    public void endBeaconUpdate(){
        
        // Trace.log(Utility.FrameworkLogger,Level.SEVERE,BeaconList.class, "endBeaconListUpdate", "updated beacon list");   

        EL.main().updateSelectedProximity();
        
        providerChangeSupport.fireProviderRefresh("beacons");
        AdfmfJavaUtilities.flushDataChangeEvent();
    }

    public ArrayList getBeacons() {
        return beacons;
    }
    
    public void addProviderChangeListener(ProviderChangeListener l){
        providerChangeSupport.addProviderChangeListener(l);
    }
    
    public void removeProviderChangeListener(ProviderChangeListener l){
        providerChangeSupport.removeProviderChangeListener(l);
    }
}
