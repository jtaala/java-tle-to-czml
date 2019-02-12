package com.pelletier.czml.writers.position;

import gov.sandia.phoenix.elements.tle.TLE;
import gov.sandia.phoenix.propagators.Propagator;
import gov.sandia.phoenix.propagators.sgp4.SGP4;
import gov.sandia.phoenix.time.TimeBuilder;

import java.util.Date;


/**
 * Uses a TLE to provide object positions from a given time
 *
 * This is not threadsafe.
 */
public class TlePositionProvider implements PositionInfoProvider {

    private TLE tle;
    private Propagator propagator;
    private Date startDate;
    private Date endDate;
    private int timeStep;

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }

    public void setTle(TLE tle) {
        this.tle = tle;
        propagator = new SGP4(this.tle, true);
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public int getTimeStep() {
        return timeStep;
    }

    @Override
    public double getX(Date date) {
        return propagator.unsafe_state(TimeBuilder.apply(date)).position().x();
    }

    @Override
    public double getY(Date date) {
        return propagator.unsafe_state(TimeBuilder.apply(date)).position().y();
    }

    @Override
    public double getZ(Date date) {
        return propagator.unsafe_state(TimeBuilder.apply(date)).position().z();
    }

}
