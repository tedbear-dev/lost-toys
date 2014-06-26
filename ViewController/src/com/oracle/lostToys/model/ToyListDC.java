package com.oracle.lostToys.model;

import java.util.ArrayList;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class ToyListDC {
    public ToyListDC() {
        super();
    }
    
    public BefriendedToy[] getToys(){
        
        ArrayList toys = ((ToyGallery)AdfmfJavaUtilities.evaluateELExpression("#{pageFlowScope.ToyGallery}")).getBefriendedToys();
        return (BefriendedToy[])toys.toArray(new BefriendedToy[toys.size()]);
    }
}
