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
 * Class that set up neural network with backpropagation learning rule and
 * predefined settings
 *
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class BackpropagationTraining extends AbstractTraining {

    /**
     * Create instance of a training using predefined neural network and given
     * settings
     *
     * @param neuralNet
     * @param dataset
     * @param settings
     */
    public BackpropagationTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    /**
     * Create instance of a training using given settings and create neural
     * network from that settings
     *
     * @param dataset
     * @param settings
     */
    public BackpropagationTraining(DataSet dataset, TrainingSettings settings) {
        super(dataset, settings);
    }

    /**
     * Method that set up learning rule with given settings, learns dataset and
     * creates statistics from results of the test
     */
    @Override
    public void testNeuralNet() {
        BackPropagation bp = (BackPropagation) this.setParameters();
        this.getNeuralNet().setLearningRule(bp);
        this.getNeuralNet().learn(this.getDataset());
        this.getStats().addData(new TrainingResult(bp.getCurrentIteration(), bp.getTotalNetworkError(), createMatrix()));
        this.getStats().calculateParameters();

    }

    /**
     * Create instance of learning rule and setup given parameters
     * @return returns learning rule with predefined parameters
     */
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
