package org.neuroph.netbeans.charts.graphs2d;

import java.awt.Color;
import java.awt.geom.Point2D;
import org.jfree.chart.ChartPanel;
import org.neuroph.core.data.DataSet;
import org.nugs.graph2d.JFreeLineChartFactory;
import org.nugs.graph2d.api.Graph2DProperties;
import org.nugs.graph2d.api.LineChartFactory;
import org.neuroph.netbeans.charts.providers2d.DatasetDataProvider2D;

/**
 *
 * @author Vedrana Gajic
 */
public class DatasetSeries2D extends Graph2DBuilder {

    private Point2D[] data;

    public DatasetSeries2D(DataSet dataset) {
        provider2D = new DatasetDataProvider2D(dataset);

    }

    @Override
    public String toString() {
        return "Series";
    }

    @Override
    public ChartPanel create() {
        data = provider2D.getData(attribute1, attribute2);
        Graph2DProperties prop = new Graph2DProperties("Dataset visualization", attribute1.getLabel(), attribute2.getLabel());
        prop.setxAxisLabel(attribute1.getLabel());
        prop.setyAxisLabel(attribute2.getLabel());
        prop.setLineColor(Color.BLUE);
        
        LineChartFactory<ChartPanel> chart2DFactory = new JFreeLineChartFactory();
        ChartPanel chart = chart2DFactory.createLineChart(data, prop);
        return chart;
    }
}
