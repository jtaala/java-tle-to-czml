package com.pelletier.czml.writers.packet;

import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumStreamWriter;
import cesiumlanguagewriter.PacketCesiumWriter;
import cesiumlanguagewriter.TimeInterval;
import com.pelletier.czml.util.JulianDateUtil;

public class DocumentPacketWriter implements PacketWriter {

    private PacketInfoProvider packetInfoProvider;

    @Override
    public void writePacket(CesiumStreamWriter cesiumStreamWriter, CesiumOutputStream cesiumOutputStream) {
        PacketCesiumWriter packetCesiumWriter = cesiumStreamWriter.openPacket(cesiumOutputStream);

        packetCesiumWriter.writeId(packetInfoProvider.getWriteId());
        packetCesiumWriter.writeVersion(packetInfoProvider.getWriteVersion());
        packetCesiumWriter.writeDescriptionProperty(packetInfoProvider.getWriteDescriptionProperty());
        packetCesiumWriter.writeAvailability(new TimeInterval(JulianDateUtil.fromDate(packetInfoProvider.getStartAvailability()), JulianDateUtil.fromDate(packetInfoProvider.getEndAvailability())));

        packetCesiumWriter.close();
    }

    public void setPacketInfoProvider(PacketInfoProvider packetInfoProvider) {
        this.packetInfoProvider = packetInfoProvider;
    }
}
