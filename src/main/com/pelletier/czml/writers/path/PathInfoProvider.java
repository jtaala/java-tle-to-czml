package com.pelletier.czml.writers.path;

import java.awt.*;
import java.util.Date;

public interface PathInfoProvider {

    Date getStartDate();
    boolean getShowProperty();
    double getWidthProperty();
    Color getColorProperty();
    double getResolutionProperty();
    int getOrbitalTimeMinutes();
}
