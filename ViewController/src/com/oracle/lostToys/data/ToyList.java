package com.oracle.lostToys.data;

import com.oracle.lostToys.bean.ToyGalleryBean;

import com.sun.util.logging.Level;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;

import java.util.Iterator;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class ToyList {
    
    private ArrayList toys;
    
    public ToyList() {
        super();
        refreshToys();
    }
    
    public Toy[] getToys(){

        return (Toy[]) toys.toArray(new Toy[toys.size()]);
    }
    
    public Toy findToyById(String id){

        Toy rc = null;
        Iterator i = toys.iterator();
        
        Trace.log(Utility.FrameworkLogger,Level.SEVERE,ToyList.class, "findToyById", "Getting toy by id: " + id);       
        while(i.hasNext()){
            Toy t = (Toy) i.next();
            if((t.getMajor() + "." + t.getMinor()).equals(id)){
                rc = t;
                Trace.log(Utility.FrameworkLogger,Level.SEVERE,ToyList.class, "findToyById", "Found toy: " + rc.getName());       
            }
        }
        
        return rc;
    }
    
    private void refreshToys(){
    
        toys = new ArrayList();
               
        Connection conn = null;
        try {
            String docRoot = AdfmfJavaUtilities.getDirectoryPathRoot(AdfmfJavaUtilities.ApplicationDirectory);
            String dbName = docRoot + "/lost-toys.db";

            conn = new SQLite.JDBCDataSource("jdbc:sqlite:" + dbName).getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT uuid,major,minor,toy_name,image_base64 FROM befriended_toys ORDER BY toy_name");
            while(rs.next()){
                toys.add(new Toy(rs.getString(4),rs.getString(5),rs.getInt(2),rs.getInt(3),rs.getString(1)));
            }
        }
        catch (Exception e) {
            Trace.log(Utility.FrameworkLogger,Level.SEVERE, ToyGalleryBean.class, "new ToyGallery", e);
        }
        finally {
            if (conn != null){
                try{
                    conn.close();
                }
                catch(Exception ex){
                    Trace.log(Utility.FrameworkLogger,Level.SEVERE, ToyGalleryBean.class, "new ToyGallery", ex);
                }
            }
        }
    }
    
    public void addNewToy(String uuid,int major,int minor,String name,String image){
            
        Connection conn = null;
        try {
            String docRoot = AdfmfJavaUtilities.getDirectoryPathRoot(AdfmfJavaUtilities.ApplicationDirectory);
            String dbName = docRoot + "/lost-toys.db";

            conn = new SQLite.JDBCDataSource("jdbc:sqlite:" + dbName).getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO befriended_toys(uuid,major,minor,toy_name,image_base64) VALUES(?,?,?,?,?)");
            stmt.setString(1, uuid);
            stmt.setInt(2, major);
            stmt.setInt(3, minor);
            stmt.setString(4, name);
            stmt.setString(5, image);
            stmt.executeUpdate();
            conn.commit();
            
            refreshToys();
        }
        catch (Exception e) {
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,ToyList.class, "addNewToy", e);
        }
        finally {
            if (conn != null){
                try{
                    conn.close();
                }
                catch(Exception ex){
                    Trace.log(Utility.FrameworkLogger,Level.SEVERE,ToyList.class, "addNewToy", ex);
                }
            }
        }

    }
}
