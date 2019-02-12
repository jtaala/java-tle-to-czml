package com.pelletier.czml.util;

import cesiumlanguagewriter.JulianDate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class JulianDateUtil {

    public static JulianDate fromDate(Date date){
        return new JulianDate(ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }
}
