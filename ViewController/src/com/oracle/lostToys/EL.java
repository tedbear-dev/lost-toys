package com.oracle.lostToys;

import com.oracle.lostToys.bean.ToyGalleryBean;
import com.oracle.lostToys.data.Toy;

import com.sun.util.logging.Level;

import oracle.adfmf.bindings.dbf.AmxMethodActionBinding;
import oracle.adfmf.dc.bean.ConcreteJavaBeanObject;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class EL {
    public EL() {
        super();
    }
    
    public static void set(String el,Object obj){
        
        try {
            AdfmfJavaUtilities.setELValue("#{" + el + "}",obj);    
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EL.class, "set", ex);    
        }

    }
    
    public static Object eval(String el){
        
        Object obj = null;
        
        try{
            obj = AdfmfJavaUtilities.evaluateELExpression("#{" + el + "}");
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EL.class, "eval", ex);    
        }
        return obj;
    }
    
    public static Object exec(String name){
        
        Object rc = null;
        
        try {
            AmxMethodActionBinding ve = (AmxMethodActionBinding)eval("bindings." + name);
            if(ve != null){
            
                ConcreteJavaBeanObject obj = (ConcreteJavaBeanObject)ve.execute();
                if(obj != null) rc = obj.getInstance();
            }
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EL.class, "exec", ex);    
        }
        
        return rc;
    }
    
    public static Object exec(String name,String[] paramNames,Object[] paramValues){

        for(int i = 0;i < paramNames.length;i++){        
            set("bindings." + name + "_" + paramNames[i], (i < paramValues.length) ? paramValues[i] : null);
        }
        
        return exec(name);
    }
}
