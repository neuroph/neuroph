package org.neuroph.samples.evaluation;

import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.MultiLayerPerceptron;


/**
 * Simple example which shows how to use EvaluationService on Multi-class classification problem (IRIS dataset)
 */
public class TestMultiClass {

    private static final String inputFileName = "/iris_data.txt";


    public static void main(String[] args) {

        DataSet irisDataSet = loadDataSet();

        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(4, 15, 3);

        configureLearningRule(neuralNet);
        neuralNet.learn(irisDataSet);

        Evaluation.runFullEvaluation(neuralNet, irisDataSet);
    }

    private static DataSet loadDataSet() {
        DataSet irisDataSet = DataSet.createFromFile(inputFileName, 4, 3, ",", false);
        irisDataSet.shuffle();
        return irisDataSet;
    }

    private static void configureLearningRule(MultiLayerPerceptron neuralNet) {
        neuralNet.getLearningRule().setLearningRate(0.02);
        neuralNet.getLearningRule().setMaxError(0.01);
        neuralNet.getLearningRule().setErrorFunction(new MeanSquaredError());
    }

}
