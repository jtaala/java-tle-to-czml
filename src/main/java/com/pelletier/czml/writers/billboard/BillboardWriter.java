package com.pelletier.czml.writers.billboard;

import cesiumlanguagewriter.BillboardCesiumWriter;
import cesiumlanguagewriter.PacketCesiumWriter;

public class BillboardWriter {

    private BillboardInfoProvider billboardInfoProvider;

    public void writeBillboard(PacketCesiumWriter packetCesiumWriter){
        BillboardCesiumWriter billboardCesiumWriter = packetCesiumWriter.openBillboardProperty();
        billboardCesiumWriter.writeScaleProperty(billboardInfoProvider.getScaleProperty());
        billboardCesiumWriter.writeImageProperty(billboardInfoProvider.getImageProperty());
        billboardCesiumWriter.writeShowProperty(billboardInfoProvider.getShowProperty());
        billboardCesiumWriter.close();
    }

    public void setBillboardInfoProvider(BillboardInfoProvider billboardInfoProvider) {
        this.billboardInfoProvider = billboardInfoProvider;
    }
}
