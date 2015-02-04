package org.neuroph.samples.mnist.test;

import org.neuroph.contrib.eval.classification.McNemarTest;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * McNemar test calculated for MNIST data set
 */
public class McNemarTestMNIST {


    /**
     * @param args command line arguments which represent paths to persisted neural networks
     *             [0] - location of first neural network
     *             [1] - location of second neural network
     */
    public static void main(String[] args) throws IOException {

        DataSet testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, 10000);

        NeuralNetwork nn1 = NeuralNetwork.load(new FileInputStream(args[0]));
        NeuralNetwork nn2 = NeuralNetwork.load(new FileInputStream(args[1]));

        new McNemarTest().evaluateNetworks(nn1, nn2, testSet);
    }

}
