package com.pelletier.czml.writers.billboard;

import cesiumlanguagewriter.CesiumImageFormat;
import cesiumlanguagewriter.CesiumResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class DefaultBillboardInfoProvider implements BillboardInfoProvider {

    private CesiumResource imageProperty;
    private boolean showProperty;
    private double scaleProperty;

    public DefaultBillboardInfoProvider() {

        try{
            ClassLoader classLoader = getClass().getClassLoader();
            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
            //TODO: figure out the right path for this to avoid this hack, Spring Boot puts it in BOOT-INF and it doesn't load the way I would expect
            Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:images/*.png");

            this.imageProperty = CesiumResource.fromStream(resources[0].getInputStream(), CesiumImageFormat.PNG);
        }catch(Exception e){
            e.printStackTrace();
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
