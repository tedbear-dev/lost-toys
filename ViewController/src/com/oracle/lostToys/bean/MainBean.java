package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;
import com.oracle.lostToys.beacon.Beacon;
import com.oracle.lostToys.data.Toy;

import com.sun.util.logging.Level;

import java.util.ArrayList;

import java.util.Iterator;

import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class MainBean {
    public MainBean() {
        super();
    }
    
    public Boolean getDebug(){
        
        Boolean debug = (Boolean)EL.eval("preferenceScope.application.preferences.debugMode");
        return debug; 
    }
    
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    private Toy selected = null;
        
    public void setSelectedToy(Toy selected) {
        
        Toy oldSelected = this.selected;
        this.selected = selected;
        propertyChangeSupport.firePropertyChange("selectedToy", oldSelected, selected);
    }

    public Toy getSelectedToy() {
        return selected;
    }
    
    public void initBeacons(){
        
        AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
            "com.oracle.lostToys.main",
            "window.beaconPlugin.defineClasses",
            new Object[] {
                "com.oracle.lostToys.beacon.BeaconListCallback",
                "com.oracle.lostToys.beacon.Beacon"
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

        Trace.log(Utility.FrameworkLogger,Level.SEVERE,MainBean.class, "initBeacons", "defineClasses");    
    }
    
    public Beacon getSelectedBeacon(){
        
        if(selected == null) return null;
        if(beacons == null || beacons.isEmpty()) return null;

        Iterator i = beacons.iterator();
        while(i.hasNext()){
            Beacon b = (Beacon)i.next();

            if(b.getProximityUUID().equals(selected.getUuid()) && b.getMajor() == selected.getMajor() && b.getMinor() == selected.getMinor()){ 
                return b;
            }
        }
        
        return null;
    }
    
    ArrayList beacons = new ArrayList();

    public void startBeaconUpdate(){
        beacons = new ArrayList();
    }
    
    public void addBeacon(Beacon beacon){
        beacons.add(beacon);
    }
    
    public void endBeaconUpdate(){
        propertyChangeSupport.firePropertyChange("beacons", null, beacons);
        
        FindFriendBean ffb = (FindFriendBean)EL.eval("viewScope.FindFriend");
        if(ffb != null){
            ffb.update();
        }
    }

    public ArrayList getBeacons() {
        return beacons;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
