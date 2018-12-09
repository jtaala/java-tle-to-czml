package com.pelletier.data.providers;

import gov.sandia.phoenix.elements.tle.TLE;

import java.awt.*;

public class TlePathInfoProvider implements PathInfoProvider {

    private final TLE tle;

    public TlePathInfoProvider(TLE tle){
        this.tle = tle;
    };

    @Override
    public int getOrbitalTimeMinutes() {
        return (int) Math.round((24.0 * 60)/Double.parseDouble(this.tle.line2().substring(52,63)));
    }

    @Override
    public boolean getShowProperty() {
        return true;
    }

    @Override
    public double getWidthProperty() {
        return 1;
    }

    @Override
    public Color getColorProperty() {
        return new Color(213, 255, 0, 255);
    }

    @Override
    public double getResolutionProperty() {
        return 120.0;
    }
}
