package com.pelletier;

import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumStreamWriter;
import cesiumlanguagewriter.PacketCesiumWriter;
import cesiumlanguagewriter.TimeInterval;
import com.pelletier.czml.util.JulianDateUtil;
import com.pelletier.czml.writers.billboard.BillboardWriter;
import com.pelletier.czml.writers.billboard.DefaultBillboardInfoProvider;
import com.pelletier.czml.writers.label.DefaultLabelInfoProvider;
import com.pelletier.czml.writers.label.LabelWriter;
import com.pelletier.czml.writers.packet.DocumentPacketInfoProvider;
import com.pelletier.czml.writers.packet.DocumentPacketWriter;
import com.pelletier.czml.writers.packet.SatellitePacketWriter;
import com.pelletier.czml.writers.path.SatellitePathWriter;
import com.pelletier.czml.writers.path.TlePathInfoProvider;
import com.pelletier.czml.writers.position.PositionInfoProvider;
import com.pelletier.czml.writers.position.SatellitePositionWriter;
import com.pelletier.czml.writers.position.TlePositionProvider;
import gov.sandia.phoenix.elements.tle.TLE;
import gov.sandia.phoenix.propagators.sgp4.WGS84;
import scala.Some;

import java.awt.*;
import java.io.StringWriter;
import java.util.Date;

public class Main {


    public static void main(String[] args) throws Exception{


        //generate values for the next 24 hours
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + (1000 * 60 * 60 * 24));

        String name = "ISS (ZARYA)";
        String tleLine1 = "1 25544U 98067A   18328.62139255  .00002015  00000-0  37850-4 0  9990";
        String tleLine2 = "2 25544  51.6419 299.2200 0005234  79.7896  74.9554 15.54014027143463";

        TLE tle = new TLE(new Some<String>(name), tleLine1, tleLine2, WGS84.getInstance());

        StringWriter stringWriter = new StringWriter();

        CesiumOutputStream cesiumOutputStream = new CesiumOutputStream(stringWriter);
        cesiumOutputStream.setPrettyFormatting(true);
        //writeEndSequence writes a '['
        cesiumOutputStream.writeStartSequence();

        CesiumStreamWriter cesiumStreamWriter = new CesiumStreamWriter();


        //WRITE REQUIRED CZML DOCUMENT PACKET
        DocumentPacketWriter documentPacketWriter = new DocumentPacketWriter();
        documentPacketWriter.setPacketInfoProvider(new DocumentPacketInfoProvider());
        documentPacketWriter.writePacket(cesiumStreamWriter, cesiumOutputStream);

        //CONFIGURE AND WRITE SATELLITE PACKET

        //WRITE LABEL
        LabelWriter labelWriter = new LabelWriter();
        DefaultLabelInfoProvider defaultLabelInfoProvider = new DefaultLabelInfoProvider();
        defaultLabelInfoProvider.setTextProperty(name);
        labelWriter.setLabelInfoProvider(defaultLabelInfoProvider);

        //WRITE BILLBOARD
        BillboardWriter billboardWriter = new BillboardWriter();
        billboardWriter.setBillboardInfoProvider(new DefaultBillboardInfoProvider());

        //GENERATE AND WRITE THE POSITION OBJECT OF THE SATELLITE
        SatellitePositionWriter satellitePositionWriter = new SatellitePositionWriter();
        TlePositionProvider tlePositionProvider = new TlePositionProvider();
        tlePositionProvider.setTle(tle);
        tlePositionProvider.setStartDate(startDate);
        tlePositionProvider.setEndDate(endDate);
        tlePositionProvider.setTimeStep(300);
        satellitePositionWriter.setPositionProvider(tlePositionProvider);

        //WRITE PATH
        SatellitePathWriter satelliteOrbitWriter = new SatellitePathWriter();
        TlePathInfoProvider tlePathInfoProvider = new TlePathInfoProvider();
        tlePathInfoProvider.setTle(tle);
        tlePathInfoProvider.setStartDate(startDate);
        tlePathInfoProvider.setWidthProperty(1.0);
        tlePathInfoProvider.setResolutionProperty(120.0);
        tlePathInfoProvider.setColorProperty(Color.GREEN);
        tlePathInfoProvider.setShowProperty(true);
        satelliteOrbitWriter.setPathInfoProvider(tlePathInfoProvider);



        SatellitePacketWriter satellitePacketWriter = new SatellitePacketWriter();
        satellitePacketWriter.setLabelWriter(labelWriter);
        satellitePacketWriter.setBillboardWriter(billboardWriter);
        satellitePacketWriter.setSatellitePositionWriter(satellitePositionWriter);
        satellitePacketWriter.setSatellitePathWriter(satelliteOrbitWriter);
        satellitePacketWriter.writePacket(cesiumStreamWriter, cesiumOutputStream);

        cesiumOutputStream.writeEndSequence();
        //view the pretty CZML
        System.out.println(stringWriter.toString());

    }
}
