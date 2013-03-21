/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nugs.graph3d;

import org.jzy3d.colors.Color;
import org.jzy3d.colors.colormaps.ColorMapHotCold;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 *
 * @author vedrana
 */
public class Chart3DProperties {

    protected Quality chartQuality = Quality.Nicest;
    protected IColorMap chartColor = new ColorMapHotCold();
    protected boolean chartWireframed = false;
    protected Color wireframeColor = Color.BLACK;

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
     * @param chartWireframed - set true if chart should have wireframe and false
     * otherwise
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
}
