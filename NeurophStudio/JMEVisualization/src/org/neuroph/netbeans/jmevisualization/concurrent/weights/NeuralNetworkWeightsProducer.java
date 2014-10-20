/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization.concurrent.weights;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.netbeans.jmevisualization.concurrent.Producer;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;

/**
 *
 * @author Milos Randjic
 */
public class NeuralNetworkWeightsProducer extends Producer {

    public NeuralNetworkWeightsProducer(NeuralNetAndDataSet neuralNetAndDataSet) {
        super(neuralNetAndDataSet);
    }

    @Override
    public void run() {
        try {
            
            /*
             Fetch neuralNetwork and dataSet 
             */
            DataSet dataSet = getNeuralNetAndDataSet().getDataSet();
            NeuralNetwork neuralNetwork = getNeuralNetAndDataSet().getNetwork();

            /*
             Perform neuralNetwork calculations
             This process refers to single neuralNetwork training iteration
             */
            for (DataSetRow dataSetRow : dataSet.getRows()) {
                neuralNetwork.setInput(dataSetRow.getInput());
                neuralNetwork.calculate();
            }

            /*
             Put neuralNetwork into sharedQueue
             If sharedQueue is full, than producer has to wait until the first free space in sharedQueue appears
             */
            getSharedQueue().put(getNeuralNetAndDataSet().getNetwork());

        } catch (InterruptedException ex) {

        }
    }
}
