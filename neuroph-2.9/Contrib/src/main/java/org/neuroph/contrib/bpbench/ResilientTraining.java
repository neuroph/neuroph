/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.ResilientPropagation;

/**
 *
 * @author Mladen
 */
public class ResilientTraining extends Training{

    @Override
    public void testNeuralNet() {
        ResilientPropagation rp = new ResilientPropagation();
        setParameters(rp);
        getNeuralNet().setLearningRule(rp);
        LearningRule learningRule = getNeuralNet().getLearningRule();
        learningRule.addListener(this);
        getNeuralNet().learn(getDataset());
        //testNeuralNetwork(getNeuralNet(), getDataset());
       
    }

  

    
    public ResilientTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    @Override
    public void setParameters(BackPropagation bp) {
        ResilientPropagation rp = (ResilientPropagation) bp;
        rp.setLearningRate(getSettings().getLearningRate());
    }
    
}
