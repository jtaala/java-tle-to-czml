package com.pelletier.czml.writers.position;

import java.util.Date;

/**
 * Interface to be implemented which provides ECI coordinates at a given time.
 */
public interface PositionProvider {

    double getX(Date date);
    double getY(Date date);
    double getZ(Date date);

}
