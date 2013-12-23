package org.neuroph.netbeans.charts.util;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.nugs.graph2d.api.Attribute;

/**
 *
 * @author Vedrana Gajic
 */
public class NeurophChartUtilities {

    /**
     * Return the max value of total layer connections
     *
     * @param nnet - Neural network
     * @return integer
     */
    public static int getMaxConnectionCount(NeuralNetwork nnet) {
        int max = 0;
        for (int i = 1; i < nnet.getLayers().length; i++) {
            Layer layer = nnet.getLayerAt(i);
            int totalLayerConnections = 0;
            for (Neuron neuron : layer.getNeurons()) {
                totalLayerConnections = totalLayerConnections + neuron.getInputConnections().length;
                if (totalLayerConnections > max) {
                    max = totalLayerConnections;
                }
            }
        }
        return max;
    }

    /**
     * Return the sum of input connections for all layers
     *
     * @param nnet - Neural network
     * @return integer
     */
    public static int getConnectionCount(NeuralNetwork nnet) {
        int totalLayerConnections = 0;
        for (int i = 1; i < nnet.getLayers().length; i++) {
            Layer layer = nnet.getLayerAt(i);
            for (Neuron neuron : layer.getNeurons()) {
                totalLayerConnections = totalLayerConnections + neuron.getInputConnections().length;
            }
        }
        return totalLayerConnections;
    }

    /**
     * Return the max value of the attribute in dataset
     *
     * @param dataset
     * @param attr - Dataset attribute (input or output)
     * @return double
     */
    public static double getMaxValue(DataSet dataset, Attribute attr) {

        int index = attr.getIndex();
        double max;

        if (attr.isOutput()) {
            max = dataset.getRowAt(0).getDesiredOutput()[index];
        } else {
            max = dataset.getRowAt(0).getInput()[index];
        }

        for (DataSetRow row : dataset.getRows()) {
            if (!attr.isOutput()) {
                if (row.getInput()[index] > max) {
                    max = row.getInput()[index];
                }
            } else {
                if (row.getDesiredOutput()[index] > max) {
                    max = row.getDesiredOutput()[index];
                }
            }

        }
        return max;

    }

    /**
     * Return the min value of the attribute in dataset
     *
     * @param dataset
     * @param attr - Dataset attribute (input or output)
     * @return double
     */
    public static double getMinValue(DataSet dataset, Attribute attr) {
        int index = attr.getIndex();
        double min;

        if (attr.isOutput()) {
            min = dataset.getRowAt(0).getDesiredOutput()[index];
        } else {
            min = dataset.getRowAt(0).getInput()[index];
        }

        for (DataSetRow row : dataset.getRows()) {
            if (!attr.isOutput()) {
                if (row.getInput()[index] < min) {
                    min = row.getInput()[index];
                }
            } else {
                if (row.getDesiredOutput()[index] < min) {
                    min = row.getDesiredOutput()[index];

                }

            }

        }

        return min;
    }

    /**
     * Return the connection count of the last layer
     *
     * @param nnet
     * @return integer
     */
    public static int getLastLayerConnCount(NeuralNetwork nnet) {


        int totalLayerConnections = 0;
        int last = nnet.getLayers().length - 1;
        Layer layer = nnet.getLayerAt(last);
        for (Neuron neuron : layer.getNeurons()) {
            totalLayerConnections = totalLayerConnections + neuron.getInputConnections().length;
        }

        return totalLayerConnections;
    }
}
