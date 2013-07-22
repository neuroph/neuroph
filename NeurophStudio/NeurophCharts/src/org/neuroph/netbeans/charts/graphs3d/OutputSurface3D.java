package org.neuroph.netbeans.charts.graphs3d;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.colormaps.ColorMapHotCold;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.nugs.graph3d.api.Point3D;
import org.nugs.graph3d.JzySurface3DFactory;
import org.nugs.graph3d.api.Surface3DFactory;
import org.nugs.graph3d.api.Surface3DProperties;
import org.neuroph.netbeans.charts.providers3d.OutputDataProvider3D;
import org.nugs.graph3d.api.Range;

/**
 * Surface chart of the network output for all rows of the dataset
 *
 * @author Vedrana Gajic
 */
public class OutputSurface3D extends Graph3DBuilder {

    Point3D[] points3d;
    int dataSetRowCount;
    NeuralNetwork neuralNet;

    public OutputSurface3D(NeuralNetwork nnet, DataSet dataset) {
        this.neuralNet = nnet;
        dataSetRowCount = dataset.getRows().size();
        dataProvider3D = new OutputDataProvider3D(dataset, nnet);
    }

    @Override
    public Chart createGraph() {
        points3d = dataProvider3D.getData(attribute1);
        Surface3DProperties prop = new Surface3DProperties();
        IColorMap map = new ColorMapHotCold();
        map.setDirection(true);
        prop.setChartColor(map);
        prop.setChartQuality(Quality.Nicest);
        prop.setChartWireframed(true);
        int outputNeuronCount = neuralNet.getLayerAt(attribute1.getIndex()).getNeuronsCount();
        prop.setyRange(new Range(1, outputNeuronCount));
        prop.setyAxeInteger(true);

        prop.setxRange(new Range(1, dataSetRowCount));
        prop.setxAxeInteger(true);

        prop.setxAxeLabel("Dataset row"); //Dataset row/chosen attr
        prop.setyAxeLabel("Output neuron"); //output neuron
        prop.setzAxeLabel("Output"); //err/out

        Surface3DFactory<Chart> surfaceFactory = new JzySurface3DFactory();
        Chart chart = surfaceFactory.createSurface(new Mapper() {
            @Override
            public double f(double x, double y) {
                for (int i = 1; i < points3d.length; i++) {
                    Point3D point = points3d[i];
                    if ((point.getX() == (int) x) && (point.getY() == (int) y)) {
                        return point.getZ();
                    }

                }
                return 0;
            }
        }, prop);

        return chart;

    }

    @Override
    public String toString() {
        return "Network outputs for entire dataset";
    }
}
