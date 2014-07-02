package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;
import com.oracle.lostToys.data.Beacon;
import com.oracle.lostToys.beacon.BeaconPlugin;
import com.oracle.lostToys.data.Toy;

import com.sun.util.logging.Level;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.java.beans.ProviderChangeListener;
import oracle.adfmf.java.beans.ProviderChangeSupport;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class FindToyBean {
    
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    private boolean found;
    private int proximity;

    public FindToyBean() {
        
        super();
        
        setProximity(MainBean.MAX_PROXIMITY);
        setFound(false);
    }

    public void setFound(boolean found) {
        boolean oldFound = this.found;
        this.found = found;
        propertyChangeSupport.firePropertyChange("found", oldFound, found);
    }

    public boolean isFound() {
        return found;
    }
    
    public void setProximity(int proximity){
        
        // Trace.log(Utility.FrameworkLogger,Level.SEVERE,FindToyBean.class, "setProximity", "proximity = " + proximity + "(was " + this.proximity + ")");   
                
        int oldProximity = this.proximity;
        this.proximity = proximity;
        propertyChangeSupport.firePropertyChange("proximity", oldProximity, this.proximity);
        
        if(!found){
            int hug = EL.main().getPreferences().getHugProximity();
            if(proximity <= hug){
                BeaconPlugin.vibrate();
                setFound(true);
            }
        }
    }
    
    public int getProximity(){
        return proximity;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public void beforeFoundFriend(ActionEvent actionEvent) {
        
        EL.set("pageFlowScope.editToy.nearby",Boolean.TRUE);
        EL.set("pageFlowScope.editToy.toyId",EL.main().getSelectedToy().getId());
    }
}
