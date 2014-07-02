package com.oracle.lostToys.bean;

import com.oracle.lostToys.EL;

import com.oracle.lostToys.data.Beacon;
import com.oracle.lostToys.beacon.BeaconPlugin;
import com.oracle.lostToys.data.Name;
import com.oracle.lostToys.data.RandomNameService;
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
    String toyId = null;
    boolean nearby = false;
    
    public EditToyBean() {
        Trace.log(Utility.FrameworkLogger,Level.SEVERE,EditToyBean.class, "constructor", "constructing new EditToyBean"); 
    }

    public void setNearby(boolean nearby){
        
        Trace.log(Utility.FrameworkLogger,Level.SEVERE,EditToyBean.class, "setNearby", "setting nearby = " + nearby); 
        
        boolean oldNearby = this.nearby;
        this.nearby = nearby;
        propertyChangeSupport.firePropertyChange("nearby", oldNearby, nearby);
    }
    
    public boolean isNearby(){
        return nearby;
    }
    
    public void setToyId(String toyId){
        
        String oldToyId = this.toyId;
        this.toyId = toyId;
        
        refreshToy();
        
        propertyChangeSupport.firePropertyChange("toyId",oldToyId,this.toyId);
    }
    
    public String getToyId(){
        return toyId;
    }
    
    public void refreshToy(){

        Toy toy = (Toy)EL.exec("findToyById",new String[]{"id"},new Object[]{toyId});
        
        if(toy != null){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EditToyBean.class, "refreshToy", "setting values from selected toy: " + toy.getName());                

            newUuid = toy.getUuid();
            setImage(toy.getImage());
            setNewName(toy.getName());
            setNewMajor(toy.getMajor());
            setNewMinor(toy.getMinor());
        }
        else{
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EditToyBean.class, "refreshToy", "initializing for new toy");    

            newUuid = EL.main().getPreferences().getFamilyUUID();

            setImage(null);
            setNewName("New Toy");
            
            if(toyId == null || toyId.indexOf('.') == -1){
                setNewMajor(0);
                setNewMinor(0);
            }
            else{
                setNewMajor(Integer.parseInt(toyId.substring(0, toyId.indexOf('.'))));
                setNewMinor(Integer.parseInt(toyId.substring(toyId.indexOf('.')+1)));
            }
        }
    }

    public void lookForCloseToy(){
    
        if(isNearby()) return;
        
        MainBean main = EL.main();
        int hug = main.getPreferences().getHugProximity();
        
        ArrayList bs = main.getBeacons().getBeacons();
        Iterator i = bs.iterator();
        while(i.hasNext()){
            Beacon b = (Beacon)i.next();
            int proximity = main.proximityFromAccuracy(b.getAccuracy());
            
            if(proximity <= hug){

                Trace.log(Utility.FrameworkLogger,Level.SEVERE,EditToyBean.class, "lookForCloseToy", "found toy at proximity " + proximity);  

                BeaconPlugin.vibrate();
                setNearby(true);
                setToyId(b.getMajor()+"."+b.getMinor());
                break;       
            }
        }
    }
    
    public String saveChanges(){
        
        Toy selected = EL.main().getSelectedToy();
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

    public void unadopt(ActionEvent actionEvent) {
        EL.exec(
            "deleteToy",
            new String[]{"uuid","major","minor"},
            new Object[]{newUuid,new Integer(newMajor),new Integer(newMinor)}
        );
    }

    public void setNewName(String newName) {
        
        String oldNewName = this.newName;
        this.newName = newName;
        propertyChangeSupport.firePropertyChange("newName", oldNewName, this.newName);         
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
        
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public String generateGirlName() {
    
        Name name = RandomNameService.get();
        
        setNewName(name.getAdjective()+" "+name.getGirlName());
    
        return null;
    }
    
    public String generateBoyName() {
    
        Name name = RandomNameService.get();
        
        setNewName(name.getAdjective()+" "+name.getBoyName());
    
        return null;
    }
}
