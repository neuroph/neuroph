package org.neuroph.samples.evaluation.optimization;

import org.neuroph.contrib.evaluation.NeuralNetworkEvaluationService;
import org.neuroph.contrib.model.selection.optimizer.MultilayerPerceptronOptimazer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;

import java.io.IOException;

public class MLPMNISTOptimization {

    public static void main(String[] args) throws IOException {

        DataSet trainSet = MNISTDataSet.createFromFile(MNISTDataSet.TRAIN_LABEL_NAME, MNISTDataSet.TRAIN_IMAGE_NAME, 60000);

        DataSet testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, 10000);

        NeuralNetwork neuralNet = new MultilayerPerceptronOptimazer<>().createOptimalModel(trainSet);

        NeuralNetworkEvaluationService.completeEvaluation(neuralNet, testSet);


    }

}
