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

    @Override
    public void testNeuralNet() {
        QuickPropagation qp = new QuickPropagation();
        setParameters(qp);
        getNeuralNet().setLearningRule(qp);
        LearningRule learningRule = getNeuralNet().getLearningRule();
        learningRule.addListener(this);
        getNeuralNet().learn(getDataset());
        //testNeuralNetwork(getNeuralNet(), getDataset());
       
    }

 

    public QuickpropagationTraining(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        super(neuralNet, dataset, settings);
    }

    @Override
    public void setParameters(BackPropagation bp) {
        QuickPropagation qp = (QuickPropagation) bp;
        qp.setBatchMode(getSettings().isBatchMode());
        qp.setLearningRate(getSettings().getLearningRate());
        qp.setMaxError(getSettings().getMaxError());
    }


}
