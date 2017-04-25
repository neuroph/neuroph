/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import java.util.Properties;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.learning.BackPropagation;

/**
 *
 * @author Mladen
 */
public class BackpropagationTraining extends Training {

    public BackpropagationTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    public BackpropagationTraining(DataSet dataset, TrainingSettings settings) {
        super(dataset, settings);
    }

    @Override
    public void testNeuralNet() {
        BackPropagation bp = (BackPropagation) this.setParameters();
        this.getNeuralNet().setLearningRule(bp);
        this.getNeuralNet().learn(this.getDataset());
        this.getStats().addData(new TrainingResult(bp.getCurrentIteration(), bp.getTotalNetworkError(), createMatrix()));
        this.getStats().calculateParameters();

    }

    @Override
    public LearningRule setParameters() {
        BackPropagation bp = new BackPropagation();
        bp.setLearningRate(getSettings().getLearningRate());
        bp.setMaxError(getSettings().getMaxError());
        bp.setBatchMode(getSettings().isBatchMode());
        bp.setMaxIterations(getSettings().getMaxIterations());
        return bp;
    }
}
