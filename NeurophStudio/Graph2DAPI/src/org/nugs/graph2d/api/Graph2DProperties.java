package org.nugs.graph2d.api;



import java.awt.Color;
import java.awt.Paint;
import org.jfree.chart.plot.PlotOrientation;

/**
 * This class holds common properties for 2D graphs
 * @author Vedrana Gajic
 */
public class Graph2DProperties {

    String chartTitle = "Chart";
    String xAxisLabel = "X";
    String yAxisLabel = "Y";
    boolean tooltips = false;
    boolean legend = false;
    boolean url = false;
    PlotOrientation orientation = PlotOrientation.VERTICAL;
    private Color chartBackground = Color.white;
    private Color plotBackgroud = new Color(0xF5F5F5);
    private Color xGridlineBackgroud = Color.lightGray;
    private Color yGridlineBackgroud = Color.lightGray;
    private Color lineColor = Color.orange;
    private Paint[] seriesColors = null;

    public Graph2DProperties() {
    }

    public Graph2DProperties(String chartTitle, String xAxisLabel, String yAxisLabel) {
        this.chartTitle = chartTitle;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
    }

    /**
     * Getter for chart title
     *
     * @return String
     */
    public String getChartTitle() {
        return chartTitle;
    }

    /**
     * Setter for chart title
     *
     * @param chartTitle title of the chart (Example: "My Chart")
     * @return void
     */
    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    /**
     * Setter for a label of x-Axis
     *
     * @return String
     */
    public String getxAxisLabel() {
        return xAxisLabel;
    }

    /**
     * Setter for a label of x-Axis
     *
     * @param xAxisLabel label of the x-axis (Example: "X")
     * @return void
     */
    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    /**
     * Getter for a label of y-Axis
     *
     * @return String
     */
    public String getyAxisLabel() {
        return yAxisLabel;
    }

    /**
     * Setter for a label of y-Axis
     *
     * @param yAxisLabel label of the y-axis (Example: "Y")
     * @return void
     */
    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    /**
     * Get the visibility of tooltips
     *
     * @return boolean
     */
    public boolean isTooltipsVisible() {
        return tooltips;
    }

    /**
     * Set visibility of tooltips
     *
     * @param tooltips boolean
     * @return void
     */
    public void setTooltipsVisible(boolean tooltips) {
        this.tooltips = tooltips;
    }

    /**
     * Get the visibility of legend
     *
     * @return boolean
     */
    public boolean isLegendVisible() {
        return legend;
    }

    /**
     * Set visibility of legend
     *
     * @param legend boolean
     * @return void
     */
    public void setLegendVisible(boolean legend) {
        this.legend = legend;
    }

    /**
     * Get orientation of the plot
     *
     * @return PlotOrientation
     */
    public PlotOrientation getOrientation() {
        return orientation;
    }

    /**
     * Set orientation of the plot
     *
     * @param orientation PlotOrientation
     * @return void
     */
    public void setOrientation(PlotOrientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Get the color of chart background
     *
     * @return Color
     */
    public Color getChartBackground() {
        return chartBackground;
    }

    /**
     * Set the color of chart background
     *
     * @param chartBackgroud Color
     * @return void
     */
    public void setChartBackground(Color chartBackgroud) {
        this.chartBackground = chartBackgroud;
    }

    /**
     * Get the color of plot background
     *
     * @return Color
     */
    public Color getPlotBackgroud() {
        return plotBackgroud;
    }

    /**
     * Set the color of plot background
     *
     * @param plotBackgroud Color
     * @return void
     */
    public void setPlotBackgroud(Color plotBackgroud) {
        this.plotBackgroud = plotBackgroud;
    }

    /**
     * Get the color of the grid on x-axis
     *
     * @return Color
     */
    public Color getXGridlineBackgroud() {
        return xGridlineBackgroud;
    }

    /**
     * Set the color of the grid on x-axis
     *
     * @param xGridlineBackgroud Color
     * @return void
     */
    public void setXGridlineBackgroud(Color xGridlineBackgroud) {
        this.xGridlineBackgroud = xGridlineBackgroud;
    }

    /**
     * Get the color of the grid on y-axis
     *
     * @return Color
     */
    public Color getYGridlineBackgroud() {
        return yGridlineBackgroud;
    }

    /**
     * Set the color of the grid on y-axis
     *
     * @param yGridlineBackgroud Color
     * @return void
     */
    public void setYGridlineBackgroud(Color yGridlineBackgroud) {
        this.yGridlineBackgroud = yGridlineBackgroud;
    }

    /**
     * Get the color of the line
     *
     * @return Color
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Set the color of the line
     *
     * @param lineColor Color
     * @return void
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * Get the visibility of urls
     *
     * @return boolean
     */
    public boolean isUrlVisible() {
        return url;
    }

    /**
     * Set the url visible
     *
     * @param url boolean
     * @return void
     */
    public void setUrl(boolean url) {
        this.url = url;
    }

    /**
     * Get the color of the serie
     *
     * @param index index of the serie
     * @return Paint
     */
    public Paint getSeriaColor(int index) {
        return seriesColors[index];

    }

    /**
     * Set the color for each serie on the chart
     *
     * @param seriesColors Paint[] (Example: {Color.blue,Color.red} for two
     * series)
     * @return void
     */
    public void setSeriesColors(Paint[] seriesColors) {
        this.seriesColors = seriesColors;
    }
}
