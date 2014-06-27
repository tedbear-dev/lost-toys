package com.oracle.lostToys.mobile;

import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class TestBean {
    public TestBean() {
    }

    public String vibrate() {
        AdfmfContainerUtilities.invokeContainerJavaScriptFunction(
            "com.oracle.lostToys.main",
            "window.beaconPlugin.vibrate",
            new Object[] {
                new Integer(
                    Integer.parseInt(
                        AdfmfJavaUtilities.evaluateELExpression("#{preferenceScope.application.preferences.soundID}").toString()
                    )
                )
            }
        );
        return null;
    }
}
