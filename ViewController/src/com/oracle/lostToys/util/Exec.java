package com.oracle.lostToys.util;

import com.oracle.lostToys.data.Toy;

import oracle.adfmf.bindings.dbf.AmxMethodActionBinding;
import oracle.adfmf.dc.bean.ConcreteJavaBeanObject;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class Exec {
    public Exec() {
        super();
    }
    
    public static Object methodBinding(String name){
        
        AmxMethodActionBinding ve = (AmxMethodActionBinding)AdfmfJavaUtilities.evaluateELExpression("#{bindings." + name + "}");
        if(ve == null) return null;
        
        ConcreteJavaBeanObject obj = (ConcreteJavaBeanObject)ve.execute();
        if(obj == null) return null;
        
        return obj.getInstance();
    }
    
    public static Object methodBinding(String name,String[] paramNames,Object[] paramValues){

        for(int i = 0;i < paramNames.length;i++){        
            AdfmfJavaUtilities.setELValue("#{bindings." + name + "_" + paramNames[i] + "}", (i < paramValues.length) ? paramValues[i] : null);
        }
        
        return methodBinding(name);
    }
}
