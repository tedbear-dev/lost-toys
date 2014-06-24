//
//  BeaconPlugin.js
//
//  Copyright (c) 2014 Oracle Retail. All rights reserved.
//


window.beaconPlugin = {
    
    startMonitoring: function(proximityUUID,regionID,fake)
    {
    
        if(fake){    
            // Fake some data that changes at random
            window.beaconPlugin.updater = setInterval(function(){
            
                window.beaconPlugin.refreshBeacons([
                    {"major":58823,"minor":47846,"rssi":Math.round(Math.random()*50.0) - 50,"accuracy":Math.random() * 3.0,"proximityUUID":proximityUUID,"proximity":1},
                    {"major":41922,"minor":29992,"rssi":Math.round(Math.random()*50.0) - 75,"accuracy":Math.random() * 3.0,"proximityUUID":proximityUUID,"proximity":2},
                    {"major":28028,"minor":4937,"rssi":Math.round(Math.random()*50.0) - 100,"accuracy":Math.random() * 3.0,"proximityUUID":proximityUUID,"proximity":3}
                ],regionID); 
                
            },1000);           
        }
        else{
            var l_proximityUUID = proximityUUID;
            var l_regionID = regionID;
            
            beaconPlugin.startMonitoringForRegion(l_proximityUUID, l_regionID, function(param){
                
                beaconPlugin.startRangingBeaconsInRegion(l_proximityUUID, l_regionID,
                    function (inner_param)
                    {
                        adf.mf.log.Application.logp(adf.mf.log.level.INFO,"beaconPlugin","startRangingBeaconsInRegion SUCCESS: " + JSON.stringify(inner_param));
                    });
            });
        }
    },

    test: function(){
        window.Cordova.exec(
            function(param){adf.mf.log.Application.logp(adf.mf.log.level.INFO,"beaconPlugin","test: '" + param + "'");},
            function(error){alert("beaconPlugin.test ERROR: " + error);},
            "BeaconPlugin",
            "test",
            [txt]
        );
    },
    
    startMonitoringForRegion: function(proximityUUID, regionID, callback){
    
        var l_callback = callback;
        
        window.Cordova.exec(
            function(param){l_callback(param);},
            function(error){alert("beaconPlugin.startMonitoringForRegion ERROR: " + error);},
            "BeaconPlugin",
            "startMonitoringForRegion",
            [proximityUUID, regionID, "window.beaconPlugin.locationManagerDelegate"]
        );  
    },
    
    startRangingBeaconsInRegion: function(proximityUUID, regionID, callback){
    
        var l_callback = callback;
    
        window.Cordova.exec(
            function(param){l_callback(param);},
            function(error){alert("beaconPlugin.startRangingBeaconsInRegion ERROR: " + error);},
            "BeaconPlugin",
            "startRangingBeaconsInRegion",
            [proximityUUID, regionID, "window.beaconPlugin.locationManagerDelegate"]
        );  
    },
    
    refreshBeacons: function(beacons,regionID){
        
            var success = function(request,response){
                adf.mf.log.Application.logp(adf.mf.log.level.INFO, "window.beaconPlugin", "refreshBeacons", 
                "Invoked method: request=" + JSON.stringify(request) + ", response=" + JSON.stringify(response));                 
             };
             
             var error = function(request,response){
                 alert("Error invoking method in refreshBeacons: request=" + JSON.stringify(request) + ", response=" + JSON.stringify(response));
             };
             
             adf.mf.api.invokeMethod("com.oracle.demo.beacon.monitor.mobile.RangeBeanProxy","startBeaconListUpdate",success,error);
             for(var i = 0;i < beacons.length;i++){
                adf.mf.api.invokeMethod("com.oracle.demo.beacon.monitor.mobile.RangeBeanProxy","addBeaconToList", {
                        ".type":"com.oracle.demo.beacon.monitor.mobile.BeaconBean",
                        "major":beacons[i].major,
                        "minor":beacons[i].minor,
                        "rssi":beacons[i].rssi,
                        "accuracy":beacons[i].accuracy,
                        "proximityUUID":beacons[i].proximityUUID,
                        "proximity":beacons[i].proximity ,
                        "regionID":regionID
                    },
                    success,error
                );
             }
             adf.mf.api.invokeMethod("com.oracle.demo.beacon.monitor.mobile.RangeBeanProxy","endBeaconListUpdate",success,error);    
    },
    
    locationManagerDelegate: 
    {
        locationManagerDidUpdateToLocationFromLocation: function(newLocation, oldLocation)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidUpdateToLocationFromLocation", 
                "newLocation: " + newLocation + ", oldLocation: " + oldLocation + ", working!");
        },
        
        locationManagerDidUpdateLocations: function(locations)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidUpdateLocations", 
                "locations: " + locations + ", working!");
        },
        
        locationManagerDidUpdateHeading: function(heading)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidUpdateHeading", 
                "heading: " + heading + ", working!");
        },
        
        locationManagerDidDetermineState: function(state, region)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidDetermineState", 
                "state: " + state + ", region: " + region + ", working!");
        },
        
        locationManagerDidRangeBeaconsInRegion: function(beacons, region)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidRangeBeaconsInRegion", 
                "beacons: " + beacons + ", region: " + region + ", working!");
            
            try {
                window.beaconPlugin.refreshBeacons(JSON.parse(beacons),JSON.parse(region).identifier);
            }
            catch(ex){
                adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidRangeBeaconsInRegion", 
                "ERROR refreshing beacons: " + ex);                
            }
        },
        
        locationManagerRangingBeaconsDidFailForRegionWithError: function(region, error)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerRangingBeaconsDidFailForRegionWithError", 
                "region: " + region + ", error: " + error + ", working!");
                
                alert("Couldn't range Beacons for region '" + region + "', with error '"+error+"'. Try turning it off and on again.");
        },
        
        locationManagerDidEnterRegion: function(region)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidEnterRegion", 
                "region: " + region + ", working!");
        },
        
        locationManagerDidExitRegion: function(region)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidExitRegion", 
                "region: " + region + ", working!");
        },
        
        locationManagerDidFailWithError: function(error)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidFailWithError", 
                "error: " + error + ", working!");
        },
        
        locationManagerMonitoringDidFailForRegionWithError: function(region, error)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerMonitoringDidFailForRegionWithError", 
                "region: " + region + ", error: " + error + ", working!");
        },
        
        locationManagerDidChangeAuthorizationStatus: function(status)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidChangeAuthorizationStatus", 
                "status: " + status + ", working!");
        },
        
        locationManagerDidStartMonitoringForRegion: function(region)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidStartMonitoringForRegion", 
                "region: " + region + ", working!");
        },
        
        locationManagerDidPauseLocationUpdates: function()
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidPauseLocationUpdates", 
                "" + ", working!");
        },
        
        locationManagerDidResumeLocationUpdates: function()
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidResumeLocationUpdates", 
                "" + ", working!");
        },
        
        locationManagerDidFinishDeferredUpdatesWithError: function(error)
        {
            adf.mf.log.Application.logp(adf.mf.log.level.INFO, "locationManagerDelegate", "locationManagerDidFinishDeferredUpdatesWithError", 
                "error: " + error + ", working!");
        }
    }
};

(function()
{
})();

