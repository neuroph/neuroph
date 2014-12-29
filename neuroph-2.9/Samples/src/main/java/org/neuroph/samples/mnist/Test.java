package org.neuroph.samples.mnist;

import org.neuroph.contrib.evaluation.NeuralNetworkEvaluationService;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;

import java.io.FileInputStream;
import java.io.IOException;



/**
 * Calculates MNIST metrics for given neural newtork
 * @author zoran
 */
public class Test {

    public static void main(String[] args) throws IOException {

        DataSet testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, 10000);

        NeuralNetwork nn = NeuralNetwork.load(new FileInputStream(args[0]));

        NeuralNetworkEvaluationService.completeEvaluation(nn, testSet);


    }

}
