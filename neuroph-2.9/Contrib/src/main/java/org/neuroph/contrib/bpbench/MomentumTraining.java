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
import org.neuroph.nnet.learning.MomentumBackpropagation;

/**
 *
 * @author Mladen
 */
public class MomentumTraining extends Training {

    @Override
    public void testNeuralNet() {
        MomentumBackpropagation mbp = new MomentumBackpropagation();
        setParameters(mbp);
        getNeuralNet().setLearningRule(mbp);
        LearningRule learningRule = getNeuralNet().getLearningRule();
       // learningRule.addListener(this);
        getNeuralNet().learn(getDataset());
         this.getStats().addData(new TrainingResult(mbp.getCurrentIteration(), mbp.getTotalNetworkError(), createMatrix()));
        //testNeuralNetwork(getNeuralNet(), getDataset());
       
    }

    public MomentumTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    @Override
    public void setParameters(BackPropagation bp) {
        MomentumBackpropagation mbp = (MomentumBackpropagation) bp;
        mbp.setLearningRate(getSettings().getLearningRate());
        mbp.setMomentum(getSettings().getLearningRate());
        mbp.setMaxIterations(getSettings().getMaxIterations());
    }

}
