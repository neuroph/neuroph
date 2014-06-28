package org.neuroph.netbeans.charts.providers3d;

import org.nugs.graph3d.api.DataProvider3D;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.nugs.graph2d.api.Attribute;
import org.nugs.graph3d.api.Point3D;
import org.neuroph.netbeans.charts.util.NeurophChartUtilities;

/**
 *
 * @author Vedrana Gajic
 */
public class WeightsDataProvider3D implements DataProvider3D<Point3D> {

    NeuralNetwork nnet;

//    public WeightsDataProvider3D() {
//    }

    public WeightsDataProvider3D(NeuralNetwork nnet) {
        this.nnet = nnet;
    }

    @Override
    public Point3D[] getData(Attribute... a) {
        //Ne treba mu atribut
        if (nnet == null) {
            throw new RuntimeException("No neural network.");
        }
        
        Point3D[] weights = new Point3D[NeurophChartUtilities.getConnectionCount(nnet) + 1];
        int totalConnectionCount = 1;
        for (int i = 1; i < nnet.getLayers().length; i++) {
            Layer l = nnet.getLayerAt(i);
            int layerConnectionCount = 1;
            for (Neuron neuron : l.getNeurons()) {
                Connection[] connections = neuron.getInputConnections();
                for (int j = 0; j < connections.length; j++) {
                    weights[totalConnectionCount] = new Point3D(i, layerConnectionCount, connections[j].getWeight().getValue());
                    layerConnectionCount++;
                    totalConnectionCount++;
                }
            }
        }
        return weights;
    }

    public Point3D[] addNewRow(Point3D[] weights) {
        Point3D[] w = new Point3D[NeurophChartUtilities.getConnectionCount(nnet) + 1 + NeurophChartUtilities.getLastLayerConnCount(nnet)];
        System.arraycopy(weights, 0, w, 0, weights.length);
        int counter = NeurophChartUtilities.getConnectionCount(nnet) + 1;
        for (int i = 1; i < weights.length; i++) {
            if (weights[i].getX() == nnet.getLayers().length - 1) {
                w[counter] = new Point3D(weights[i].getX() + 1, weights[i].getY(), weights[i].getZ());
                counter++;
            }

        }
        return w;

    }
}
