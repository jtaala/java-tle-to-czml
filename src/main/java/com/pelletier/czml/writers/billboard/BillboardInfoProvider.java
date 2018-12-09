package com.pelletier.czml.writers.billboard;

import cesiumlanguagewriter.CesiumResource;

public interface BillboardInfoProvider {

    CesiumResource getImageProperty();
    double getScaleProperty();
    boolean getShowProperty();
}
