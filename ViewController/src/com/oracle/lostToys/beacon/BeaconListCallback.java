package com.oracle.lostToys.beacon;

import com.oracle.lostToys.EL;
import com.oracle.lostToys.bean.MainBean;

public class BeaconListCallback {
    
    public BeaconListCallback() {
        super();
    }
    
    public void startUpdate(){

        ((MainBean)(EL.eval("applicationScope.Main"))).startBeaconUpdate();
    }
    
    public void add(Beacon beacon){
        ((MainBean)(EL.eval("applicationScope.Main"))).addBeacon(beacon);
    }
    
    public void endUpdate(){
        ((MainBean)(EL.eval("applicationScope.Main"))).endBeaconUpdate();
    }
}
