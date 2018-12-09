package com.pelletier.czml.writers.path;

import cesiumlanguagewriter.*;
import com.pelletier.czml.util.JulianDateUtil;

import java.util.Arrays;
import java.util.Date;


public class SatellitePathWriter {

    public void writeSatelliteOrbit(PathInfoProvider pathInfoProvider, PacketCesiumWriter packetCesiumWriter, Date startDate, Date endDate) {

        PathCesiumWriter pathCesiumWriter = packetCesiumWriter.openPathProperty();

        //write show, width, and resolution
        pathCesiumWriter.writeShowProperty(pathInfoProvider.getShowProperty());
        pathCesiumWriter.writeWidthProperty(pathInfoProvider.getWidthProperty());
        pathCesiumWriter.writeResolutionProperty(pathInfoProvider.getResolutionProperty());

        //write material
        PolylineMaterialCesiumWriter polylineMaterialCesiumWriter = pathCesiumWriter.openMaterialProperty();
        SolidColorMaterialCesiumWriter solidColorMaterialCesiumWriter = polylineMaterialCesiumWriter.openSolidColorProperty();
        ColorCesiumWriter colorCesiumWriter =  solidColorMaterialCesiumWriter.openColorProperty();
        colorCesiumWriter.writeRgba(pathInfoProvider.getColorProperty());
        colorCesiumWriter.close();
        solidColorMaterialCesiumWriter.close();
        polylineMaterialCesiumWriter.close();


        //write lead times
        DoubleCesiumWriter leadTimeWriter = pathCesiumWriter.openLeadTimeProperty();

        //just because something returns a writer doesn't mean you will be using it
        CesiumIntervalListWriter<DoubleCesiumWriter> leadTimeIntervalListWriter = leadTimeWriter.openMultipleIntervals();


        //we have start epoch and end epoch
        final int MINUTES_IN_DAY = 24 * 60;
        final int orbitalTimeMinutes = pathInfoProvider.getOrbitalTimeMinutes();
        final int orbitalTimeSeconds = orbitalTimeMinutes * 60;

        int leftOverMinutes = MINUTES_IN_DAY % orbitalTimeMinutes;
        int numberOfFullOrbits = Math.floorDiv(MINUTES_IN_DAY, orbitalTimeMinutes);

        JulianDate intervalStart = JulianDateUtil.fromDate(startDate);
        JulianDate intervalEnd = intervalStart.addSeconds(leftOverMinutes * 60);

        for(int i = 0; i < (numberOfFullOrbits + 1); i++){
            DoubleCesiumWriter doubleCesiumWriter = leadTimeIntervalListWriter.openInterval(intervalStart, intervalEnd);
            doubleCesiumWriter.writeNumber(Arrays.asList(intervalStart, intervalStart.addSeconds(orbitalTimeSeconds)),Arrays.asList((double) orbitalTimeSeconds, 0.0),0,2);
            doubleCesiumWriter.close();


            intervalStart = intervalEnd;
            intervalEnd = intervalStart.addSeconds(orbitalTimeMinutes * 60);
        }
        leadTimeIntervalListWriter.close();
        leadTimeWriter.close();


        DoubleCesiumWriter trailTimeWriter = pathCesiumWriter.openTrailTimeProperty();
        CesiumIntervalListWriter<DoubleCesiumWriter> trailTimeIntervalListWriter = trailTimeWriter.openMultipleIntervals();

        intervalStart = JulianDateUtil.fromDate(startDate);
        intervalEnd = intervalStart.addSeconds(leftOverMinutes * 60);

        for(int i = 0; i < (numberOfFullOrbits + 1); i++){
            DoubleCesiumWriter doubleCesiumWriter = trailTimeIntervalListWriter.openInterval(intervalStart, intervalEnd);
            doubleCesiumWriter.writeNumber(Arrays.asList(intervalStart, intervalStart.addSeconds(orbitalTimeSeconds)),Arrays.asList(0.0, (double) orbitalTimeSeconds),0,2);
            doubleCesiumWriter.close();


            intervalStart = intervalEnd;
            intervalEnd = intervalStart.addSeconds(orbitalTimeMinutes * 60);
        }
        trailTimeIntervalListWriter.close();
        trailTimeWriter.close();

        pathCesiumWriter.close();


    }
}
