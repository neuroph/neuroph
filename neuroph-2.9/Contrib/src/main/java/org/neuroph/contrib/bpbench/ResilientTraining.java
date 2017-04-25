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
public class ResilientTraining extends Training {

    public ResilientTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    public ResilientTraining(DataSet dataset, TrainingSettings settings) {
        super(dataset, settings);
    }
    

    @Override
    public void testNeuralNet() {
        ResilientPropagation rp = (ResilientPropagation) setParameters();
        getNeuralNet().setLearningRule(rp);
        getNeuralNet().learn(getDataset());
        this.getStats().addData(new TrainingResult(rp.getCurrentIteration(), rp.getTotalNetworkError(), createMatrix()));
        this.getStats().calculateParameters();

    }

    @Override
    public LearningRule setParameters() {
        ResilientPropagation rp = new ResilientPropagation();
        rp.setBatchMode(getSettings().isBatchMode());
        rp.setMaxError(getSettings().getMaxError());
        rp.setMaxIterations(getSettings().getMaxIterations());
        return rp;
    }

}
