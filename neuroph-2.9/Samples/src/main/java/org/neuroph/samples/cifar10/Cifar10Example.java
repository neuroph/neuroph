package org.neuroph.samples.cifar10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.eval.Evaluation;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.ConvolutionalBackpropagation;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cifar10Example {
     private static Logger LOG = LoggerFactory.getLogger(Cifar10Example.class);
    
     public static void main(String[] args) throws IOException {
         int maxIter = 20; // Integer.parseInt(args[0]);
         double maxError = 0.03; //Double.parseDouble(args[1]);
         double learningRate = 0.3; //  Double.parseDouble(args[2]);
         int layer1 = 10;//Integer.parseInt(args[3]);
         int layer2 = 15;//Integer.parseInt(args[4]);
         int layer3 = 20;//Integer.parseInt(args[5]);
         LOG.info("{}-{}-{}", layer1, layer2, layer3);
       /*  String putanja = "C:\\Users\\jecak_000\\Documents\\Neuroph\\neuroph_novaVerzija\\neurophNoviPull\\neuroph-2.9\\Samples\\";
         String labelName = putanja.concat(MNISTDataSet.TRAIN_LABEL_NAME);
         String trainImage = putanja.concat(MNISTDataSet.TRAIN_IMAGE_NAME);
         String testLabel =  putanja.concat(MNISTDataSet.TEST_LABEL_NAME);
         String testImage  = putanja.concat(MNISTDataSet.TEST_IMAGE_NAME);
         DataSet trainSet = MNISTDataSet.createFromFile(labelName,trainImage, 100);
         DataSet testSet = MNISTDataSet.createFromFile(testLabel,testImage, 100);*/
       
       //DataSet trainSet = DataSet.load("C:\\Users\\jecak_000\\Documents\\NetBeansProjects\\PrepoznavanjeSlika_p1\\Training Sets\\cifar_veciSkupTrening.tset");
       //DataSet testSwithPoolingLayeret = trainSet;//DataSet.load("C:\\Users\\jecak_000\\Documents\\NetBeansProjects\\PrepoznavanjeSlika_p1\\Test Sets\\cifar_veciSkupTest.tset");
       List<String> labels = Arrays.asList(new String[]{"airplane","automobile","bird","cat","deer","dog","frog","horse","ship","truck"});

       //trainSet.setColumnNames(labels);
       //testSet.setColumnNames(labels);'
       DataSet trainSet = ImageRecognitionHelper.createImageDataSetFromFile("D:\\Doktorske\\Beograd\\Neuronske mreze - Zoran Sevarac\\Cifar 10\\train\\train_1000\\", labels, "", ColorMode.COLOR_RGB, new Dimension(32, 32), "cifar", 1);
       DataSet testSet = trainSet;
         
       /*  Dimension2D inputDimension = new Dimension2D(32, 32);
         Dimension2D convolutionKernel = new Dimension2D(5, 5);
         Dimension2D poolingKernel = new Dimension2D(2, 2);*/
         ConvolutionalNetwork convolutionNetwork = new ConvolutionalNetwork.Builder()
                 .withInputLayer(32, 32, 3)
                 .withConvolutionLayer(5, 5, layer1)
                 .withPoolingLayer(2, 2)
                 .withConvolutionLayer(5, 5, layer2)
                 .withPoolingLayer(2, 2)
                 .withConvolutionLayer(5, 5, layer3)
                 .withFullConnectedLayer(10)
                 .build();
         ConvolutionalBackpropagation backPropagation = new ConvolutionalBackpropagation();
         backPropagation.setLearningRate(learningRate);
         backPropagation.setMaxError(maxError);
         backPropagation.setMaxIterations(maxIter);
         backPropagation.addListener(new LearningListener(convolutionNetwork, testSet));
         backPropagation.setErrorFunction(new MeanSquaredError());
         convolutionNetwork.setLearningRule(backPropagation);
         convolutionNetwork.learn(trainSet);
        Evaluation.runFullEvaluation(convolutionNetwork, testSet);
    
}
     private static class LearningListener implements LearningEventListener {

        private final NeuralNetwork neuralNetwork;
        private DataSet testSet;
        // Ovde moze da se doda da nakon k iteracija smanji learning rate

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
