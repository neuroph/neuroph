/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nugs.neurophgraph3d;

import org.nugs.graph3d.Chart3DUtils;
import org.jzy3d.chart.Chart;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.nugs.graph3d.Hist3DFactory;

/**
 *
 * @author vedrana
 */
public class Histogram3D {

    public Histogram3D() {
    }

    public void makeHistogram(NeuralNetwork nnet) {
        if (nnet != null) {
            //Popunjavam niz height[][] i saljem openInChartLauncher metodi Hist3DFactory
            double[][] heights = new double[nnet.getLayersCount()][NeurophChartUtilities.getMaxConnectionCount(nnet)]; //getMaxConnCount()

            for (int i = 0; i < nnet.getLayers().length; i++) {

                for (Neuron neuron : nnet.getLayers()[i].getNeurons()) {
                    for (int j = 0; j < neuron.getInputConnections().length; j++) {

                        heights[i][j] = neuron.getInputConnections()[j].getWeight().value;

                    }
                }
            }
            //Biblioteka u akciji!

            Chart chart = Hist3DFactory.createHistogram(heights);
            Chart3DUtils.openInChartLauncher(chart); 
        }
    }
}
