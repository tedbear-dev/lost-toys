package com.oracle.lostToys.model;

import com.sun.util.logging.Level;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import oracle.adf.model.datacontrols.device.DeviceManager;
import oracle.adf.model.datacontrols.device.DeviceManagerFactory;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class TakePictureBean {
    public TakePictureBean() {
    }

    public String snap() {
       
        String selectedPicture = DeviceManagerFactory.getDeviceManager().getPicture(100,
                    DeviceManager.CAMERA_DESTINATIONTYPE_DATA_URL,
                    DeviceManager.CAMERA_SOURCETYPE_PHOTOLIBRARY, false,
                    DeviceManager.CAMERA_ENCODINGTYPE_PNG, 0, 0);
        
        Connection conn = null;
        try {
            String docRoot = AdfmfJavaUtilities.getDirectoryPathRoot(AdfmfJavaUtilities.ApplicationDirectory);
            String dbName = docRoot + "/lost-toys.db";

            conn = new SQLite.JDBCDataSource("jdbc:sqlite:" + dbName).getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO befriended_toys(uuid,major,minor,toy_name,image_base64) VALUES('n',0,0,'new toy',?)");
            stmt.setString(1, selectedPicture);
            stmt.executeUpdate();
            conn.commit();
            
            ((ToyGallery)(AdfmfJavaUtilities.evaluateELExpression("#{pageFlowScope.ToyGallery}"))).refreshToys();
        }
        catch (Exception e) {
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,TakePictureBean.class, "snap", e);
        }
        finally {
            if (conn != null){
                try{
                    conn.close();
                }
                catch(Exception ex){
                    Trace.log(Utility.FrameworkLogger,Level.SEVERE,TakePictureBean.class, "snap", ex);
                }
            }
        }
        
        return "gotoGallery";
    }
}
