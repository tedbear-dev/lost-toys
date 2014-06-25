package com.oracle.lostToys.model;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class BefriendedToy {
    
    private String uuid;
    private int major;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void setUuid(String uuid) {
        String oldUuid = this.uuid;
        this.uuid = uuid;
        propertyChangeSupport.firePropertyChange("uuid", oldUuid, uuid);
    }

    public String getUuid() {
        return uuid;
    }

    public void setMajor(int major) {
        int oldMajor = this.major;
        this.major = major;
        propertyChangeSupport.firePropertyChange("major", oldMajor, major);
    }

    public int getMajor() {
        return major;
    }

    public void setMinor(int minor) {
        int oldMinor = this.minor;
        this.minor = minor;
        propertyChangeSupport.firePropertyChange("minor", oldMinor, minor);
    }

    public int getMinor() {
        return minor;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange("name", oldName, name);
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        String oldImage = this.image;
        this.image = image;
        propertyChangeSupport.firePropertyChange("image", oldImage, image);
    }

    public String getImage() {
        if(image == null) return "img/teddybear-blank.png";
        else return "data:image/gif;base64," + image;
    }
    private int minor;
    private String name;
    private String image;
    
    public BefriendedToy() {
        super();
    }

    public BefriendedToy(String name,String image) {
        super();
        
        this.name = name;
        this.image = image;
        this.minor = 0;
        this.major = 0;
        this.uuid = null;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
