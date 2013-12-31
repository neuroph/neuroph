package org.neuroph.neurosky.demo;

import org.neuroph.bci.mindwave.MindWaveManager;
import org.neuroph.bci.mindwave.MindWaveSample;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;

/**
 * Neural Network based mind wave controller
 * This class contains neural network which classifess input brain wave pattern
 * Output is action as a string, which corresponds to output neuron label
 * 
 * @author Zoran Sevarac
 */
public class MindWaveController {
    /**
     * Neural network based classifier
     */
    NeuralNetwork neuralNetwork;

    /**
     * Creates new MindwaveController with specified neural network
     * @param neuralNetwork 
     */
    public MindWaveController(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    /**
     * Returns neural network used by this controller
     * @return 
     */
    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    /**
     * Sets neural network for this controller
     * @param neuralNetwork 
     */
    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }
        
    // Idea: use sequence as mind wave packets as input
    // Idea: use several neural networks at the time
    
    /**
     * Returns action for the given mind wave sample.
     * Uses neural network to classify input sample pattern, and
     * returns label of the neuron with highest activation.
     * 
     * @param mwSample
     * @return 
     */
    public String getAction(MindWaveSample mwSample) {
        // since we're going to use 
        long[] values = mwSample.getValues(); // normalize values the same way as for training  
        
        // scale values with same scaling factor as training set 
        double[] inputs = new double[values.length];
        double scaleFactors[] = MindWaveManager.getInstance().getScaleFactors();
        
        for(int i=0; i<values.length; i++ ) {                             
            inputs[i] = values[i]/scaleFactors[i];
        }
        
        // we should use max normalization as default dataset normalization used in training while sampling bci data
        
        neuralNetwork.setInput(inputs);
        neuralNetwork.calculate();
        neuralNetwork.getOutput();
        
        // iterate output neurons and get the label from one with the highest output     
        // find neuron with highest output
        Neuron maxNeuron=null;
        double maxOut = Double.NEGATIVE_INFINITY;
        for(Neuron neuron : neuralNetwork.getOutputNeurons()) {
            if (neuron.getOutput() > maxOut) {
                maxNeuron = neuron;
                maxOut = neuron.getOutput();
            }
        }
                           
        // and return its label
        return maxNeuron.getLabel();        
    }
          
}