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

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class ToyGallery {
    
    private ArrayList befriendedToys;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

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
                newBefriendedToys.add(new BefriendedToy(rs.getString(4),rs.getString(5)));
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
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
