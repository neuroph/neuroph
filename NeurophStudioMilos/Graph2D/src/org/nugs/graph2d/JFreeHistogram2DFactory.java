package org.nugs.graph2d;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.nugs.graph2d.api.Graph2DProperties;
import org.nugs.graph2d.api.Histogram2DFactory;

/**
 * 2D Histogram Chart
 * @author Vedrana Gajic
 */
public class JFreeHistogram2DFactory implements Histogram2DFactory<ChartPanel> {

     /**
     * Create a 2D Histogram chart (type: frequency)
     *
     * @param values  points on the plot
     * @param numberOfBins  number of bins
     * @param prop properties
     * @return ChartPanel
     */
    @Override
    public ChartPanel createHistogram2D(double[] values, int numberOfBins,Graph2DProperties prop) {
               
        //create the histogram data set
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        
        //add the values to the dateset
        dataset.addSeries("Histogram", values, numberOfBins);
        //create the histogram
        JFreeChart chart = ChartFactory.createHistogram(prop.getChartTitle(), prop.getxAxisLabel(), prop.getyAxisLabel(),
                dataset, prop.getOrientation(), prop.isLegendVisible(), prop.isTooltipsVisible(), prop.isUrlVisible());

        //populate the panel object with histogram
        ChartPanel panel = new ChartPanel(chart);

        //return the panel containing the histogram of weights
        return panel;
    }

    /**
     * Create a 2D Histogram chart (type: frequency) with default properties
     *
     * @param values  points on the plot
     * @param numberOfBins  number of bins
     * @return ChartPanel
     */
    @Override
    public ChartPanel createHistogram2D(double[] values, int numberOfBins) {
        return createHistogram2D(values, numberOfBins, new Graph2DProperties());
    }
}
