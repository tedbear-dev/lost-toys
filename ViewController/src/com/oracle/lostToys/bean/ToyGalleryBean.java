package com.oracle.lostToys.bean;


import com.oracle.lostToys.data.Toy;

import com.sun.util.logging.Level;

import com.sun.util.logging.Logger;

import java.io.BufferedReader;
import java.io.File;

import java.io.InputStreamReader;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;

import java.util.Iterator;

import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.bindings.OperationBinding;
import oracle.adfmf.bindings.dbf.AmxCollectionModel;
import oracle.adfmf.bindings.dbf.AmxMethodActionBinding;
import oracle.adfmf.dc.bean.ConcreteJavaBeanObject;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.api.GenericTypeBeanSerializationHelper;
import oracle.adfmf.framework.model.AdfELContext;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.java.beans.ProviderChangeListener;
import oracle.adfmf.java.beans.ProviderChangeSupport;
import oracle.adfmf.util.GenericType;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class ToyGalleryBean {
    
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    private Toy selected = null;
    
    private String selectedId = "0.0";    
    public String getSelectedId(){return selectedId;}
    
    public void setSetSelectedById(String id){
        
        try{
            AmxMethodActionBinding ve = (AmxMethodActionBinding)AdfmfJavaUtilities.evaluateELExpression("#{bindings.findToyById}");
            selectedId = id;
            setSelectedToy((Toy) ((ConcreteJavaBeanObject)ve.execute()).getInstance());
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,ToyGalleryBean.class, "setSelectedById", ex);    
        }
    }
    
    public void setSelectedToy(Toy selected) {
        Toy oldSelected = this.selected;
        this.selected = (Toy) selected;
        propertyChangeSupport.firePropertyChange("selectedToy", oldSelected, selected);
    }

    public Toy getSelectedToy() {
        return selected;
    }

    public ToyGalleryBean() {
        super();
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
