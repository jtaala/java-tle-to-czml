package com.pelletier.czml.writers.label;

import cesiumlanguagewriter.CesiumHorizontalOrigin;

public class DefaultLabelInfoProvider implements LabelInfoProvider {

    private String fontProperty;
    private CesiumHorizontalOrigin horizontalOriginProperty;
    private double outlineWidthProperty;
    private String textProperty;
    private boolean showProperty;
    private double pixelOffsetXProperty;
    private double pixelOffsetYProperty;

    public DefaultLabelInfoProvider(){
        this.fontProperty = "11pt Lucida Console";
        this.horizontalOriginProperty = CesiumHorizontalOrigin.LEFT;
        this.outlineWidthProperty = 2.0;
        this.textProperty = "Satellite";
        this.showProperty = true;
        this.pixelOffsetXProperty = 15;
        this.pixelOffsetYProperty = 0;
    }

    @Override
    public String getFontProperty() {
        return fontProperty;
    }

    public void setFontProperty(String fontProperty) {
        this.fontProperty = fontProperty;
    }

    @Override
	public CesiumHorizontalOrigin getHorizontalOriginProperty() {
        return horizontalOriginProperty;
    }

    public void setHorizontalOriginProperty(CesiumHorizontalOrigin horizontalOriginProperty) {
        this.horizontalOriginProperty = horizontalOriginProperty;
    }

    @Override
    public double getOutlineWidthProperty() {
        return outlineWidthProperty;
    }

    public void setOutlineWidthProperty(double outlineWidthProperty) {
        this.outlineWidthProperty = outlineWidthProperty;
    }

    @Override
    public String getTextProperty() {
        return textProperty;
    }

    public void setTextProperty(String textProperty) {
        this.textProperty = textProperty;
    }

    @Override
    public boolean getShowProperty() {
        return showProperty;
    }

    public void setShowProperty(boolean showProperty) {
        this.showProperty = showProperty;
    }

    @Override
    public double getPixelOffsetXProperty() {
        return pixelOffsetXProperty;
    }

    public void setPixelOffsetXProperty(double pixelOffsetXProperty) {
        this.pixelOffsetXProperty = pixelOffsetXProperty;
    }

    @Override
    public double getPixelOffsetYProperty() {
        return pixelOffsetYProperty;
    }

    public void setPixelOffsetYProperty(double pixelOffsetYProperty) {
        this.pixelOffsetYProperty = pixelOffsetYProperty;
    }
}
