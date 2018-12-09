package com.pelletier;

import cesiumlanguagewriter.*;
import com.pelletier.czml.writers.billboard.BillboardWriter;
import com.pelletier.czml.writers.billboard.DefaultBillboardInfoProvider;
import com.pelletier.czml.writers.label.DefaultLabelInfoProvider;
import com.pelletier.czml.writers.label.LabelInfoProvider;
import com.pelletier.czml.writers.label.LabelWriter;
import com.pelletier.czml.writers.path.PathInfoProvider;
import com.pelletier.czml.writers.path.SatellitePathWriter;
import com.pelletier.czml.writers.position.SatellitePositionWriter;
import com.pelletier.czml.util.JulianDateUtil;
import com.pelletier.czml.writers.path.TlePathInfoProvider;
import com.pelletier.czml.writers.position.TlePositionProvider;
import gov.sandia.phoenix.elements.tle.TLE;
import gov.sandia.phoenix.propagators.sgp4.WGS84;
import scala.Some;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
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

        CesiumOutputStream output = new CesiumOutputStream(stringWriter);
        output.setPrettyFormatting(true);
        //writeEndSequence writes a '['
        output.writeStartSequence();

        CesiumStreamWriter stream = new CesiumStreamWriter();
        PacketCesiumWriter packetCesiumWriter = stream.openPacket(output);

        packetCesiumWriter.writeId("document");
        packetCesiumWriter.writeVersion("1.0");
        packetCesiumWriter.writeDescriptionProperty("Satellite Orbits");
        packetCesiumWriter.writeAvailability(new TimeInterval(JulianDateUtil.fromDate(startDate), JulianDateUtil.fromDate(endDate)));

        packetCesiumWriter.close();
        packetCesiumWriter.open(output);

        //WRITE LABEL
        LabelWriter labelWriter = new LabelWriter();
        DefaultLabelInfoProvider defaultLabelInfoProvider = new DefaultLabelInfoProvider();
        defaultLabelInfoProvider.setTextProperty(name);
        labelWriter.writeLabel(defaultLabelInfoProvider, packetCesiumWriter);

        //WRITE BILLBOARD
        BillboardWriter billboardWriter = new BillboardWriter();
        billboardWriter.writeBillboard(new DefaultBillboardInfoProvider(), packetCesiumWriter);

        //GENERATE AND WRITE THE POSITION OBJECT OF THE SATELLITE
        SatellitePositionWriter satellitePositionWriter = new SatellitePositionWriter();
        satellitePositionWriter.writeSatellitePositions(new TlePositionProvider(tle), packetCesiumWriter, startDate, endDate, 300);

        //WRITE PATH
        SatellitePathWriter satelliteOrbitWriter = new SatellitePathWriter();
        TlePathInfoProvider tlePathInfoProvider = new TlePathInfoProvider();
        tlePathInfoProvider.setTle(tle);
        tlePathInfoProvider.setWidthProperty(1.0);
        tlePathInfoProvider.setResolutionProperty(120.0);
        tlePathInfoProvider.setColorProperty(Color.GREEN);
        tlePathInfoProvider.setShowProperty(true);

        satelliteOrbitWriter.writeSatelliteOrbit(tlePathInfoProvider, packetCesiumWriter, startDate, endDate);

        packetCesiumWriter.close();
        output.writeEndSequence();
        //view the pretty CZML
        System.out.println(stringWriter.toString());

    }
}
