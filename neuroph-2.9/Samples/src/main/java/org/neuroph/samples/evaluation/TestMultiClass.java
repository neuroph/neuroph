package org.neuroph.samples.evaluation;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.contrib.evaluation.NeuralNetworkEvaluationService;

public class TestMultiClass {


    static class LearningListener implements LearningEventListener {
        public void handleLearningEvent(LearningEvent event) {
            long start = System.currentTimeMillis();
            BackPropagation bp = (BackPropagation) event.getSource();
            System.out.println("Current iteration: " + bp.getCurrentIteration());
            System.out.println("Error: " + bp.getTotalNetworkError());
            System.out.println((System.currentTimeMillis() - start) / 1000.0);
        }

    }

    public static void main(String[] args) {
        String inputFileName = "/iris_data.txt";

        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(4, 5, 3);
        DataSet irisDataSet = DataSet.createFromFile(inputFileName, 4, 3, ",", false);
        irisDataSet.shuffle();

        neuralNet.getLearningRule().addListener(new LearningListener());
        neuralNet.getLearningRule().setLearningRate(0.2);
        neuralNet.getLearningRule().setMaxIterations(100);

        neuralNet.learn(irisDataSet);

        NeuralNetworkEvaluationService.completeEvaluation(neuralNet, irisDataSet);

    }

}
