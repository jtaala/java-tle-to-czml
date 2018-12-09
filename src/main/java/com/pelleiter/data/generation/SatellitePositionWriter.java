package com.pelleiter.data.generation;

import cesiumlanguagewriter.*;
import com.pelletier.czml.util.JulianDateUtil;
import com.pelletier.data.providers.PositionProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SatellitePositionWriter {

    public void writeSatellitePositions(PositionProvider positionProvider, PacketCesiumWriter packetCesiumWriter, Date startDate, Date endDate, int timeStep){
        CartesianTimeList cartesianTimeList = generateTimeList(positionProvider, startDate, endDate, timeStep);
        //getting the position writer
        PositionCesiumWriter positionCesiumWriter = packetCesiumWriter.openPositionProperty();

        positionCesiumWriter.writeInterpolationAlgorithm(CesiumInterpolationAlgorithm.LAGRANGE);
        positionCesiumWriter.writeInterpolationDegree(5);
        positionCesiumWriter.writeReferenceFrame("INERTIAL");
        positionCesiumWriter.writeCartesian(cartesianTimeList.getTimes(),cartesianTimeList.getPositions());
        positionCesiumWriter.close();
    }


    private CartesianTimeList generateTimeList(PositionProvider positionProvider, Date startDate, Date endDate, int timeStep){
        CartesianTimeList cartographicTimeList = new CartesianTimeList();

        List<JulianDate> positionTimes = new ArrayList<>();
        List<Cartesian> positions = new ArrayList<>();

        Date dateIterator = startDate;
        JulianDate julianDate = JulianDateUtil.fromDate(startDate);
        do{
            positions.add(new Cartesian(positionProvider.getX(dateIterator), positionProvider.getY(dateIterator), positionProvider.getZ(dateIterator)));
            positionTimes.add(julianDate);

            //I hate that I do all this to add a few seconds to a date. Why Java?
            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(dateIterator); // sets calendar time/date
            cal.add(Calendar.SECOND, timeStep); // adds one hour
            dateIterator = cal.getTime();

            julianDate = julianDate.addSeconds(timeStep);
        }while(dateIterator.before(endDate));

        cartographicTimeList.setPositions(positions);
        cartographicTimeList.setTimes(positionTimes);

        return cartographicTimeList;
    }

}
