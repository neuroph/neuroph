package org.neuroph.samples.mnist;


import org.neuroph.contrib.evaluation.NeuralNetworkEvaluationService;
import org.neuroph.contrib.model.selection.ErrorEstimationMethod;
import org.neuroph.contrib.model.selection.KFoldCrossValidation;
import org.neuroph.contrib.model.selection.optimizer.MultilayerPerceptronOptimazer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Trains MNIST using MLP
 * @author zoran
 */
public class MultyLayerMNIST {

    private static Logger LOG = LoggerFactory.getLogger(MultyLayerMNIST.class);


    public static void main(String[] args) throws IOException {

        int maxIter = Integer.parseInt(args[0]);
        double maxError = Double.parseDouble(args[1]);
        double learningRate = Double.parseDouble(args[2]);

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

        ErrorEstimationMethod errorEstimationMethod = new KFoldCrossValidation(validationFolds);

        NeuralNetwork neuralNet = new MultilayerPerceptronOptimazer<>()
                .withLearningRule(bp)
                .withErrorEstimationMethod(errorEstimationMethod)
                .withMaxLayers(maxLayers)
                .withMaxNeurons(maxNeuronCount)
                .withMinNeurons(minNeuronCount)
                .withNeuronIncrement(neuronIncrement)
                .createOptimalModel(trainSet);

        LOG.info("Evaluating model on Test Set.....");

        NeuralNetworkEvaluationService.completeEvaluation(neuralNet, testSet);

        LOG.info("MLP learning for MNIST successfully finished.....");
    }
}
