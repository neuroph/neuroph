package org.neuroph.samples.mnist.test;

import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Utility class used for metrics evaluation of neural network
 */
public class MetricTestMNIST {

    /**
     * @param args command line arguments which represent paths to persisted neural network
     *             [0] - location of  neural network
     */
    public static void main(String[] args) throws IOException {

        DataSet testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, 10000);
        NeuralNetwork nn = NeuralNetwork.load(new FileInputStream(args[0]));

        Evaluation.runFullEvaluation(nn, testSet);
    }

}
