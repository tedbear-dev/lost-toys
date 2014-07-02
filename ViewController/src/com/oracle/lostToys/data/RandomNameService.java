package com.oracle.lostToys.data;

import com.oracle.lostToys.EL;

import com.sun.util.logging.Level;

import java.util.HashMap;

import oracle.adfmf.dc.ws.rest.RestServiceAdapter;
import oracle.adfmf.framework.api.JSONBeanSerializationHelper;
import oracle.adfmf.framework.api.Model;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class RandomNameService {
    public RandomNameService() {
        super();
    }
    
    public static Name get(){
        
        Name name = null;
        
        RestServiceAdapter restServiceAdapter = Model.createRestServiceAdapter();

        restServiceAdapter.clearRequestProperties();
        restServiceAdapter.setConnectionName("Uxh");
        restServiceAdapter.setRequestType(RestServiceAdapter.REQUEST_TYPE_GET);
        restServiceAdapter.setRetryLimit(0);
        restServiceAdapter.setRequestURI("/randomName");

        String response = "";

        try {
            response = restServiceAdapter.send("");
            ApexJsonResponse result = (ApexJsonResponse)(new JSONBeanSerializationHelper()).fromJSON(ApexJsonResponse.class, response);

            JSONArray arr = result.getItems();
            if(arr.length() > 0){
                JSONObject obj = arr.getJSONObject(0); 
                name = (Name)JSONBeanSerializationHelper.fromJSON(Name.class, obj);
                
                Trace.log(Utility.FrameworkLogger,Level.SEVERE,RandomNameService.class, "get", "got:"+name.getAdjective()+" "+name.getGirlName()+" "+name.getBoyName());    
            }
        }
        catch (Exception ex) {
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,RandomNameService.class, "get", ex);    
        }
        
        return name;
    }
}
