/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.jmevisualization.charts.providers;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.charts.util.NeurophChartUtilities;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.Point3D;
import org.nugs.graph3d.api.DataProvider3D;

/**
 *
 * @author Milos Randjic
 */
public class WeightsDataProvider3D implements DataProvider3D<Point3D.Float>{
    
    NeuralNetwork nnet;

    public WeightsDataProvider3D(NeuralNetwork nnet) {
        this.nnet = nnet;
    }

    @Override
    public Point3D.Float[] getData(Attribute... a) {
        
        if (nnet == null) {
            throw new RuntimeException("No neural network.");
        }
        
        Point3D.Float[] weights = new Point3D.Float[NeurophChartUtilities.getConnectionCount(nnet) + 1];
        int totalConnectionCount = 1;
        for (int i = 1; i < nnet.getLayers().length; i++) {
            Layer l = nnet.getLayerAt(i);
            int layerConnectionCount = 1;
            for (Neuron neuron : l.getNeurons()) {
                Connection[] connections = neuron.getInputConnections();
                for (Connection connection : connections) {
                    weights[totalConnectionCount] = new Point3D.Float(i, layerConnectionCount, (float) connection.getWeight().getValue());
                    layerConnectionCount++;
                    totalConnectionCount++;
                }
            }
        }
        return weights;
    }
    
}
