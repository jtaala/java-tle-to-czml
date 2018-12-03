package com.pelletier.data.providers;

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
public class TlePositionProvider implements PositionProvider {

    private final TLE tle;
    private final Propagator propagator;

    public TlePositionProvider(TLE tle){
        this.tle = tle;
        propagator = new SGP4(this.tle, true);
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
