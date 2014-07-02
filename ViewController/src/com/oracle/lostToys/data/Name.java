package com.oracle.lostToys.data;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class Name {
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Name() {
        super();
    }
    
    private String adjective;
    private String girlName;
    private String boyName;

    public void setAdjective(String adjective) {
        String oldAdjective = this.adjective;
        this.adjective = adjective;
        propertyChangeSupport.firePropertyChange("adjective", oldAdjective, adjective);
    }

    public String getAdjective() {
        return adjective;
    }

    public void setGirlName(String girlName) {
        String oldGirlName = this.girlName;
        this.girlName = girlName;
        propertyChangeSupport.firePropertyChange("girlName", oldGirlName, girlName);
    }

    public String getGirlName() {
        return girlName;
    }

    public void setBoyName(String boyName) {
        String oldBoyName = this.boyName;
        this.boyName = boyName;
        propertyChangeSupport.firePropertyChange("boyName", oldBoyName, boyName);
    }

    public String getBoyName() {
        return boyName;
    }


    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
