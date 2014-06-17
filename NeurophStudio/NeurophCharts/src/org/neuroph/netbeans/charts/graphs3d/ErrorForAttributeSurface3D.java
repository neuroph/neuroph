package org.neuroph.netbeans.charts.graphs3d;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.colormaps.ColorMapHotCold;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.nugs.graph3d.api.Point3D;
import org.nugs.graph3d.JzySurface3DFactory;
import org.nugs.graph3d.api.Surface3DFactory;
import org.nugs.graph3d.api.Surface3DProperties;
import org.neuroph.netbeans.charts.providers3d.DatasetErrorDataProvider3D;
import org.nugs.graph3d.api.Range;

/**
 * Surface chart of the network error for chosen attributes of dataset
 *
 * @author Vedrana Gajic
 */
public class ErrorForAttributeSurface3D extends Graph3DBuilder {

    private Point3D[] points3d;
    int pointIdx = 1;
    int outputNeuronCount;

    public ErrorForAttributeSurface3D(NeuralNetwork nnet, DataSet dataset) {

        dataProvider3D = new DatasetErrorDataProvider3D(dataset, nnet);
        outputNeuronCount = nnet.getOutputsCount();
    }

    @Override
    public String toString() {
        return "Network error for chosen attributes of dataset";
    }

    @Override
    public Chart createGraph() {
        points3d = (Point3D[]) dataProvider3D.getData(attribute2);
        Surface3DProperties prop = new Surface3DProperties();
        IColorMap map = new ColorMapHotCold();
        map.setDirection(true);
        prop.setChartColor(map);
        prop.setChartQuality(Quality.Nicest);
        prop.setChartWireframed(true);
        prop.setxRange(new Range(0, 1));

        prop.setyRange(new Range(1, outputNeuronCount));
        prop.setyAxeInteger(true);

        prop.setxAxeLabel("Chosen input"); //Dataset row/chosen attr
        prop.setyAxeLabel("Output neuron"); //output neuron
        prop.setzAxeLabel("Error"); //err/out
        pointIdx = 1;
        
        Surface3DFactory<Chart> surfaceFactory = new JzySurface3DFactory();
        
        Chart chart = surfaceFactory.createSurface(new Mapper() {
            @Override
            public double f(double x, double y) {
                //  funkcija krive - koja vraca vrednost weights 

                for (int i = pointIdx; i < points3d.length; i++) {
                    Point3D point = points3d[i];
                    if ((point.getX() <= x) && (point.getY() == (int) y)) {
                        pointIdx++;
                        return point.getZ();
                    }
                }
                return 0;

            }
        }, prop);

        return chart;
    }
}
