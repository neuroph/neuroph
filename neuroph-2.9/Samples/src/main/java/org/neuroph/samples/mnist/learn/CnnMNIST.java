package org.neuroph.samples.mnist.learn;

import java.io.IOException;

import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class which can be used from command prompt to train ConvolutionalNetwork
 */
public class CnnMNIST {

    private static Logger LOG = LoggerFactory.getLogger(CnnMNIST.class);


    /**
     * @param args Command line parameters used to initialize parameters of convolutional network
     *             [0] - maximal number of epochs during learning
     *             [1] - learning error stop condition
     *             [2] - learning rate used during learning process
     *             [3] - number of feature maps in 1st convolutional layer
     *             [4] - number of feature maps in 2nd convolutional layer
     *             [5] - number of feature maps in 3rd convolutional layer
     */
    public static void main(String[] args) {
        try {
            int maxIter = 10000; // Integer.parseInt(args[0]);
            double maxError = 0.01; //Double.parseDouble(args[1]);
            double learningRate = 0.2; //  Double.parseDouble(args[2]);

            int layer1 = Integer.parseInt(args[3]);
            int layer2 = Integer.parseInt(args[4]);
            int layer3 = Integer.parseInt(args[5]);

            LOG.info("{}-{}-{}", layer1, layer2, layer3);

            DataSet trainSet = MNISTDataSet.createFromFile(MNISTDataSet.TRAIN_LABEL_NAME, MNISTDataSet.TRAIN_IMAGE_NAME, 100);
            DataSet testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, 10000);

            Layer2D.Dimensions inputDimension = new Layer2D.Dimensions(32, 32);
            Kernel convolutionKernel = new Kernel(5, 5);
            Kernel poolingKernel = new Kernel(2, 2);

            ConvolutionalNetwork convolutionNetwork = new ConvolutionalNetwork.Builder(inputDimension, 1)
                    .withConvolutionLayer(convolutionKernel, layer1)
                    .withPoolingLayer(poolingKernel)
                    .withConvolutionLayer(convolutionKernel, layer2)
                    .withPoolingLayer(poolingKernel)
                    .withConvolutionLayer(convolutionKernel, layer3)
                    .withFullConnectedLayer(10)
                    .createNetwork();

            BackPropagation backPropagation = new MomentumBackpropagation();
            backPropagation.setLearningRate(learningRate);
            backPropagation.setMaxError(maxError);
            backPropagation.setMaxIterations(maxIter);
            backPropagation.addListener(new LearningListener(convolutionNetwork, testSet));
            backPropagation.setErrorFunction(new MeanSquaredError());

            convolutionNetwork.setLearningRule(backPropagation);
            convolutionNetwork.learn(trainSet);

            Evaluation.runFullEvaluation(convolutionNetwork, testSet);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class LearningListener implements LearningEventListener {

        private final NeuralNetwork neuralNetwork;
        private DataSet testSet;

        public LearningListener(NeuralNetwork neuralNetwork, DataSet testSet) {
            this.testSet = testSet;
            this.neuralNetwork = neuralNetwork;
        }


        long start = System.currentTimeMillis();

        public void handleLearningEvent(LearningEvent event) {
            BackPropagation bp = (BackPropagation) event.getSource();
            LOG.info("Epoch no#: [{}]. Error [{}]", bp.getCurrentIteration(), bp.getTotalNetworkError());
            LOG.info("Epoch execution time: {} sec", (System.currentTimeMillis() - start) / 1000.0);
           // neuralNetwork.save(bp.getCurrentIteration() + "_MNIST_CNN-MIC.nnet");

            start = System.currentTimeMillis();
          //  if (bp.getCurrentIteration() % 5 == 0)
          //      Evaluation.runFullEvaluation(neuralNetwork, testSet);
        }

    }


}
