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
 * Provides neural network weights data as an array of 3D points
 * Suitable for visualizaing network weights in a 3D
 * 
 * @author Vedrana Gajic
 */
public class WeightsDataProvider3D implements DataProvider3D<Point3D> {

    /**
     * A neural network that is source of weights data
     */
    private NeuralNetwork neuralNetwork;

    public WeightsDataProvider3D(NeuralNetwork neuralNetwork) {
        if (neuralNetwork == null) {
            throw new IllegalArgumentException("Neural network is null!");
        }
        
        this.neuralNetwork = neuralNetwork;
    }

    /**
     * Returns neural network weights data as an array of 3D points
     * For each 3D point coords are:
     *  x - layer index
     *  y - connection index in layer
     *  z - weight value
     * 
     * @param attributes
     * @return 
     */
    @Override
    public Point3D[] getData(Attribute... attributes) {
        // we dont need attributes here, but thats how interface is defined
        
        // get the number of total connections in this network and create an array 
        Point3D[] weights = new Point3D[NeurophChartUtilities.getConnectionCount(neuralNetwork) + 1]; // why +1? probably some fix...
        
        int totalConnectionCounter = 1;
        
        // iterate all layers, neurons and connections and create an array filled with weight values
        for (int layerIdx = 1; layerIdx < neuralNetwork.getLayers().length; layerIdx++) {
            Layer currentLayer = neuralNetwork.getLayerAt(layerIdx);
            int layerConnectionCounter = 1; // connection counter for current layer
            for (Neuron neuron : currentLayer.getNeurons()) {
                Connection[] connections = neuron.getInputConnections();
                for (int j = 0; j < connections.length; j++) {
                    // create and add 3D Point to weight points array easy 3D point. Each point is represented with: layerIdx, connIdx in corresponding layer, and weight value
                    weights[totalConnectionCounter] = new Point3D(layerIdx, layerConnectionCounter, connections[j].getWeight().getValue());
                    layerConnectionCounter++;
                    totalConnectionCounter++;
                }
            }
        }
        return weights;
    }

    
    // this methods add new row in order to fix display - to avoid sudden cuts and drop to zero
    // it repeats weight data from the last layer once again
    public Point3D[] addNewRow(Point3D[] weights) {        
        // firts grow the array
        Point3D[] newWeights = new Point3D[NeurophChartUtilities.getConnectionCount(neuralNetwork) + 1 + NeurophChartUtilities.getLastLayerConnCount(neuralNetwork)];
        System.arraycopy(weights, 0, newWeights, 0, weights.length);
        
        int counter = NeurophChartUtilities.getConnectionCount(neuralNetwork) + 1; // used for indexing additional weights that will be added
        
        for (int i = 1; i < weights.length; i++) { // FIX: do we have to iterate everything from start here? 
            if (weights[i].getX() == neuralNetwork.getLayers().length - 1) { // if we've reached the last layer
                newWeights[counter] = new Point3D(weights[i].getX() + 1, weights[i].getY(), weights[i].getZ()); // copy it again once more
                counter++;
            }
        }
        return newWeights;
    }
}
