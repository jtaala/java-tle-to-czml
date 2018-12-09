package com.pelletier.czml.writers.packet;

import java.util.Date;

public interface PacketInfoProvider {

    String getWriteId();
    String getWriteVersion();
    String getWriteDescriptionProperty();
    Date getStartAvailability();
    Date getEndAvailability();

}
