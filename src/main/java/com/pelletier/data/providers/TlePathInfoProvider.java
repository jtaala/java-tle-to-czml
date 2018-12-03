package com.pelletier.data.providers;

import gov.sandia.phoenix.elements.tle.TLE;

public class TlePathInfoProvider implements PathInfoProvider {

    private final TLE tle;

    public TlePathInfoProvider(TLE tle){
        this.tle = tle;
    };

    @Override
    public int getOrbitalTimeMinutes() {
        return (int) Math.round((24.0 * 60)/Double.parseDouble(this.tle.line2().substring(52,63)));
    }
}
