package com.pelletier.czml.writers.label;

import cesiumlanguagewriter.LabelCesiumWriter;
import cesiumlanguagewriter.PacketCesiumWriter;

public class LabelWriter {

    private LabelInfoProvider labelInfoProvider;

    public void writeLabel(PacketCesiumWriter packetCesiumWriter){

        LabelCesiumWriter labelCesiumWriter = packetCesiumWriter.openLabelProperty();
        labelCesiumWriter.writeFontProperty(labelInfoProvider.getFontProperty());
        labelCesiumWriter.writeHorizontalOriginProperty(labelInfoProvider.getHorizontalOriginProperty());
        labelCesiumWriter.writeOutlineWidthProperty(labelInfoProvider.getOutlineWidthProperty());
        labelCesiumWriter.writeTextProperty(labelInfoProvider.getTextProperty());
        labelCesiumWriter.writeShowProperty(labelInfoProvider.getShowProperty());
        labelCesiumWriter.writePixelOffsetProperty(labelInfoProvider.getPixelOffsetXProperty(), labelInfoProvider.getPixelOffsetYProperty());
        labelCesiumWriter.close();
    }

    public void setLabelInfoProvider(LabelInfoProvider labelInfoProvider) {
        this.labelInfoProvider = labelInfoProvider;
    }
}
