package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;

import com.oracle.lostToys.beacon.Beacon;
import com.oracle.lostToys.beacon.BeaconPlugin;
import com.oracle.lostToys.data.Toy;

import com.sun.util.logging.Level;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;

import java.util.Iterator;

import oracle.adf.model.datacontrols.device.DeviceManager;
import oracle.adf.model.datacontrols.device.DeviceManagerFactory;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.bindings.dbf.AmxMethodActionBinding;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class EditToyBean {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    String newPicture = null;
    String newName = null;
    int newMajor = -1;
    int newMinor = -1;
    String newUuid = null;
    boolean close = false;
    
    public EditToyBean() {
        reset();
    }

    public void setClose(boolean close) {
        boolean oldClose = this.close;
        this.close = close;
        propertyChangeSupport.firePropertyChange("close", oldClose, close);
    }

    public boolean isClose() {
        return close;
    }

    public void reset(){
        
        Toy selected = (Toy)EL.eval("applicationScope.Main.selectedToy");
        if(selected != null){
            
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EditToyBean.class, "reset", "setting values from selected toy: " + selected.getName());    
            
            newUuid = selected.getUuid();
            setImage(selected.getImage());
            setNewName(selected.getName());
            setNewMajor(selected.getMajor());
            setNewMinor(selected.getMinor());
        }
        else{
            newUuid = (String)EL.eval("preferenceScope.application.preferences.familyUUID");

            setImage(null);
            setNewName("No Name");
            setNewMajor(0);
            setNewMinor(0);

            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EditToyBean.class, "reset", "initializing for new toy");    
        }
    }

    public void update(){

        if(isClose()) return;
        
        double hug = Integer.parseInt(EL.eval("preferenceScope.application.preferences.hugProximity").toString());
        
        MainBean main = (MainBean)EL.eval("applicationScope.Main");
        if(main != null){
            ArrayList bs = main.getBeacons();
            if(bs != null){
                Iterator i = bs.iterator();
                while(i.hasNext()){
                    Beacon b = (Beacon)i.next();
                    double acc = b.getAccuracy();
                    if(acc < 0.0) acc = 1000.0;
                    acc = Math.min(1000.0,Math.max(0.0,acc * 100.0)); // Accuracy in meters == distance in centimeters
                    if(acc <= hug){
                        BeaconPlugin.vibrate();
                        setClose(true);

                        Toy t = (Toy)EL.exec("findToyById",new String[]{"id"},new Object[]{b.getMajor() + "." + b.getMinor()});
                        if(t != null){
                            newUuid = t.getUuid();
                            setNewName(t.getName());
                            setImage(t.getImage());
                            setNewMajor(t.getMajor());
                            setNewMinor(t.getMinor());
                        }
                        else{
                            newUuid = (String)EL.eval("preferenceScope.application.preferences.familyUUID");

                            setImage(null);
                            setNewName("No Name");
                            setNewMajor(b.getMajor());
                            setNewMinor(b.getMinor());
                        }
                        
                        break;
                    }
                }
            }
        }
    }
    
    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewMajor(int newMajor) {
        
        int oldNewMajor = this.newMajor;
        this.newMajor = newMajor;
        propertyChangeSupport.firePropertyChange("newMajor", oldNewMajor, this.newMajor);         
    }

    public int getNewMajor() {
        return newMajor;
    }

    public void setNewMinor(int newMinor) {
        
        int oldNewMinor = this.newMinor;
        this.newMinor = newMinor;
        propertyChangeSupport.firePropertyChange("newMinor", oldNewMinor, this.newMinor);         
    }

    public int getNewMinor() {
        return newMinor;
    }


    public String getNewUuid() {
        return newUuid;
    }

    public String getImage(){
        
        if(newPicture == null) return "/img/teddybear-blank.png";
        else return "data:image/gif;base64," + newPicture;
    }

    public void setImage(String picture){
    
        String oldPicture = getImage();
        
        String pic = null;
        
        if(picture == null) pic = null;
        else{
            if(picture.indexOf("data:image/gif;base64,") == 0) pic = picture.substring(picture.indexOf(',')+1);
            else if (picture.indexOf("/img/teddybear-blank.png") == 0) pic = null;
            else pic = picture;
        }
        
        newPicture = pic;
        propertyChangeSupport.firePropertyChange("image", oldPicture, getImage());            
    }
    
    private String getPicture(int source){

        String rc = DeviceManagerFactory.getDeviceManager().getPicture(
            75,
            DeviceManager.CAMERA_DESTINATIONTYPE_DATA_URL,
            source, 
            false,
            DeviceManager.CAMERA_ENCODINGTYPE_JPEG, 
            256, 
            256
        );
        
        if(rc != null && rc.length() < 1) rc = null;
        
        return rc;
    }
    
    public void choosePicture(ActionEvent evt) {
        setImage(getPicture(DeviceManager.CAMERA_SOURCETYPE_PHOTOLIBRARY));
    }

    public void takePicture(ActionEvent evt) {     
        setImage(getPicture(DeviceManager.CAMERA_SOURCETYPE_CAMERA));
    }
    
    public String saveChanges(){
        
        Toy selected = (Toy)EL.eval("applicationScope.Main.selectedToy");
        if(selected != null){
            EL.exec(
                "updateExistingToy",
                new String[]{"uuid","major","minor","name","image"},
                new Object[]{newUuid,new Integer(newMajor),new Integer(newMinor),newName,newPicture}
            );
        }
        else{
            EL.exec(
                "addNewToy",
                new String[]{"uuid","major","minor","name","image"},
                new Object[]{newUuid,new Integer(newMajor),new Integer(newMinor),newName,newPicture}
            );
        }
        
        return "gotoGallery";
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public void unadopt(ActionEvent actionEvent) {
        EL.exec(
            "deleteToy",
            new String[]{"uuid","major","minor"},
            new Object[]{newUuid,new Integer(newMajor),new Integer(newMinor)}
        );
    }
}
