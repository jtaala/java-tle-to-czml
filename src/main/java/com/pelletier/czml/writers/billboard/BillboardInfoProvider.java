package com.pelletier.czml.writers.billboard;

import cesiumlanguagewriter.CesiumResource;

public interface BillboardInfoProvider {

    String getImageReferenceString();
    CesiumResource getImageProperty();
    double getScaleProperty();
    boolean getShowProperty();
}
