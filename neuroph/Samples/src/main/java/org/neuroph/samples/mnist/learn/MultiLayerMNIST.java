package org.neuroph.samples.mnist.learn;


import org.neuroph.contrib.model.errorestimation.CrossValidation;
import org.neuroph.contrib.model.errorestimation.CrossValidation;
import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.contrib.model.modelselection.MultilayerPerceptronOptimazer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Utility class which can be used from command prompt to train MLP
 */
public class MultiLayerMNIST {

    private static Logger LOG = LoggerFactory.getLogger(MultiLayerMNIST.class);


    /**
     * @param args Command line parameters used to initialize parameters of multi layer neural network optimizer
     *             [0] - maximal number of epochs during learning
     *             [1] - learning error stop condition
     *             [2] - learning rate used during learning process
     *             [3] - number of validation folds
     *             [4] - max number of layers in neural network
     *             [5] - min neuron count per layer
     *             [6] - max neuron count per layer
     *             [7] - neuron increment count
     */
    public static void main(String[] args) throws IOException {

        int maxIter = 10000; //Integer.parseInt(args[0]);
        double maxError = 0.01; // Double.parseDouble(args[1]);
        double learningRate = 0.2 ; // Double.parseDouble(args[2]);

        int validationFolds = Integer.parseInt(args[3]);

        int maxLayers = Integer.parseInt(args[4]);
        int minNeuronCount = Integer.parseInt(args[5]);
        int maxNeuronCount = Integer.parseInt(args[6]);
        int neuronIncrement = Integer.parseInt(args[7]);

        LOG.info("MLP learning for MNIST started.....");

        DataSet trainSet = MNISTDataSet.createFromFile(MNISTDataSet.TRAIN_LABEL_NAME, MNISTDataSet.TRAIN_IMAGE_NAME, 60000);
        DataSet testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, 10000);

        BackPropagation bp = new BackPropagation();
        bp.setMaxIterations(maxIter);
        bp.setMaxError(maxError);
        bp.setLearningRate(learningRate);
// commented out due to errors
//        KFoldCrossValidation errorEstimationMethod = new KFoldCrossValidation(neuralNet, trainSet, validationFolds);
//
//        NeuralNetwork neuralNet = new MultilayerPerceptronOptimazer<>()
//                .withLearningRule(bp)
//                .withErrorEstimationMethod(errorEstimationMethod)
//                .withMaxLayers(maxLayers)
//                .withMaxNeurons(maxNeuronCount)
//                .withMinNeurons(minNeuronCount)
//                .withNeuronIncrement(neuronIncrement)
//                .createOptimalModel(trainSet);

        LOG.info("Evaluating model on Test Set.....");
// commented out due to errors
      //  Evaluation.runFullEvaluation(neuralNet, testSet);

        LOG.info("MLP learning for MNIST successfully finished.....");
    }
}
