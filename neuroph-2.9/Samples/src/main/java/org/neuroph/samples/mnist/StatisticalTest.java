package org.neuroph.samples.mnist;

import org.neuroph.contrib.evaluation.NeuralNetworkEvaluationService;
import org.neuroph.contrib.evaluation.stat.McNemarTest;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;

import java.io.FileInputStream;
import java.io.IOException;

public class StatisticalTest {


    public static void main(String[] args) throws IOException {

        DataSet testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, 10000);


        NeuralNetwork nn1 = NeuralNetwork.load(new FileInputStream(args[0]));
        NeuralNetwork nn2 = NeuralNetwork.load(new FileInputStream(args[1]));

        new McNemarTest().evaluateNetworks(nn1, nn2, testSet);


    }

}
