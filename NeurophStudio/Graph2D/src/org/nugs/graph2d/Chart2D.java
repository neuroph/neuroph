package org.nugs.graph2d;

import java.awt.BasicStroke;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Vedrana
 */
public class Chart2D {

    public ChartPanel create2DChart(double[] x, double[] y, Graph2DProperties prop) {

        ChartPanel panel;
        // Add points from arrays to dataset of linegraph...
        XYSeries series1 = new XYSeries("[" + prop.getxAxisLabel() + "," + prop.getyAxisLabel() + "]");
        for (int i = 0; i < y.length; i++) {
            for (int j = i; j < x.length; j++) {
                series1.add(x[j], y[i]);
                break;
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

        chart.setBackgroundPaint(prop.getChartBackgroud());
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(prop.getPlotBackgroud());  // Lighter gray background   
        plot.setDomainGridlinePaint(prop.getXGridlineBackgroud());
        plot.setRangeGridlinePaint(prop.getYGridlineBackgroud());
        plot.getRenderer().setSeriesPaint(0, prop.getLineColor()); // Orange line

        // Additional settings... 
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(2f)); // Bold line

        panel = new ChartPanel(chart);
        return panel;
    }

    public ChartPanel create2DChart(double[] x, double[] y) {
        return create2DChart(x, y, new Graph2DProperties());
    }
    //int int

    public ChartPanel create2dChart(int[] x, int[] y, Graph2DProperties prop) {
        ChartPanel panel;
        double[] x1 = new double[x.length];
        double[] y1 = new double[y.length];
        for (int i = 0; i < x.length; i++) {
            x1[i] = x[i];
        }
        for (int j = 0; j < y.length; j++) {
            y1[j] = y[j];
        }

        panel = create2DChart(x1, y1, prop);
        
        XYPlot plot = panel.getChart().getXYPlot();
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());        
        
        return panel;
    }

    public ChartPanel create2DChart(int[] x, int[] y) {
        return create2dChart(x, y, new Graph2DProperties());
    }

    //int double
    public ChartPanel create2dChart(int[] x, double[] y, Graph2DProperties prop) {
        double[] x1 = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            x1[i] = x[i];
        }
        ChartPanel panel = create2DChart(x1, y, prop);

        XYPlot plot = panel.getChart().getXYPlot();
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        return panel;
    }

    public ChartPanel create2DChart(int[] x, double[] y) {
        return create2dChart(x, y, new Graph2DProperties());
    }

    //double int
    public ChartPanel create2dChart(double[] x, int[] y, Graph2DProperties prop) {
        double[] y1 = new double[y.length];
        for (int i = 0; i < y.length; i++) {
            y1[i] = y[i];
        }
        ChartPanel panel = create2DChart(x, y1, prop);
        XYPlot plot = panel.getChart().getXYPlot();
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        return panel;
    }

    public ChartPanel create2DChart(double[] x, int[] y) {
        return create2dChart(x, y, new Graph2DProperties());
    }

//    public void setXAxisInteger(XYPlot plot) {
//        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
//        domain.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//    }

//    public void setYAxisInteger(XYPlot plot) {
//        NumberAxis range = (NumberAxis) plot.getRangeAxis();
//        range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//    }
//
//    public void setXYAxisInteger(XYPlot plot) {
//        setXAxisInteger(plot);
//        setYAxisInteger(plot);
//    }
}
