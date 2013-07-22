package org.nugs.graph3d.api;

import org.jzy3d.colors.Color;
import org.jzy3d.colors.colormaps.ColorMapHotCold;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 *
 * @author Vedrana Gajic
 */
public class Chart3DProperties {

    protected Quality chartQuality = Quality.Nicest;
    protected IColorMap chartColor = new ColorMapHotCold();
    protected boolean chartWireframed = false;
    protected Color wireframeColor = Color.BLACK;
    protected String xAxeLabel = "X";
    protected String yAxeLabel = "Y";
    protected String zAxeLabel = "Z";
    protected boolean xAxeInteger = false;
    protected boolean yAxeInteger = false;

    /**
     * Setter for chart color
     *
     * @param map color map (Example: new ColorMapHotCold())
     * @return void
     */
    public void setChartColor(IColorMap map) {
        this.chartColor = map;
    }

    /**
     * Returns the chart color
     *
     * @return IColorMap
     */
    public IColorMap getChartColor() {
        return chartColor;
    }

    /**
     * Returns true if chart have wireframe and false otherwise
     *
     * @return boolean
     */
    public boolean isChartWireframed() {
        return chartWireframed;
    }

    /**
     * @param chartWireframed - set true if chart should have wireframe and
     * false otherwise
     * @return void
     */
    public void setChartWireframed(boolean chartWireframed) {
        this.chartWireframed = chartWireframed;
    }

    /**
     * Returns the wireframe color
     *
     * @return Color
     */
    public Color getWireframeColor() {
        return wireframeColor;
    }

    /**
     * Setter for wireframe color
     *
     * @param wireframeColor (Example: Color.BLACK)
     * @return void
     */
    public void setWireframeColor(Color wireframeColor) {
        this.wireframeColor = wireframeColor;
    }

    /**
     * Returns the quality of the chart
     *
     * @return Quality
     */
    public Quality getChartQuality() {
        return chartQuality;
    }

    /**
     * Setter for quality of the chart
     *
     * @param chartQuality (Example Quality.Nicest)
     */
    public void setChartQuality(Quality chartQuality) {
        this.chartQuality = chartQuality;
    }

    /**
     * Getter for the label of x-Axis
     *
     * @return String
     */
    public String getxAxeLabel() {
        return xAxeLabel;
    }

    /**
     * Setter for the label of x-Axis
     *
     * @param xAxeLabel String
     * @return void
     */
    public void setxAxeLabel(String xAxeLabel) {
        this.xAxeLabel = xAxeLabel;
    }

    /**
     * Getter for the label of y-Axis
     *
     * @return String
     */
    public String getyAxeLabel() {
        return yAxeLabel;
    }

    /**
     * Setter for the label of y-Axis
     *
     * @param yAxeLabel String
     * @return void
     */
    public void setyAxeLabel(String yAxeLabel) {
        this.yAxeLabel = yAxeLabel;
    }

    /**
     * Setter for the label of z-Axis
     *
     * @return String
     */
    public String getzAxeLabel() {
        return zAxeLabel;
    }

    /**
     * Setter for the label of z-Axis
     *
     * @param zAxeLabel String
     * @return void
     */
    public void setzAxeLabel(String zAxeLabel) {
        this.zAxeLabel = zAxeLabel;
    }

    /**
     * Returns true if all values on x-Axis are integer, false otherwise
     *
     * @return boolean
     */
    public boolean isxAxeInteger() {
        return xAxeInteger;
    }

    /**
     * Set to true if all values on x-Axis are integer, false otherwise
     *
     * @param xAxeInteger
     * @return void
     */
    public void setxAxeInteger(boolean xAxeInteger) {
        this.xAxeInteger = xAxeInteger;
    }

    /**
     * Returns true if all values on y-Axis are integer, false otherwise
     *
     * @return boolean
     */
    public boolean isyAxeInteger() {
        return yAxeInteger;
    }

    /**
     * Set to true if all values on y-Axis are integer, false otherwise
     *
     * @param yAxeInteger
     * @return void
     */
    public void setyAxeInteger(boolean yAxeInteger) {
        this.yAxeInteger = yAxeInteger;
    }
}
