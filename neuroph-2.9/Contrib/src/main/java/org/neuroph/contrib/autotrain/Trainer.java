package org.neuroph.contrib.autotrain;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author Milan Brkic - milan.brkic1@yahoo.com
 */
public interface Trainer {  

    /**
     * Auto trainer trains neural network based on neural network and data set.
     * 
     * @param neuralNet
     * @param dataSet
     */
    public void train(NeuralNetwork neuralNet, DataSet dataSet); 
    
    
    /**
     * Traines nn with speciifed data set.
     * Implementations should generate neural networks automatcaly(internaly)
     * @param dataSet 
     */
    public void train(DataSet dataSet); 
}
