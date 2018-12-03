package com.pelleiter.data.generation;

import agi.foundation.compatibility.DateTimeHelper;
import cesiumlanguagewriter.Cartesian;
import cesiumlanguagewriter.Cartographic;
import cesiumlanguagewriter.JulianDate;
import com.pelletier.czml.util.JulianDateUtil;
import com.pelletier.data.providers.PositionProvider;
import gov.sandia.phoenix.elements.tle.TLE;
import gov.sandia.phoenix.geometry.Vector3;
import gov.sandia.phoenix.propagators.Propagator;
import gov.sandia.phoenix.propagators.sgp4.SGP4;
import gov.sandia.phoenix.propagators.sgp4.WGS84;
import gov.sandia.phoenix.time.JD;
import gov.sandia.phoenix.time.TimeBuilder;
import scala.Option;
import scala.Some;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PositionsGenerator {

    private PositionProvider positionProvider;

    public PositionsGenerator(){}

    public CartesianTimeList generateTimeList(Date startDate, Date endDate, int timeStep){
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

    public void setPositionProvider(PositionProvider positionProvider) {
        this.positionProvider = positionProvider;
    }
}
