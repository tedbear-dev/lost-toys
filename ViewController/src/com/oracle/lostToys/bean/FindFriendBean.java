package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;
import com.oracle.lostToys.beacon.Beacon;
import com.oracle.lostToys.beacon.BeaconPlugin;
import com.oracle.lostToys.data.Toy;

import com.sun.util.logging.Level;

import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class FindFriendBean {
    
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    private boolean found = false;


    public void setFound(boolean found) {
        boolean oldFound = this.found;
        this.found = found;
        propertyChangeSupport.firePropertyChange("found", oldFound, found);
    }

    public boolean isFound() {
        return found;
    }

    public FindFriendBean() {
        super();
    }
    
    public String getName(){
        
        return ((MainBean)EL.eval("applicationScope.Main")).getSelectedToy().getName();
    }
    
    public String getImage(){
        
        return ((MainBean)EL.eval("applicationScope.Main")).getSelectedToy().getImage();
    }
    
    public int getMajor(){
        return ((Integer)EL.eval("applicationScope.Main.selectedToy.major")).intValue();
    }

    public int getMinor(){
        return ((Integer)EL.eval("applicationScope.Main.selectedToy.minor")).intValue();
    }
    
    public double getProximity(){
        
        double rc = 1000.0; // == 1000cm == 10 meters
        
        MainBean main = (MainBean)EL.eval("applicationScope.Main");
        if(main != null){
            Beacon b = main.getSelectedBeacon();
            if(b != null){
                
                double acc = b.getAccuracy();
                
                if(acc < 0.0) acc = 1000.0;
                
                rc = Math.min(1000.0,Math.max(0.0,acc * 100.0)); // Accuracy in meters == distance in centimeters
                // Trace.log(Utility.FrameworkLogger,Level.SEVERE,FindFriendBean.class, "getProximity", "proximity="+rc);    
            }
        }
        
        double hug = Integer.parseInt(EL.eval("preferenceScope.application.preferences.hugProximity").toString());
        if(rc <= hug){
            BeaconPlugin.vibrate();
            setFound(true);
        }
        
        return rc;
    }
    
    public void update(){
        
        propertyChangeSupport.firePropertyChange("proximity", null, new Double(getProximity()));
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
