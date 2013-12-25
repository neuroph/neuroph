package org.nugs.graph2d;

import java.awt.BasicStroke;
import java.awt.geom.Point2D;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.nugs.graph2d.api.Graph2DProperties;
import org.nugs.graph2d.api.LineChartFactory;

/**
 * 2D Line Chart
 * @author Vedrana Gajic
 */
public class JFreeLineChartFactory implements LineChartFactory <ChartPanel>{

    /**
     * Create a 2D Line chart
     *
     * TODO:consider returning JPanel
     * 
     * @param points an array of 2D points on the plot
     * @param prop graph properties
     * @return 2D Chart Panel
     */
    @Override
    public ChartPanel createLineChart(Point2D[] points, Graph2DProperties prop) {
        // Add points from arrays to dataset of linegraph...
        XYSeries series1 = new XYSeries("[" + prop.getxAxisLabel() + "," + prop.getyAxisLabel() + "]");
        for (int i = 0; i < points.length; i++) {
            if (points[i] != null) {
                series1.add(points[i].getX(), points[i].getY());
            }
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        // Create the linechart...
        JFreeChart chart = ChartFactory.createXYLineChart(
                prop.getChartTitle(), // chart title
                prop.getxAxisLabel(), // x axis label (domain axis)
                prop.getyAxisLabel(), // y axis label (range axis)
                dataset, // data
                prop.getOrientation(),
                prop.isLegendVisible(), // include legend
                prop.isTooltipsVisible(), // tooltips
                false // urls
                );

        chart.setBackgroundPaint(prop.getChartBackground());
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(prop.getPlotBackgroud());  // Lighter gray background   
        plot.setDomainGridlinePaint(prop.getXGridlineBackgroud());
        plot.setRangeGridlinePaint(prop.getYGridlineBackgroud());
        plot.getRenderer().setSeriesPaint(0, prop.getLineColor()); // Orange line

        // Additional settings... 
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(2f)); // Bold line

        ChartPanel panel = new ChartPanel(chart);
        return panel;
    }

    /**
     * Create a 2D Line chart with default properties
     *
     * @param points points on the plot
     * @return ChartPanel
     */
    @Override
    public ChartPanel createLineChart(Point2D[] points) {
        return createLineChart(points, new Graph2DProperties());
    }

}
