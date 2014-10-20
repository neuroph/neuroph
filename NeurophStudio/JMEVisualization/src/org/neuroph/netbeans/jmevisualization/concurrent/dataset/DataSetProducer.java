/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization.concurrent.dataset;

import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.netbeans.jmevisualization.DataSetVisualizationParameters;
import org.neuroph.netbeans.jmevisualization.IOSettingsDialog;
import org.neuroph.netbeans.jmevisualization.concurrent.Producer;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;

/**
 *
 * @author Milos Randjic
 */
public class DataSetProducer extends Producer {

    public DataSetProducer(NeuralNetAndDataSet neuralNetAndDataSet) {
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
             Create parameters for visualization
             */
            DataSetVisualizationParameters parameters = new DataSetVisualizationParameters();
            parameters.setDataSet(dataSet);
            parameters.setInputs(IOSettingsDialog.getInstance().getStoredInputs());

            ArrayList<ColorRGBA> dominantOutputColors = new ArrayList<>(dataSet.size());

            /*
             Add appropriate output colors for a single training iteration
             */
            for (DataSetRow dataSetRow : dataSet.getRows()) {
                
                /*
                 Add input to the neuralNetwork
                 */
                neuralNetwork.setInput(dataSetRow.getInput());

                /*
                 Calculate network output,                 
                 in order to compare original dataSet row output with calculated one
                 */
                neuralNetwork.calculate();

                /*
                 Add dominant color, index determined by maximum absolute value in output network layer
                 */
                dominantOutputColors.add(getDominantOutputColor(neuralNetwork));

            }

            parameters.setDominantOutputColors(dominantOutputColors);

            /*
             Put defined parameters into sharedQueue
             If sharedQueue is full, than producer has to wait until the first free space in sharedQueue appears
             */
            getSharedQueue().put(parameters);

        } catch (InterruptedException ex) {

        }
    }

    /**
     * Calculate dominant output color from neuralNetwork output layer
     *
     * Dominant output is the one that has the maximum absolute value, among
     * other output values in output layer
     *
     * @param neuralNetwork
     * @return
     */
    private ColorRGBA getDominantOutputColor(NeuralNetwork neuralNetwork) {

        int index = 0;
        double max = Double.MIN_VALUE;
        double[] outputValues = neuralNetwork.getOutput();

        for (int i = 0; i < outputValues.length; i++) {
            if (Math.abs(outputValues[i]) > max) {
                max = outputValues[i];
                index = i;
            }
        }

        return IOSettingsDialog.getInstance().getOutputColors().get(index);

    }

}
