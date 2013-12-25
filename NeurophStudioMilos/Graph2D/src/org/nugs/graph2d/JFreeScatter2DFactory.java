package org.nugs.graph2d;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.nugs.graph2d.api.Graph2DProperties;
import org.nugs.graph2d.api.Scatter2DFactory;

/**
 * 2D Scatter Chart
 * @author Vedrana Gajic
 */
public class JFreeScatter2DFactory implements Scatter2DFactory<ChartPanel>{

    /**
     * Create a 2D Scatter chart
     *
     * @param series  an array of all series on the chart
     * @param prop properties
     * @return ChartPanel
     */
    @Override
    public ChartPanel createScatter2D(XYSeries[] series, Graph2DProperties prop) {

        ChartPanel panel;
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i < series.length; i++) {
            if (series[i].getKey() == null && series[i].getDescription() != null) {
                series[i].setKey(series[i].getDescription());
                dataset.addSeries(series[i]);
            } else {
                dataset.addSeries(series[i]);
            }
        }

        // Create the scatter...
        JFreeChart chart = ChartFactory.createScatterPlot(
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

        for (int i = 0; i < series.length; i++) {
            if (prop.getSeriaColor(i) != null) {
                plot.getRenderer().setSeriesPaint(i, prop.getSeriaColor(i)); // Orange line
            }
        }

        panel = new ChartPanel(chart);
        return panel;
    }

    /**
     * Create a 2D Scatter chart with default properties
     *
     * TODO: why do we take series here, why not points?
     * 
     * @param series  an array of all series on the chart
     * @return ChartPanel
     */
    @Override
    public ChartPanel createScatter2D(XYSeries[] series) {
        return createScatter2D(series, new Graph2DProperties());
    }
}
