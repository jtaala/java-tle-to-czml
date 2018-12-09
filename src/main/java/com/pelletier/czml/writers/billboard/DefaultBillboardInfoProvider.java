package com.pelletier.czml.writers.billboard;

import cesiumlanguagewriter.CesiumImageFormat;
import cesiumlanguagewriter.CesiumResource;

import java.io.*;

public class DefaultBillboardInfoProvider implements BillboardInfoProvider {

    private CesiumResource imageProperty;
    private boolean showProperty;
    private double scaleProperty;

    public DefaultBillboardInfoProvider() {

        try{
            FileInputStream fileInputStream = new FileInputStream("satellite.png");
            this.imageProperty = CesiumResource.fromStream(fileInputStream, CesiumImageFormat.PNG);
        }catch(Exception e){
            System.out.println("Unable to find satellite.png. You must set imageProperty on DefaultBillboardInfoProvider if you want your satellite to have an image.");
        }

        this.showProperty = true;
        this.scaleProperty = .15;
    }

    public boolean getShowProperty() {
        return showProperty;
    }

    public void setShowProperty(boolean showProperty) {
        this.showProperty = showProperty;
    }

    @Override
    public double getScaleProperty() {
        return scaleProperty;
    }

    public void setScaleProperty(double scaleProperty) {
        this.scaleProperty = scaleProperty;
    }

    @Override
    public CesiumResource getImageProperty() {
        return imageProperty;
    }

    public void setImageProperty(CesiumResource imageProperty) {
        this.imageProperty = imageProperty;
    }
}
