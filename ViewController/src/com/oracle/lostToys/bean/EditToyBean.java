package com.oracle.lostToys.bean;

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
    
    private String newUuid = "";
    private int newMajor = 0;
    private int newMinor = 0;
    private String newName = "";
    private String newPicture = null;

    public String getNewUuid(){return newUuid;}
    public int getNewMajor() {return newMajor;}
    public int getNewMinor() {return newMinor;}
    public String getNewName() {return newName;}
    public String getNewPicture() {return newPicture;}

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
    
    public void choosePicture(ActionEvent evt) {
       
        String selectedPicture = DeviceManagerFactory.getDeviceManager().getPicture(100,
                    DeviceManager.CAMERA_DESTINATIONTYPE_DATA_URL,
                    DeviceManager.CAMERA_SOURCETYPE_PHOTOLIBRARY, false,
                    DeviceManager.CAMERA_ENCODINGTYPE_PNG, 0, 0);

        setImage(selectedPicture);
    }

    public void takePicture(ActionEvent evt) {
       
        String selectedPicture = DeviceManagerFactory.getDeviceManager().getPicture(100,
                    DeviceManager.CAMERA_DESTINATIONTYPE_DATA_URL,
                    DeviceManager.CAMERA_SOURCETYPE_CAMERA, false,
                    DeviceManager.CAMERA_ENCODINGTYPE_PNG, 0, 0);
        
        setImage(selectedPicture);
    }
    
    public String saveChanges(){
        
        newUuid = "no-uuid";
        newMajor = 123;
        newMinor = 456;
        newName = "no name";
        
        try{
            AmxMethodActionBinding ve = (AmxMethodActionBinding)AdfmfJavaUtilities.evaluateELExpression("#{bindings.addNewToy}");
            ve.execute();
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EditToyBean.class, "saveChanges", ex);    
        }
           
        return "gotoGallery";
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
