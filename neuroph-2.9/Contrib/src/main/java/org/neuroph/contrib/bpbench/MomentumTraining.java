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

     public MomentumTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    public MomentumTraining(DataSet dataset, TrainingSettings settings) {
        super(dataset, settings);
    }
     
    
    @Override
    public void testNeuralNet() {
        MomentumBackpropagation mbp = (MomentumBackpropagation) setParameters();
        getNeuralNet().setLearningRule(mbp);
        getNeuralNet().learn(getDataset());
        this.getStats().addData(new TrainingResult(mbp.getCurrentIteration(), mbp.getTotalNetworkError(), createMatrix()));
        this.getStats().calculateParameters();

    }

   

    @Override
    public LearningRule setParameters() {
        MomentumBackpropagation mbp = new MomentumBackpropagation();
        mbp.setBatchMode(getSettings().isBatchMode());
        mbp.setLearningRate(getSettings().getLearningRate());
        mbp.setMaxError(getSettings().getMaxError());
        mbp.setMaxIterations(getSettings().getMaxIterations());
        mbp.setMomentum(getSettings().getMomentum());
        return mbp;
    }

}
