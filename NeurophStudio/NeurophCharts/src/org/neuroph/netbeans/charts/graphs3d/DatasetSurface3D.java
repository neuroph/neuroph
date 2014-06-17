package org.neuroph.netbeans.charts.graphs3d;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.colormaps.ColorMapHotCold;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.neuroph.core.data.DataSet;
import org.nugs.graph3d.api.Point3D;
import org.nugs.graph3d.JzySurface3DFactory;
import org.nugs.graph3d.api.Surface3DProperties;
import org.neuroph.netbeans.charts.providers3d.DatasetDataProvider3D;
import org.neuroph.netbeans.charts.util.NeurophChartUtilities;
import org.nugs.graph3d.api.Range;

/**
 * Surface of the attributes of dataset (3D)
 *
 * @author Vedrana Gajic
 */
public class DatasetSurface3D extends Graph3DBuilder {

    private Point3D[] points3d;
    private int pointIdx = 0;
    DataSet dataset;

    public DatasetSurface3D(DataSet dataset) {
        dataProvider3D = new DatasetDataProvider3D(dataset);
        this.dataset = dataset;
    }

    @Override
    public String toString() {
        return "Surface";
    }

    @Override
    public Chart createGraph() {

        points3d = (Point3D[]) dataProvider3D.getData(attribute1, attribute2, attribute3);

        double xMin = NeurophChartUtilities.getMinValue(dataset, attribute1);
        double yMin = NeurophChartUtilities.getMinValue(dataset, attribute2);
        double xMax = NeurophChartUtilities.getMaxValue(dataset, attribute1);
        double yMax = NeurophChartUtilities.getMaxValue(dataset, attribute2);
        Surface3DProperties prop = new Surface3DProperties();
        prop.setxAxeLabel(attribute1.getLabel());
        prop.setyAxeLabel(attribute2.getLabel());
        prop.setzAxeLabel(attribute3.getLabel());
        IColorMap map = new ColorMapHotCold();
        map.setDirection(true);
        prop.setChartColor(map);
        prop.setxSteps(50);
        prop.setySteps(50);
        prop.setChartQuality(Quality.Nicest);
        prop.setChartWireframed(true);
        prop.setxRange(new Range(xMin, xMax));
        prop.setyRange(new Range(yMin, yMax));

        pointIdx = 0;
        
        JzySurface3DFactory surfaceFactory = new JzySurface3DFactory();
        Chart chart = surfaceFactory.createSurface(points3d, prop);

        return chart;
    }
}
