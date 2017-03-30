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

/**
 *
 * @author Mladen
 */
public class BackpropagationTraining extends Training{

    @Override
    public void testNeuralNet() {
        //create learning rule and set given learning parameters
        BackPropagation bp = new BackPropagation();
        setParameters(bp);
        this.getNeuralNet().setLearningRule(bp);
        LearningRule learningRule = this.getNeuralNet().getLearningRule();
      //  learningRule.addListener(this);
        //learn and and add results to statistics
        this.getNeuralNet().learn(this.getDataset());
        
        this.getStats().addData(new TrainingResult(bp.getCurrentIteration(), bp.getTotalNetworkError(), createMatrix()));
        
        //calculate mean and std for error and iterations
        this.getStats().calculateParameters();
        
        
        //System.out.println(getStats());
        //System.out.println(getStats().getTrainingResults().get(0).getConfusionMatrix().toString());
    }


    public BackpropagationTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    @Override
    public void setParameters(BackPropagation bp){
        bp.setLearningRate(getSettings().getLearningRate());
        bp.setMaxError(getSettings().getMaxError());
        bp.setBatchMode(getSettings().isBatchMode()); 
        bp.setMaxIterations(getSettings().getMaxIterations());
    }
}
