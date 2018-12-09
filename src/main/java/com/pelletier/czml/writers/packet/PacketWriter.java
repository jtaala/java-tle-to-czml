package com.pelletier.czml.writers.packet;

import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumStreamWriter;

public interface PacketWriter {

    void writePacket(CesiumStreamWriter cesiumStreamWriter, CesiumOutputStream cesiumOutputStream);

}
