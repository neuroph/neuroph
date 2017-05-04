package org.neuroph.contrib.bpbench;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.learning.QuickPropagation;

/**
 * Class that set up neural network with quickpropagation learning rule and
 * predefined settings
 *
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class QuickpropagationTraining extends AbstractTraining {

    /**
     * Create instance of a training using predefined neural network and given
     * settings
     *
     * @param neuralNet
     * @param dataset
     * @param settings
     */
    public QuickpropagationTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    /**
     * Create instance of a training using given settings and create neural
     * network from that settings
     *
     * @param dataset
     * @param settings
     */
    public QuickpropagationTraining(DataSet dataset, TrainingSettings settings) {
        super(dataset, settings);
    }

    /**
     * Method that set up learning rule with given settings, learns dataset and
     * creates statistics from results of the test
     */
    @Override
    public void testNeuralNet() {
        QuickPropagation qp = (QuickPropagation) setParameters();
        getNeuralNet().setLearningRule(qp);
        getNeuralNet().learn(getDataset());
        this.getStats().addData(new TrainingResult(qp.getCurrentIteration(), qp.getTotalNetworkError(), createMatrix()));
        this.getStats().calculateParameters();

    }

    /**
     * Create instance of learning rule and setup given parameters
     *
     * @return returns learning rule with predefined parameters
     */
    @Override
    public LearningRule setParameters() {
        QuickPropagation qp = new QuickPropagation();
        qp.setBatchMode(true);
        qp.setLearningRate(getSettings().getLearningRate());
        qp.setMaxError(getSettings().getLearningRate());
        qp.setMaxIterations(getSettings().getMaxIterations());
        return qp;
    }

}
