package org.nugs.graph2d;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

/**
 *
 * @author Vedrana
 */
public class Hist2D {

    public ChartPanel createChartPanel(double[] values, int numberOfBins,Graph2DProperties prop) {
        //Method specific variables
        JFreeChart chart = null;
        ChartPanel panel;
               
        //create the histogram data set
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        
        //add the values to the dateset
        dataset.addSeries("Histogram", values, numberOfBins);
        //create the histogram
        chart = ChartFactory.createHistogram(prop.getChartTitle(), prop.getxAxisLabel(), prop.getyAxisLabel(),
                dataset, prop.getOrientation(), prop.isLegendVisible(), prop.isTooltipsVisible(), prop.isUrlVisible());

        //populate the panel object with histogram
        panel = new ChartPanel(chart);

        //return the panel containing the histogram of weights
        return panel;
    }

    public ChartPanel createChartPanel(double[] values, int numberOfBins) {
        return createChartPanel(values, numberOfBins, new Graph2DProperties());
    }
}
