package com.pelletier;

import cesiumlanguagewriter.*;
import com.pelleiter.data.generation.CartesianTimeList;
import com.pelleiter.data.generation.TleToPositionsGenerator;
import com.pelletier.czml.util.JulianDateUtil;
import gov.sandia.phoenix.elements.tle.TLE;
import gov.sandia.phoenix.propagators.sgp4.WGS84;
import scala.Some;

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
        packetCesiumWriter.writeDescriptionProperty("ISS Orbit");
        packetCesiumWriter.writeAvailability(new TimeInterval(JulianDateUtil.fromDate(startDate), JulianDateUtil.fromDate(endDate)));

        packetCesiumWriter.close();
        packetCesiumWriter.open(output);

        //WRITE LABEL
        LabelCesiumWriter labelCesiumWriter = packetCesiumWriter.getLabelWriter();
        labelCesiumWriter.open(output);
        labelCesiumWriter.writeFontProperty("11pt Lucida Console");
        labelCesiumWriter.writeHorizontalOriginProperty(CesiumHorizontalOrigin.LEFT);
        labelCesiumWriter.writeOutlineWidthProperty(2);
        labelCesiumWriter.writeTextProperty(name);
        labelCesiumWriter.writeShowProperty(true);

        labelCesiumWriter.writePixelOffsetProperty(15, 0);

        labelCesiumWriter.close();


        //WRITE BILLBOARD
        BillboardCesiumWriter billboardCesiumWriter = packetCesiumWriter.getBillboardWriter();
        billboardCesiumWriter.open(output);

        FileInputStream fileInputStream = new FileInputStream(new File("satellite-png-40929.png"));
        billboardCesiumWriter.writeScaleProperty(.15);
        billboardCesiumWriter.writeImageProperty(CesiumResource.fromStream(fileInputStream, CesiumImageFormat.PNG));
        billboardCesiumWriter.writeShowProperty(true);
        billboardCesiumWriter.close();

        //GENERATE AND WRITE THE POSITION OBJECT OF THE SATELLITE
        TleToPositionsGenerator tleToPositionsGenerator = new TleToPositionsGenerator(tle);

        CartesianTimeList cartesianTimeList = tleToPositionsGenerator.generateTimeList(startDate, endDate, 300);
        //getting the position writer
        PositionCesiumWriter positionCesiumWriter = packetCesiumWriter.getPositionWriter();
        positionCesiumWriter.open(output);
        positionCesiumWriter.writeInterpolationAlgorithm(CesiumInterpolationAlgorithm.LAGRANGE);
        positionCesiumWriter.writeInterpolationDegree(5);
        positionCesiumWriter.writeReferenceFrame("INERTIAL");
        positionCesiumWriter.writeCartesian(cartesianTimeList.getTimes(),cartesianTimeList.getPositions());
        positionCesiumWriter.close();

        packetCesiumWriter.close();
        output.writeEndSequence();
        //view the pretty CZML
        System.out.println(stringWriter.toString());

    }
}
