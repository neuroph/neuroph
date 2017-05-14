package org.neuroph.samples.evaluation;

import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

/**
 * Simple example which shows how to use EvaluationService on Binary classification problem (XOR problem)
 */
public class TestBinaryClass {

    public static void main(String[] args) {

        DataSet trainingSet = new DataSet(2, 1);
        trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));

        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 3, 1);
        neuralNet.learn(trainingSet);

        Evaluation.runFullEvaluation(neuralNet, trainingSet);
    }

}
