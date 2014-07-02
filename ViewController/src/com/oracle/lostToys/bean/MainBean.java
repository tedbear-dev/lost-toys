package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;
import com.oracle.lostToys.beacon.BeaconPlugin;
import com.oracle.lostToys.data.Beacon;
import com.oracle.lostToys.data.BeaconList;
import com.oracle.lostToys.data.Toy;

import com.oracle.lostToys.data.ToyList;

import com.sun.util.logging.Level;

import java.util.ArrayList;

import java.util.Iterator;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.java.beans.ProviderChangeListener;
import oracle.adfmf.java.beans.ProviderChangeSupport;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class MainBean {
    
    public static final int MAX_PROXIMITY = 200;
    public static final int MIN_PROXIMITY = 0;
    
    private Toy selectedToy;
    private BeaconList beaconList;
    // private ToyList toyList;
    
    // private PreferencesBean preferences;
    // private BeaconPluginBean beaconPlugin;
    // private ToyGalleryBean toyGallery;
    // private FindToyBean findToy;
    // private EditToyBean editToy;
    
    public MainBean() {
        
        super();
        
        try {
            setSelectedToy(null);
            
            beaconList = new BeaconList();
            // toyList = new ToyList();
            
            // preferences = new PreferencesBean();
            // beaconPlugin = new BeaconPluginBean();
            
            // toyGallery = new ToyGalleryBean();
            // findToy = new FindToyBean();
            // editToy = new EditToyBean();
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,MainBean.class, "constructor", ex);      
        }
    }
    
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
        
    public void setSelectedToy(Toy selectedToy) {
        
        Trace.log(Utility.FrameworkLogger,Level.SEVERE,MainBean.class, "setSelectedToy", "newly selected toy is " + ((selectedToy == null)?"null":selectedToy.getName()));   
                
        Toy oldSelectedToy = this.selectedToy;
         
         /* 
        if(selectedToy == null){
            this.selectedToy = null;
        }
        else{
            if(this.selectedToy == null){
                this.selectedToy = new Toy(selectedToy.getName(),selectedToy.getImage(),selectedToy.getMajor(),selectedToy.getMinor(),selectedToy.getUuid());
            }
            else{
                this.selectedToy.setName(selectedToy.getName());
                this.selectedToy.setMajor(selectedToy.getMajor());
                this.selectedToy.setMinor(selectedToy.getMinor());
                this.selectedToy.setImage(selectedToy.getImage());
                this.selectedToy.setUuid(selectedToy.getUuid());
            }
        }
            */
        this.selectedToy = selectedToy;
        
        propertyChangeSupport.firePropertyChange("selectedToy", oldSelectedToy, selectedToy);
    }

    public Toy getSelectedToy() {
        return selectedToy;
    }
    
    public BeaconList getBeacons(){
        return beaconList;
    }
    
    /*public ToyList getToys(){
        return toyList;    
    }*/
    
    public PreferencesBean getPreferences(){
        return (PreferencesBean)EL.eval("pageFlowScope.preferences");
    }
    
    public BeaconPluginBean getBeaconPlugin(){
        return (BeaconPluginBean)EL.eval("pageFlowScope.beaconPlugin");
    }

    public String beaconPluginTest(){
        getBeaconPlugin().test();
        return null;
    }
    
    public String beaconPluginVibrate(){
        getBeaconPlugin().vibrate();
        return null;
    }
    
    public ToyGalleryBean getToyGallery(){
        return (ToyGalleryBean)EL.eval("viewScope.toyGallery");
    }
    
    public FindToyBean getFindToy(){
        return (FindToyBean)EL.eval("viewScope.findToy");
    }
    
    public EditToyBean getEditToy(){
        return (EditToyBean)EL.eval("pageFlowScope.editToy");
    }
    
    public Boolean getTRUE(){
        return Boolean.TRUE;
    }
    
    public Boolean getFALSE(){
        return Boolean.FALSE;
    }
    
    public String getNULL(){
        return null;
    }
    
    public Beacon getSelectedBeacon(){
        
        if(selectedToy == null) {
            //Trace.log(Utility.FrameworkLogger,Level.SEVERE,MainBean.class, "getSelectedBeacon", "no toy is selected" ); 
            return null;
        }
        if(beaconList == null || beaconList.getBeacons().isEmpty()) {
            //Trace.log(Utility.FrameworkLogger,Level.SEVERE,MainBean.class, "getSelectedBeacon", "no beacon list, or its empty" ); 
            return null;
        }

        //Trace.log(Utility.FrameworkLogger,Level.SEVERE,MainBean.class, "getSelectedBeacon", "selected = " + selectedToy.getUuid() + ", " + selectedToy.getMajor() + "." + selectedToy.getMinor());   
        
        Iterator i = beaconList.getBeacons().iterator();
        while(i.hasNext()){
            Beacon b = (Beacon)i.next();

            //Trace.log(Utility.FrameworkLogger,Level.SEVERE,MainBean.class, "getSelectedBeacon", "beacon = " + b.getProximityUUID() + ", " + b.getMajor() + "." + b.getMinor());   
            
            if(b.getProximityUUID().equals(selectedToy.getUuid()) && b.getMajor() == selectedToy.getMajor() && b.getMinor() == selectedToy.getMinor()){ 
                return b;
            }
        }
        
        return null;
    }

    public int proximityFromAccuracy(double accuracy){
    
        // Accuracy in meters == proximity in centimeters
        int rc = (int)Math.round(accuracy * 100.0);
        
        if(rc < MIN_PROXIMITY) rc = MAX_PROXIMITY;
        else rc = Math.min(MAX_PROXIMITY,Math.max(MIN_PROXIMITY,rc)); 

        return rc;
    }
    
    public void updateSelectedProximity(){
        
        Beacon b = getSelectedBeacon();
        if(b != null){
            // Trace.log(Utility.FrameworkLogger,Level.SEVERE,MainBean.class, "updateSelectedProximity", "accuracy for selected beacon is " + b.getAccuracy() ); 
            
            int proximity = proximityFromAccuracy(b.getAccuracy());
            if(getFindToy() != null) getFindToy().setProximity(proximity);
        }
        else{
            // Trace.log(Utility.FrameworkLogger,Level.SEVERE,MainBean.class, "updateSelectedProximity", "no selected beacon found" ); 
        }

        if(getEditToy() != null) getEditToy().lookForCloseToy();
    }

    public int getMinProximity(){
        return MIN_PROXIMITY;
    }

    public int getMaxProximity(){
        return MAX_PROXIMITY;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
