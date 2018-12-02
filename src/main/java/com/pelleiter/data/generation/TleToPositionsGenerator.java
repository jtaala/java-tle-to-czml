package com.pelleiter.data.generation;

import agi.foundation.compatibility.DateTimeHelper;
import cesiumlanguagewriter.Cartesian;
import cesiumlanguagewriter.Cartographic;
import cesiumlanguagewriter.JulianDate;
import com.pelletier.czml.util.JulianDateUtil;
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
import java.util.Date;
import java.util.List;

public class TleToPositionsGenerator {

    private TLE tle;
    private Date startDate;
    private Date endDate;

    public TleToPositionsGenerator(TLE tle){
        this.tle = tle;
    }


    public int getOrbitalTimeMinutes(){
      return (int) Math.round((24.0 * 60)/Double.parseDouble(this.tle.line2().substring(52,63)));
    };


    public CartesianTimeList generateTimeList(Date startDate, Date endDate, double timeStep){

        Option<String> optionalName = this.tle.line0();
        Propagator propagator = new SGP4(this.tle, true);

        CartesianTimeList cartographicTimeList = new CartesianTimeList();

        List<JulianDate> positionTimes = new ArrayList<JulianDate>();
        List<Cartesian> positions = new ArrayList<Cartesian>();

        int i = 0;

        //TODO make the time interval for position generation a parameter
        JD date = TimeBuilder.apply(startDate);

        //ew but OK
        JulianDate julianDate = JulianDateUtil.fromDate(startDate);
        do{
            Vector3 position = propagator.unsafe_state(date).position();
            positions.add(new Cartesian(position.x(), position.y(), position.z()));
            positionTimes.add(julianDate);
            i++;
            date = date.plusSeconds(timeStep);
            julianDate = julianDate.addSeconds(timeStep);
        }while(date.isBefore(TimeBuilder.apply(endDate))); //TODO fix this to be on time interval

        cartographicTimeList.setPositions(positions);
        cartographicTimeList.setTimes(positionTimes);

        return cartographicTimeList;
    }

}
