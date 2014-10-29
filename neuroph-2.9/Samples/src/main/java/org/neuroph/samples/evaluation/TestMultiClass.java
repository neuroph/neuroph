package org.neuroph.samples.evaluation;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.samples.evaluation.domain.MetricResult;
import org.neuroph.samples.evaluation.evaluators.CrossEntropyEvaluator;
import org.neuroph.samples.evaluation.evaluators.MeanSquareErrorEvaluator;
import org.neuroph.samples.evaluation.evaluators.MetricsEvaluator;

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

        // create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(4, 5, 3);
        // create training set from file
        DataSet irisDataSet = DataSet.createFromFile(inputFileName, 4, 3, ",", false);
        irisDataSet.shuffle();
        // train the network with training set

        neuralNet.getLearningRule().addListener(new LearningListener());
        neuralNet.getLearningRule().setLearningRate(0.2);
        neuralNet.getLearningRule().setMaxIterations(111);

        neuralNet.learn(irisDataSet);


        NeuralNetworkEvaluationService neuralNetworkEvaluationService = new NeuralNetworkEvaluationService();
        neuralNetworkEvaluationService.add(MeanSquareErrorEvaluator.class, new MeanSquareErrorEvaluator());
        neuralNetworkEvaluationService.add(CrossEntropyEvaluator.class, new CrossEntropyEvaluator());
        neuralNetworkEvaluationService.add(MetricsEvaluator.class, MetricsEvaluator.createEvaluator(irisDataSet));

        neuralNetworkEvaluationService.evaluate(neuralNet, irisDataSet);


        System.out.println("#################################################");
        System.out.println("Error Metrics: ");
        System.out.println("MSE Error: " + neuralNetworkEvaluationService.resultFor(MeanSquareErrorEvaluator.class).getEvaluationResult().getError());
        System.out.println("CrossEntropy Error: " + neuralNetworkEvaluationService.resultFor(CrossEntropyEvaluator.class).getEvaluationResult().getError());
        System.out.println("#################################################");
        System.out.println("Base Metrics: ");
        MetricResult result = neuralNetworkEvaluationService.resultFor(MetricsEvaluator.class).getEvaluationResult();
        System.out.println("Accuracy: " + result.getAccuracy());
        System.out.println("Error Rate: " + result.getError());
        System.out.println("Precision: " + result.getPrecision());
        System.out.println("Recall: " + result.getRecall());
        System.out.println("FScore: " + result.getfScore());
        System.out.println("#################################################");
    }

}
