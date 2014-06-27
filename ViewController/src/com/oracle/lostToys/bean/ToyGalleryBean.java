package com.oracle.lostToys.model;

import com.sun.util.logging.Level;

import com.sun.util.logging.Logger;

import java.io.BufferedReader;
import java.io.File;

import java.io.InputStreamReader;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;

import java.util.Iterator;

import java.util.Map;

import javax.el.ValueExpression;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.bindings.dbf.AmxCollectionModel;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.java.beans.ProviderChangeListener;
import oracle.adfmf.java.beans.ProviderChangeSupport;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class ToyGallery {
    
    private ArrayList befriendedToys;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    private BefriendedToy selected = null;

    private int selectedMajor = -1;
    private int selectedMinor = -1;

    public void setSelectedMajor(int selectedMajor) {
        int oldSelectedMajor = this.selectedMajor;
        this.selectedMajor = selectedMajor;
        propertyChangeSupport.firePropertyChange("selectedMajor", oldSelectedMajor, selectedMajor);

        refreshSelected();
    }

    public int getSelectedMajor() {
        return selectedMajor;
    }

    public void setSelectedMinor(int selectedMinor) {
        int oldSelectedMinor = this.selectedMinor;
        this.selectedMinor = selectedMinor;
        propertyChangeSupport.firePropertyChange("selectedMinor", oldSelectedMinor, selectedMinor);
        
        refreshSelected();
    }

    public int getSelectedMinor() {
        return selectedMinor;
    }

    private void refreshSelected(){
        
        BefriendedToy rc = null;
        
        if(selectedMajor != -1 || selectedMinor != -1){
            
            Iterator i = befriendedToys.iterator();
            
            while(i.hasNext()){
                BefriendedToy t = (BefriendedToy)i.next();
                if(t.getMajor() == selectedMajor && t.getMinor() == selectedMinor){
                    rc = t;
                }
            }
        }
        setSelectedToy(rc);
    }

    public void setSelectedToy(BefriendedToy selected) {
        BefriendedToy oldSelected = this.selected;
        this.selected = (BefriendedToy)selected;
        propertyChangeSupport.firePropertyChange("selectedToy", oldSelected, selected);
    }

    public BefriendedToy getSelectedToy() {
        return selected;
    }


    public void setBefriendedToys(ArrayList befriendedToys) {
        ArrayList oldBefriendedToys = this.befriendedToys;
        this.befriendedToys = befriendedToys;
        propertyChangeSupport.firePropertyChange("befriendedToys", oldBefriendedToys, befriendedToys);
    }

    public ArrayList getBefriendedToys() {
        return befriendedToys;
    }

    public ToyGallery() {
        super();
        refreshToys();
    }
    
    public void refreshToys(){
    
        ArrayList newBefriendedToys = new ArrayList();
               
        Connection conn = null;
        try {
            String docRoot = AdfmfJavaUtilities.getDirectoryPathRoot(AdfmfJavaUtilities.ApplicationDirectory);
            String dbName = docRoot + "/lost-toys.db";

            conn = new SQLite.JDBCDataSource("jdbc:sqlite:" + dbName).getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT uuid,major,minor,toy_name,image_base64 FROM befriended_toys ORDER BY toy_name");
            while(rs.next()){
                newBefriendedToys.add(new BefriendedToy(rs.getString(4),rs.getString(5),rs.getInt(2),rs.getInt(3),rs.getString(1)));
            }
        }
        catch (Exception e) {
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,ToyGallery.class, "new ToyGallery", e);
        }
        finally {
            if (conn != null){
                try{
                    conn.close();
                }
                catch(Exception ex){
                    Trace.log(Utility.FrameworkLogger,Level.SEVERE,ToyGallery.class, "new ToyGallery", ex);
                }
            }
        }
        
        setBefriendedToys(newBefriendedToys);
        
        refreshSelected();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

}
