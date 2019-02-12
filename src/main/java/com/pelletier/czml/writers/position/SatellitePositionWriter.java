package com.pelletier.czml.writers.position;

import cesiumlanguagewriter.*;
import com.pelletier.czml.data.CartesianTimeList;
import com.pelletier.czml.util.JulianDateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SatellitePositionWriter {

    private PositionInfoProvider positionProvider;

    public void writeSatellitePositions(PacketCesiumWriter packetCesiumWriter){
        CartesianTimeList cartesianTimeList = generateTimeList(positionProvider);
        //getting the position writer
        PositionCesiumWriter positionCesiumWriter = packetCesiumWriter.openPositionProperty();

        positionCesiumWriter.writeInterpolationAlgorithm(CesiumInterpolationAlgorithm.LAGRANGE);
        positionCesiumWriter.writeInterpolationDegree(5);
        positionCesiumWriter.writeReferenceFrame("INERTIAL");
        positionCesiumWriter.writeCartesian(cartesianTimeList.getTimes(),cartesianTimeList.getPositions());
        positionCesiumWriter.close();
    }


    private CartesianTimeList generateTimeList(PositionInfoProvider positionProvider){
        CartesianTimeList cartographicTimeList = new CartesianTimeList();

        List<JulianDate> positionTimes = new ArrayList<>();
        List<Cartesian> positions = new ArrayList<>();

        Date dateIterator = positionProvider.getStartDate();
        JulianDate julianDate = JulianDateUtil.fromDate(positionProvider.getStartDate());
        do{
            positions.add(new Cartesian(positionProvider.getX(dateIterator), positionProvider.getY(dateIterator), positionProvider.getZ(dateIterator)));
            positionTimes.add(julianDate);

            //I hate that I do all this to add a few seconds to a date. Why Java?
            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(dateIterator); // sets calendar time/date
            cal.add(Calendar.SECOND, positionProvider.getTimeStep()); // adds one hour
            dateIterator = cal.getTime();

            julianDate = julianDate.addSeconds(positionProvider.getTimeStep());
        }while(dateIterator.before(positionProvider.getEndDate()));

        cartographicTimeList.setPositions(positions);
        cartographicTimeList.setTimes(positionTimes);

        return cartographicTimeList;
    }

    public void setPositionProvider(PositionInfoProvider positionProvider) {
        this.positionProvider = positionProvider;
    }
}
