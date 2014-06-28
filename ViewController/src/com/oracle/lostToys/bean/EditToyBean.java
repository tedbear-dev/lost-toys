package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;

import com.sun.util.logging.Level;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
    
    public EditToyBean() {
    }

    public String getImage(){
        
        if(newPicture == null) return "/img/teddybear-blank.png";
        else return "data:image/gif;base64," + newPicture;
    }

    public void setImage(String picture){
    
        String oldPicture = getImage();
        newPicture = picture;
        propertyChangeSupport.firePropertyChange("image", oldPicture, getImage());            
    }
    
    private String getPicture(int source){

        return DeviceManagerFactory.getDeviceManager().getPicture(
            75,
            DeviceManager.CAMERA_DESTINATIONTYPE_DATA_URL,
            source, 
            false,
            DeviceManager.CAMERA_ENCODINGTYPE_JPEG, 
            256, 
            256
        );
    }
    
    public void choosePicture(ActionEvent evt) {
        setImage(getPicture(DeviceManager.CAMERA_SOURCETYPE_PHOTOLIBRARY));
    }

    public void takePicture(ActionEvent evt) {     
        setImage(getPicture(DeviceManager.CAMERA_SOURCETYPE_CAMERA));
    }
    
    public String saveChanges(){
        
        EL.exec(
            "addNewToy",
            new String[]{"uuid","major","minor","name","image"},
            new Object[]{"new-uuid",new Integer(123),new Integer(456),"No Name",newPicture}
        );
           
        return "gotoGallery";
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
