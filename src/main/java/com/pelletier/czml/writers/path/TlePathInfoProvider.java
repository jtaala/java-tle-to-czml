package com.pelletier.czml.writers.path;

import gov.sandia.phoenix.elements.tle.TLE;

import java.awt.*;

public class TlePathInfoProvider implements PathInfoProvider {

    private TLE tle;
    private Color colorProperty;
    private double resolutionProperty;
    private double widthProperty;
    private boolean showProperty;


    public void setTle(TLE tle) {
        this.tle = tle;
    }

    @Override
    public int getOrbitalTimeMinutes() {
        return (int) Math.round((24.0 * 60)/Double.parseDouble(this.tle.line2().substring(52,63)));
    }

    @Override
    public Color getColorProperty() {
        return colorProperty;
    }

    public void setColorProperty(Color colorProperty) {
        this.colorProperty = colorProperty;
    }

    @Override
    public double getResolutionProperty() {
        return resolutionProperty;
    }

    public void setResolutionProperty(double resolutionProperty) {
        this.resolutionProperty = resolutionProperty;
    }

    @Override
    public double getWidthProperty() {
        return widthProperty;
    }

    public void setWidthProperty(double widthProperty) {
        this.widthProperty = widthProperty;
    }

    @Override
    public boolean getShowProperty() {
        return showProperty;
    }

    public void setShowProperty(boolean showProperty) {
        this.showProperty = showProperty;
    }
}
