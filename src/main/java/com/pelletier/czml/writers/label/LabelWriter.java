package com.pelletier.czml.writers.label;

import cesiumlanguagewriter.LabelCesiumWriter;
import cesiumlanguagewriter.PacketCesiumWriter;

public class LabelWriter {

    public void writeLabel(LabelInfoProvider labelInfoProvider, PacketCesiumWriter packetCesiumWriter){

        LabelCesiumWriter labelCesiumWriter = packetCesiumWriter.openLabelProperty();
        labelCesiumWriter.writeFontProperty(labelInfoProvider.getFontProperty());
        labelCesiumWriter.writeHorizontalOriginProperty(labelInfoProvider.getHorizontalOriginProperty());
        labelCesiumWriter.writeOutlineWidthProperty(labelInfoProvider.getOutlineWidthProperty());
        labelCesiumWriter.writeTextProperty(labelInfoProvider.getTextProperty());
        labelCesiumWriter.writeShowProperty(labelInfoProvider.getShowProperty());
        labelCesiumWriter.writePixelOffsetProperty(labelInfoProvider.getPixelOffsetXProperty(), labelInfoProvider.getPixelOffsetYProperty());
        labelCesiumWriter.close();
    }
}
