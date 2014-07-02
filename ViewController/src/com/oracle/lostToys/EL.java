package com.oracle.lostToys;

import com.oracle.lostToys.bean.MainBean;
import com.oracle.lostToys.bean.ToyGalleryBean;
import com.oracle.lostToys.data.Toy;

import com.sun.util.logging.Level;

import javax.el.PropertyNotFoundException;

import oracle.adfmf.bindings.dbf.AmxMethodActionBinding;
import oracle.adfmf.dc.bean.ConcreteJavaBeanObject;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class EL {
    public EL() {
        super();
    }
    
    public static MainBean main(){
        
        try {
            return ((MainBean)(EL.eval("applicationScope.Main")));
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EL.class, "main", ex);    
        }
        
        return null;
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
        catch(PropertyNotFoundException pnfe){            
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EL.class, "eval", "Property refrenced in '" + el + "' does not exist, yet?");    
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EL.class, "eval", ex);    
        }
        return obj;
    }
    
    public static boolean exists(String name){

        Object obj = null;
        
        try{
            obj = AdfmfJavaUtilities.evaluateELExpression("#{(bindings != null && bindings." + name + " != null) ? true : false}");
        }
        catch(PropertyNotFoundException pnfe){
            obj = Boolean.FALSE;
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EL.class, "exists", "Binding " + name + " does not exist, yet?");    
        }
        catch(Exception ex){
            Trace.log(Utility.FrameworkLogger,Level.SEVERE,EL.class, "exists", ex);    
        }
        return (obj == null)?false:((Boolean)obj).booleanValue();    
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
