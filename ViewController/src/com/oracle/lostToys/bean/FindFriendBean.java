package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;
import com.oracle.lostToys.beacon.Beacon;
import com.oracle.lostToys.data.Toy;

import com.sun.util.logging.Level;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class FindFriendBean {
    
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    public FindFriendBean() {
        super();
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
    
    public int getProximity(){
        
        int rc = 0;
        
        MainBean main = (MainBean)EL.eval("applicationScope.Main");
        if(main != null){
            Beacon b = main.getSelectedBeacon();
            if(b != null){
                rc = b.getRssi() * -1;
                Trace.log(Utility.FrameworkLogger,Level.SEVERE,FindFriendBean.class, "getProximity", "proximity="+rc);    
            }
        }
        
        return rc;
    }
    
    public void update(){
        
        propertyChangeSupport.firePropertyChange("proximity", null, new Integer(getProximity()));
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
