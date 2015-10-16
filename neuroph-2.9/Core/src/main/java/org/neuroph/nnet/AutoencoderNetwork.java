package org.neuroph.nnet;

/**
 * Auto Encoder Neural Network
 */
public class AutoencoderNetwork extends MultiLayerPerceptron {

    public AutoencoderNetwork(int inputsCount, int hiddenCount) {      
        super(inputsCount, hiddenCount, inputsCount);
    }
    
}
