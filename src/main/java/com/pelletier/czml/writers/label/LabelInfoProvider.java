package com.pelletier.czml.writers.label;

import cesiumlanguagewriter.CesiumHorizontalOrigin;

public interface LabelInfoProvider {

    String getFontProperty();
    CesiumHorizontalOrigin getHorizontalOriginProperty();
    double getOutlineWidthProperty();
    String getTextProperty();
    boolean getShowProperty();
    double getPixelOffsetXProperty();
    double getPixelOffsetYProperty();
}
