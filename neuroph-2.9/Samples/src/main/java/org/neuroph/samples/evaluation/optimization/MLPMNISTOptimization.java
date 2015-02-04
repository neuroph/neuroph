package org.neuroph.samples.evaluation.optimization;

import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.contrib.model.modelselection.MultilayerPerceptronOptimazer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;

import java.io.IOException;

/**
 * Example which demonstrated how to use MultilayerPerceptronOptimazer in order to create optimal
 * network architecture for MNIST dataset
 * <p/>
 * Default optimization parameters are used.
 */
public class MLPMNISTOptimization {

    public static void main(String[] args) throws IOException {

        DataSet trainSet = MNISTDataSet.createFromFile(MNISTDataSet.TRAIN_LABEL_NAME, MNISTDataSet.TRAIN_IMAGE_NAME, 200);
        DataSet testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, 10000);
        BackPropagation learningRule = createLearningRule();


        NeuralNetwork neuralNet = new MultilayerPerceptronOptimazer<>()
                .withLearningRule(learningRule)
                .createOptimalModel(trainSet);

        Evaluation.runFullEvaluation(neuralNet, testSet);
    }

    private static BackPropagation createLearningRule() {
        BackPropagation learningRule = new BackPropagation();
        learningRule.setMaxIterations(100);
        learningRule.setMaxError(0.0001);
        return learningRule;
    }


}
