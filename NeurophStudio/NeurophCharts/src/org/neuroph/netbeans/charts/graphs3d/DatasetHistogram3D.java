package org.neuroph.netbeans.charts.graphs3d;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.colors.colormaps.IColorMap;
import org.neuroph.core.data.DataSet;
import org.nugs.graph3d.JzyHistogram3DFactory;
import org.nugs.graph3d.api.Histogram3DProperties;
import org.nugs.graph3d.api.Histogram3DFactory;
import org.nugs.graph3d.api.Point3D;
import org.neuroph.netbeans.charts.providers3d.DatasetDataProvider3D;

/**
 * 3D Histogram of a selected attributes of the dataset
 *
 * @author Vedrana Gajic
 */
public class DatasetHistogram3D extends Graph3DBuilder {

    private Point3D[] points3d;
    private DataSet dataset;

    public DatasetHistogram3D(DataSet dataset) {
        this.dataset = dataset;
        dataProvider3D = new DatasetDataProvider3D(dataset);
    }

    @Override
    public String toString() {
        return "Histogram";
    }

    @Override
    public Chart createGraph() {
        points3d = (Point3D[]) dataProvider3D.getData(attribute1, attribute2, attribute3);
        Histogram3DProperties prop = new Histogram3DProperties();
        IColorMap map = new ColorMapRainbow();
        map.setDirection(true);
        prop.setChartColor(map);
        prop.setRadius(0.003f);
        prop.setxAxeLabel(attribute1.getLabel());
        prop.setyAxeLabel(attribute2.getLabel());
        prop.setzAxeLabel(attribute3.getLabel());
        prop.setChartWireframed(false);

        Histogram3DFactory<Chart,Point3D> histogramFactory = new JzyHistogram3DFactory();
        Chart chart = histogramFactory.createHistogram3D(points3d, prop);

        return chart;
    }
}
