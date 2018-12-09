package com.pelletier.czml.writers.packet;

import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumStreamWriter;
import cesiumlanguagewriter.PacketCesiumWriter;
import com.pelletier.czml.writers.billboard.BillboardWriter;
import com.pelletier.czml.writers.label.LabelWriter;
import com.pelletier.czml.writers.path.SatellitePathWriter;
import com.pelletier.czml.writers.position.SatellitePositionWriter;

public class SatellitePacketWriter implements PacketWriter {

    private LabelWriter labelWriter;
    private BillboardWriter billboardWriter;
    private SatellitePositionWriter satellitePositionWriter;
    private SatellitePathWriter satellitePathWriter;



    @Override
    public void writePacket(CesiumStreamWriter cesiumStreamWriter, CesiumOutputStream cesiumOutputStream) {
        PacketCesiumWriter packetCesiumWriter = cesiumStreamWriter.openPacket(cesiumOutputStream);


        if(this.labelWriter != null){
            labelWriter.writeLabel(packetCesiumWriter);
        }
        if(this.billboardWriter != null){
            this.billboardWriter.writeBillboard(packetCesiumWriter);
        }
        if(this.satellitePositionWriter != null){
            this.satellitePositionWriter.writeSatellitePositions(packetCesiumWriter);
        }
        if(this.satellitePathWriter != null){
            this.satellitePathWriter.writeSatelliteOrbit(packetCesiumWriter);
        }

        packetCesiumWriter.close();
    }


    public void setLabelWriter(LabelWriter labelWriter) {
        this.labelWriter = labelWriter;
    }

    public void setBillboardWriter(BillboardWriter billboardWriter) {
        this.billboardWriter = billboardWriter;
    }

    public void setSatellitePositionWriter(SatellitePositionWriter satellitePositionWriter) {
        this.satellitePositionWriter = satellitePositionWriter;
    }

    public void setSatellitePathWriter(SatellitePathWriter satellitePathWriter) {
        this.satellitePathWriter = satellitePathWriter;
    }
}
