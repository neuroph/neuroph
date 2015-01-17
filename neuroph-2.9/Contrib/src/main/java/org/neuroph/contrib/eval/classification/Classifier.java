/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.eval.classification;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.util.plugins.PluginBase;

/**
 * Classifier plugin for neurla networks
 * @author zoran
 */
public class Classifier extends PluginBase {
    
    double threshold = 0.5;
    
    public String classify(double[] pattern) {
        NeuralNetwork nnet = getParentNetwork();
        nnet.setInput(pattern);
        nnet.calculate();
        
        Neuron maxNeuron = null;
        double maxOutput = Double.MIN_VALUE;
        
        for (Neuron neuron : nnet.getOutputNeurons()) {
            if (neuron.getOutput() > maxOutput) {
                maxOutput = neuron.getOutput();
                maxNeuron = neuron;
            }
        }
        
        if (maxOutput > threshold)
                return maxNeuron.getLabel();
        else
            return null;
    }
    
}
