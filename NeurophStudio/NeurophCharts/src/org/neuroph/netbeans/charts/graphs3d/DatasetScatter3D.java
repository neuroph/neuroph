package org.neuroph.netbeans.charts.graphs3d;

import java.util.Arrays;
import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.colormaps.IColorMap;
import org.neuroph.core.data.DataSet;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.MyColorMap;
import org.nugs.graph3d.api.Point3D;
import org.nugs.graph3d.JzyScatter3DFactory;
import org.nugs.graph3d.api.Scatter3DProperties;
import org.neuroph.netbeans.charts.providers3d.DatasetDataProvider3D;

/**
 * Scatter of the attributes of dataset (3D)
 *
 * @author Vedrana Gajic
 */
public class DatasetScatter3D extends Graph3DBuilder<Chart, Point3D> {

    private DataSet dataset;

    public DatasetScatter3D(DataSet dataset) {
        dataProvider3D = new DatasetDataProvider3D(dataset);
        this.dataset = dataset;
    }

    @Override
    public String toString() {
        return "Scatter";
    }

    @Override
    public Chart createGraph() {


        Point3D[] points3d = (Point3D[]) dataProvider3D.getData(attribute1, attribute2, attribute3);

        Scatter3DProperties properties = new Scatter3DProperties();
        properties.setDotSize(10);
        List<Attribute> attributes = Arrays.asList(attribute1, attribute2, attribute3);

        IColorMap map = new MyColorMap(dataset, attributes);
        map.setDirection(true);
        properties.setChartColor(map);
        properties.setxAxeLabel(attribute1.getLabel());
        properties.setyAxeLabel(attribute2.getLabel());
        properties.setzAxeLabel(attribute3.getLabel());

        //  properties.setRadius((float)1.0/data[0].length);
        JzyScatter3DFactory chartFactory = new JzyScatter3DFactory();
        
        Chart chart = chartFactory.createScatter3D(points3d, properties);


        return chart;
    }
}
