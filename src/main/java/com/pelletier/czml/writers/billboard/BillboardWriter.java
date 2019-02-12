package com.pelletier.czml.writers.billboard;

import cesiumlanguagewriter.BillboardCesiumWriter;
import cesiumlanguagewriter.PacketCesiumWriter;
import cesiumlanguagewriter.Reference;

public class BillboardWriter {

    private BillboardInfoProvider billboardInfoProvider;

    public void writeBillboard(PacketCesiumWriter packetCesiumWriter){
        BillboardCesiumWriter billboardCesiumWriter = packetCesiumWriter.openBillboardProperty();
        billboardCesiumWriter.writeScaleProperty(billboardInfoProvider.getScaleProperty());

        if(billboardInfoProvider.getImageReferenceString() != null && !billboardInfoProvider.getImageReferenceString().isEmpty()){
            billboardCesiumWriter.writeImagePropertyReference(billboardInfoProvider.getImageReferenceString());
        }else{
            billboardCesiumWriter.writeImageProperty(billboardInfoProvider.getImageProperty());
        }
        billboardCesiumWriter.writeShowProperty(billboardInfoProvider.getShowProperty());
        billboardCesiumWriter.close();
    }

    public void setBillboardInfoProvider(BillboardInfoProvider billboardInfoProvider) {
        this.billboardInfoProvider = billboardInfoProvider;
    }
}
