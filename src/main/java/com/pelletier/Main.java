package com.pelletier;

import cesiumlanguagewriter.*;
import cesiumlanguagewriter.advanced.CesiumPropertyWriter;
import com.pelleiter.data.generation.CartesianTimeList;
import com.pelleiter.data.generation.TleToPositionsGenerator;
import com.pelletier.czml.util.JulianDateUtil;
import gov.sandia.phoenix.elements.tle.TLE;
import gov.sandia.phoenix.propagators.sgp4.WGS84;
import scala.Some;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
        PositionCesiumWriter positionCesiumWriter = packetCesiumWriter.openPositionProperty();
        positionCesiumWriter.writeInterpolationAlgorithm(CesiumInterpolationAlgorithm.LAGRANGE);
        positionCesiumWriter.writeInterpolationDegree(5);
        positionCesiumWriter.writeReferenceFrame("INERTIAL");
        positionCesiumWriter.writeCartesian(cartesianTimeList.getTimes(),cartesianTimeList.getPositions());
        positionCesiumWriter.close();

        //WRITE PATH
        PathCesiumWriter pathCesiumWriter = packetCesiumWriter.openPathProperty();
//        pathCesiumWriter.open(output);

        //write show, width, and resolution
        pathCesiumWriter.writeShowProperty(true);
        pathCesiumWriter.writeWidthProperty(1.0);
        pathCesiumWriter.writeResolutionProperty(120.0);

        //write material
        PolylineMaterialCesiumWriter polylineMaterialCesiumWriter = pathCesiumWriter.openMaterialProperty();
        SolidColorMaterialCesiumWriter solidColorMaterialCesiumWriter = polylineMaterialCesiumWriter.openSolidColorProperty();
        ColorCesiumWriter colorCesiumWriter =  solidColorMaterialCesiumWriter.openColorProperty();
        colorCesiumWriter.writeRgba(new Color(213, 255, 0, 255));
        colorCesiumWriter.close();
        solidColorMaterialCesiumWriter.close();
        polylineMaterialCesiumWriter.close();


        //write lead times
        DoubleCesiumWriter leadTimeWriter = pathCesiumWriter.openLeadTimeProperty();

        //just because something returns a writer doesn't mean you will be using it
        CesiumIntervalListWriter<DoubleCesiumWriter> leadTimeIntervalListWriter = leadTimeWriter.openMultipleIntervals();


        //we have start epoch and end epoch
        final int MINUTES_IN_DAY = 24 * 60;
        final int orbitalTimeMinutes = tleToPositionsGenerator.getOrbitalTimeMinutes();
        final int orbitalTimeSeconds = orbitalTimeMinutes * 60;

        int leftOverMinutes = MINUTES_IN_DAY % orbitalTimeMinutes;
        int numberOfFullOrbits = Math.floorDiv(MINUTES_IN_DAY, orbitalTimeMinutes);

        JulianDate intervalStart = JulianDateUtil.fromDate(startDate);
        JulianDate intervalEnd = intervalStart.addSeconds(leftOverMinutes);

        for(int i = 0; i < (numberOfFullOrbits + 1); i++){
            DoubleCesiumWriter doubleCesiumWriter = leadTimeIntervalListWriter.openInterval(intervalStart, intervalEnd);
            doubleCesiumWriter.writeNumber(Arrays.asList(intervalStart, intervalStart.addSeconds(orbitalTimeSeconds)),Arrays.asList((double) orbitalTimeSeconds, 0.0),0,2);
            doubleCesiumWriter.close();


            intervalStart = intervalEnd;
            intervalEnd = intervalStart.addSeconds(orbitalTimeMinutes);
        }
        leadTimeIntervalListWriter.close();
        leadTimeWriter.close();


        DoubleCesiumWriter trailTimeWriter = pathCesiumWriter.openTrailTimeProperty();
        CesiumIntervalListWriter<DoubleCesiumWriter> trailTimeIntervalListWriter = trailTimeWriter.openMultipleIntervals();

        intervalStart = JulianDateUtil.fromDate(startDate);
        intervalEnd = intervalStart.addSeconds(leftOverMinutes);

        for(int i = 0; i < (numberOfFullOrbits + 1); i++){
            DoubleCesiumWriter doubleCesiumWriter = trailTimeIntervalListWriter.openInterval(intervalStart, intervalEnd);
            doubleCesiumWriter.writeNumber(Arrays.asList(intervalStart, intervalStart.addSeconds(orbitalTimeSeconds)),Arrays.asList(0.0, (double) orbitalTimeSeconds),0,2);
            doubleCesiumWriter.close();


            intervalStart = intervalEnd;
            intervalEnd = intervalStart.addSeconds(orbitalTimeMinutes);
        }
        trailTimeIntervalListWriter.close();
        trailTimeWriter.close();

        pathCesiumWriter.close();

        packetCesiumWriter.close();
        output.writeEndSequence();
        //view the pretty CZML
        System.out.println(stringWriter.toString());

    }
}
