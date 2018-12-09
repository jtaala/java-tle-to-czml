package com.pelletier.czml.writers.billboard;

import cesiumlanguagewriter.BillboardCesiumWriter;
import cesiumlanguagewriter.PacketCesiumWriter;

public class BillboardWriter {

    public void writeBillboard(BillboardInfoProvider billboardInfoProvider, PacketCesiumWriter packetCesiumWriter){
        BillboardCesiumWriter billboardCesiumWriter = packetCesiumWriter.openBillboardProperty();
        billboardCesiumWriter.writeScaleProperty(billboardInfoProvider.getScaleProperty());
        billboardCesiumWriter.writeImageProperty(billboardInfoProvider.getImageProperty());
        billboardCesiumWriter.writeShowProperty(billboardInfoProvider.getShowProperty());
        billboardCesiumWriter.close();
    }
}
