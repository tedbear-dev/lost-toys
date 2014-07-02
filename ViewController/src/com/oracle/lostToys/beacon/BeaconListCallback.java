package com.oracle.lostToys.beacon;

import com.oracle.lostToys.EL;
import com.oracle.lostToys.bean.MainBean;
import com.oracle.lostToys.data.Beacon;

public class BeaconListCallback {
    
    public BeaconListCallback() {
        super();
    }
    
    public void startUpdate(){
        EL.main().getBeacons().startBeaconUpdate();
    }
    
    public void add(Beacon beacon){
        EL.main().getBeacons().addBeacon(beacon);
    }
    
    public void endUpdate(){
        EL.main().getBeacons().endBeaconUpdate();
    }
}
