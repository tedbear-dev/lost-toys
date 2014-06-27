package com.oracle.lostToys.bean;

import com.oracle.lostToys.data.ToyList;

import com.sun.util.logging.Level;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;

public class ApplicationBean {
    public ApplicationBean() {
        super();
    }
    
    public Boolean getDebug(){
        
        Boolean debug = (Boolean)AdfmfJavaUtilities.evaluateELExpression("#{preferenceScope.application.preferences.debugMode}");
        return debug; 
    }
}
