/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nugs.neurophgraph3d;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;

/**
 *
 * @author vedrana
 */
public class NeurophChartUtilities {

    public static int getMaxConnectionCount(NeuralNetwork nnet) {
        //Pitati Zokija jel moglo prostije!
        int max = 0;
        for (int i = 0; i < nnet.getLayers().length; i++) {
            for (Neuron neuron : nnet.getLayers()[i].getNeurons()) {
                for (int j = 0; j < neuron.getInputConnections().length; j++) {
                    if (neuron.getInputConnections().length > max) {
                        max = neuron.getInputConnections().length;
                    }
                }
            }
        }
        return max;
    }
}
