package com.pelletier.czml.writers.packet;

import java.util.Date;

public class DocumentPacketInfoProvider implements PacketInfoProvider {

    private String writeId;
    private String writeVersion;
    private String writeDescriptionProperty;
    private Date startAvailability;
    private Date endAvailability;

    public DocumentPacketInfoProvider() {
        this.writeId = "document";
        this.writeVersion = "1.0";
        this.writeDescriptionProperty = "CZML Document";
        this.startAvailability = new Date();
        this.endAvailability = new Date(this.startAvailability.getTime() + (1000 * 60 * 60 * 24));
    }

    @Override
    public String getWriteId() {
        return writeId;
    }

    public void setWriteId(String writeId) {
        this.writeId = writeId;
    }

    @Override
    public String getWriteVersion() {
        return writeVersion;
    }

    public void setWriteVersion(String writeVersion) {
        this.writeVersion = writeVersion;
    }

    @Override
    public String getWriteDescriptionProperty() {
        return writeDescriptionProperty;
    }

    public void setWriteDescriptionProperty(String writeDescriptionProperty) {
        this.writeDescriptionProperty = writeDescriptionProperty;
    }

    @Override
    public Date getStartAvailability() {
        return startAvailability;
    }

    public void setStartAvailability(Date startAvailability) {
        this.startAvailability = startAvailability;
    }

    @Override
    public Date getEndAvailability() {
        return endAvailability;
    }

    public void setEndAvailability(Date endAvailability) {
        this.endAvailability = endAvailability;
    }
}
