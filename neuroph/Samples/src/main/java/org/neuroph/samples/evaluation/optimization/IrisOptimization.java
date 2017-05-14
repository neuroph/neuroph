package org.neuroph.samples.evaluation.optimization;

import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.contrib.model.modelselection.MultilayerPerceptronOptimazer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;

/**
 * Example which demonstrated how to use MultilayerPerceptronOptimazer in order to create optimal
 * network architecture for IRIS dataset
 * <p/>
 * Default optimization parameters are used.
 */
public class IrisOptimization {


    public static void main(String[] args) {
        String inputFileName = "/iris_data.txt";

        DataSet irisDataSet = DataSet.createFromFile(inputFileName, 4, 3, ",", false);
        BackPropagation learningRule = createLearningRule();

        NeuralNetwork neuralNet = new MultilayerPerceptronOptimazer<>()
                .withLearningRule(learningRule)
                .createOptimalModel(irisDataSet);

        neuralNet.learn(irisDataSet);
        Evaluation.runFullEvaluation(neuralNet, irisDataSet);

    }

    private static BackPropagation createLearningRule() {
        BackPropagation learningRule = new BackPropagation();
        learningRule.setMaxIterations(50);
        learningRule.setMaxError(0.0001);
        return learningRule;
    }

}
