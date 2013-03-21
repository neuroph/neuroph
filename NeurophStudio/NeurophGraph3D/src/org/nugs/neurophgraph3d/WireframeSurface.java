package org.nugs.neurophgraph3d;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.nugs.graph3d.Chart3DUtils;
import org.nugs.graph3d.Surface3DFactory;
import org.nugs.graph3d.Surface3DProperties;

public class WireframeSurface {

    double[][] weights;

    public WireframeSurface() {
    }

    public void create(NeuralNetwork nnet) {

        int lcount = 0; //layer count
        int ccount = 0; // connction count


        if (nnet != null) {
            lcount = nnet.getLayersCount();
            ccount = NeurophChartUtilities.getMaxConnectionCount(nnet);//prebaciti ovu metodu u klasu ChartUtilities?
            weights = new double[lcount][ccount];

            for (int i = 0; i < nnet.getLayers().length; i++) {
                for (Neuron neuron : nnet.getLayers()[i].getNeurons()) {
                    for (int j = 0; j < neuron.getInputConnections().length; j++) {
                        weights[i][j] = neuron.getInputConnections()[j].getWeight().value;
                    }
                }
            }

        }
        //Biblioteka u akciji :)
        Surface3DProperties prop=new Surface3DProperties();
        prop.setChartColor(new ColorMapRainbow());
        prop.setChartQuality(Quality.Nicest);
        prop.setChartWireframed(true);
        
        Chart chart = Surface3DFactory.createSurface(new Mapper() {
            @Override
            public double f(double x, double y) {
                return weights[(int) x][(int) y]; //function
            }
        }, new Range(1, lcount - 1), new Range(1, ccount - 1),prop);
        
        Chart3DUtils.openInChartLauncher(chart);


    }
}
