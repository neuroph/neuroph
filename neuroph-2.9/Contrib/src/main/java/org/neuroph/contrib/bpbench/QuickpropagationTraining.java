package org.neuroph.contrib.bpbench;

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.QuickPropagation;

/**
 *
 * @author Mladen
 */
public class QuickpropagationTraining extends Training {

    public QuickpropagationTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    public QuickpropagationTraining(DataSet dataset, TrainingSettings settings) {
        super(dataset, settings);
    }

    @Override
    public void testNeuralNet() {
        QuickPropagation qp = (QuickPropagation) setParameters();
        getNeuralNet().setLearningRule(qp);
        getNeuralNet().learn(getDataset());
        this.getStats().addData(new TrainingResult(qp.getCurrentIteration(), qp.getTotalNetworkError(), createMatrix()));
        this.getStats().calculateParameters();

    }

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
