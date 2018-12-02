package com.pelleiter.data.generation;

import cesiumlanguagewriter.Cartesian;
import cesiumlanguagewriter.JulianDate;

import java.util.List;


public class CartesianTimeList {

    private List<Cartesian> positions;
    private List<JulianDate> times;


    public List<Cartesian> getPositions() {
        return positions;
    }

    public void setPositions(List<Cartesian> positions) {
        this.positions = positions;
    }

    public List<JulianDate> getTimes() {
        return times;
    }

    public void setTimes(List<JulianDate> times) {
        this.times = times;
    }
}
