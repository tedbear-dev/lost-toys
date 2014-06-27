package com.oracle.lostToys.data;

import com.sun.util.logging.Level;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class Toy {
    
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    private String uuid;
    private int major;
    private int minor;
    private String id;
    private String name;
    private String image;

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
        
        setId(this.major+"."+minor);
    }

    public int getMajor() {
        return major;
    }

    public void setMinor(int minor) {
        int oldMinor = this.minor;
        this.minor = minor;
        propertyChangeSupport.firePropertyChange("minor", oldMinor, minor);
        
        setId(major+"."+this.minor);
    }

    public int getMinor() {
        return minor;
    }

    public void setId(String id) {
        String oldId = this.id;
        this.id = id;
        propertyChangeSupport.firePropertyChange("id", oldId, id);
    }

    public String getId() {
        return id;
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
        if(image == null) return "/img/teddybear-blank.png";
        else return "data:image/gif;base64," + image;
    }
    
    public Toy() {
        super();
    }

    public Toy(String name,String image,int major,int minor,String uuid) {
        super();
        
        this.name = name;
        this.image = image;
        this.minor = major;
        this.major = minor;
        this.uuid = uuid;
        
        this.id = this.major+"."+this.minor;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
