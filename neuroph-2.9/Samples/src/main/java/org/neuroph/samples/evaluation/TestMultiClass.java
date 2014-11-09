package org.neuroph.samples.evaluation;

import org.neuroph.contrib.learning.CrossEntropyError;
import org.neuroph.contrib.model.selection.optimizer.MultilayerPerceptronOptimazer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.contrib.evaluation.NeuralNetworkEvaluationService;

public class TestMultiClass {


    static class LearningListener implements LearningEventListener {
        long start = System.currentTimeMillis();

        public void handleLearningEvent(LearningEvent event) {
            BackPropagation bp = (BackPropagation) event.getSource();
            System.out.println("Current iteration: " + bp.getCurrentIteration());
            System.out.println("Error: " + bp.getTotalNetworkError());
            System.out.println((System.currentTimeMillis() - start) / 1000.0);
            start = System.currentTimeMillis();
        }

    }
    //1.5
    //0.022

    public static void main(String[] args) {
        String inputFileName = "/iris_data.txt";

        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(4, 15, 3);
        DataSet irisDataSet = DataSet.createFromFile(inputFileName, 4, 3, ",", false);
        irisDataSet.shuffle();



        neuralNet.getLearningRule().addListener(new LearningListener());
        neuralNet.getLearningRule().setLearningRate(0.02);
        neuralNet.getLearningRule().setMaxIterations(100);
        neuralNet.getLearningRule().setMaxError(0.00001);
        neuralNet.getLearningRule().setErrorFunction(new CrossEntropyError());

        neuralNet.learn(irisDataSet);


        NeuralNetworkEvaluationService.completeEvaluation(neuralNet, irisDataSet);

    }

}
