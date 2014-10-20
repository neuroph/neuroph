/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization.concurrent.weights;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.jmevisualization.JMEVisualization;
import org.neuroph.netbeans.jmevisualization.charts.graphs.JMEWeightsHistogram3D;
import org.neuroph.netbeans.jmevisualization.concurrent.Consumer;

/**
 *
 * @author Milos Randjic
 */
public class NeuralNetworkWeightsConsumer extends Consumer {

    public NeuralNetworkWeightsConsumer(JMEVisualization jmeVisualization) {
        super(jmeVisualization);
    }

    @Override
    public void run() {

        JMEWeightsHistogram3D jmeHistogram3D = new JMEWeightsHistogram3D(getJmeVisualization());

        while (true) {
            try {
                /*
                 Fetch neuralNetwork from sharedQueue
                 If sharedQueue is empty, then consumer has to wait until the first object in sharedQueue appears
                 */
                NeuralNetwork neuralNetwork = (NeuralNetwork) getSharedQueue().take();

                /*
                 Draw a histogram graph, for given neuralNetwork
                 */
                jmeHistogram3D.setNeuralNetwork(neuralNetwork);
                jmeHistogram3D.createGraph();

            } catch (InterruptedException ex) {
            }
            
        }
        
    }
    
}
