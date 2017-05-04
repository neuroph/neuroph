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
 * Class that set up neural network with momentum backpropagation learning rule
 * and predefined settings
 *
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class MomentumTraining extends AbstractTraining {

    /**
     * Create instance of a training using predefined neural network and given
     * settings
     *
     * @param neuralNet
     * @param dataset
     * @param settings
     */
    
    public MomentumTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    /**
     * Create instance of a training using given settings and create neural
     * network from that settings
     *
     * @param dataset
     * @param settings
     */
    
    public MomentumTraining(DataSet dataset, TrainingSettings settings) {
        super(dataset, settings);
    }

    /**
     * Method that set up learning rule with given settings, learns dataset and
     * creates statistics from results of the test
     */
    
    @Override
    public void testNeuralNet() {
        MomentumBackpropagation mbp = (MomentumBackpropagation) setParameters();
        getNeuralNet().setLearningRule(mbp);
        getNeuralNet().learn(getDataset());
        this.getStats().addData(new TrainingResult(mbp.getCurrentIteration(), mbp.getTotalNetworkError(), createMatrix()));
        this.getStats().calculateParameters();

    }

    /**
     * Create instance of learning rule and setup given parameters
     *
     * @return returns learning rule with predefined parameters
     */
    
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
