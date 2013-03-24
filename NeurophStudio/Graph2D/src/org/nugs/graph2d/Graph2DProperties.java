package org.nugs.graph2d;

import java.awt.Color;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author hrza
 */
public class Graph2DProperties {

    String chartTitle;
    String xAxisLabel;
    String yAxisLabel;
    boolean tooltips = false;
    boolean legend = false;
    boolean url = false;
    PlotOrientation orientation = PlotOrientation.VERTICAL;
    private Color chartBackgroud = Color.white;
    private Color plotBackgroud = new Color(0xF5F5F5);
    private Color xGridlineBackgroud = Color.lightGray;
    private Color yGridlineBackgroud = Color.lightGray;
    private Color lineColor = Color.orange;

    public Graph2DProperties() {
    }

    
    
    public Graph2DProperties(String chartTitle, String xAxisLabel, String yAxisLabel) {
        this.chartTitle = chartTitle;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public boolean isTooltipsVisible() {
        return tooltips;
    }

    public void setTooltipsVisible(boolean tooltips) {
        this.tooltips = tooltips;
    }

    public boolean isLegendVisible() {
        return legend;
    }

    public void setLegendVisible(boolean legend) {
        this.legend = legend;
    }

    public PlotOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(PlotOrientation orientation) {
        this.orientation = orientation;
    }

    public Color getChartBackgroud() {
        return chartBackgroud;
    }

    public void setChartBackgroud(Color chartBackgroud) {
        this.chartBackgroud = chartBackgroud;
    }

    public Color getPlotBackgroud() {
        return plotBackgroud;
    }

    public void setPlotBackgroud(Color plotBackgroud) {
        this.plotBackgroud = plotBackgroud;
    }

    public Color getXGridlineBackgroud() {
        return xGridlineBackgroud;
    }

    public void setXGridlineBackgroud(Color xGridlineBackgroud) {
        this.xGridlineBackgroud = xGridlineBackgroud;
    }

    public Color getYGridlineBackgroud() {
        return yGridlineBackgroud;
    }

    public void setYGridlineBackgroud(Color yGridlineBackgroud) {
        this.yGridlineBackgroud = yGridlineBackgroud;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public boolean isUrlVisible() {
        return url;
    }

    public void setUrl(boolean url) {
        this.url = url;
    }
}
