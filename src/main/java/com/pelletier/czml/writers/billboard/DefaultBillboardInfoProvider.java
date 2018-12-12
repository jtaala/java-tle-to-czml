package com.pelletier.czml.writers.billboard;

import cesiumlanguagewriter.CesiumImageFormat;
import cesiumlanguagewriter.CesiumResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class DefaultBillboardInfoProvider implements BillboardInfoProvider {

    private CesiumResource imageProperty;
    private String imageReferenceString;
    private boolean showProperty;
    private double scaleProperty;

    public DefaultBillboardInfoProvider() {

        try{
            ClassLoader classLoader = getClass().getClassLoader();
            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath:images/satellite.png");
            this.imageProperty = CesiumResource.fromStream(resources[0].getInputStream(), CesiumImageFormat.PNG);
        }catch(Exception e){
            e.printStackTrace();
        }

        this.showProperty = true;
        this.scaleProperty = .15;
    }


    @Override
    public String getImageReferenceString() {
        return imageReferenceString;
    }

    public void setImageReferenceString(String imageReferenceString) {
        this.imageReferenceString = imageReferenceString;
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
