package org.neuroph.samples.evaluation;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.samples.evaluation.domain.MetricResult;
import org.neuroph.samples.evaluation.evaluators.CrossEntropyEvaluator;
import org.neuroph.samples.evaluation.evaluators.MeanSquareErrorEvaluator;
import org.neuroph.samples.evaluation.evaluators.MetricsEvaluator;
import org.neuroph.util.TransferFunctionType;

public class TestBinaryClass {


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

        DataSet trainingSet = new DataSet(2, 1);
        trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));

        // create multi layer perceptron
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 3, 1);


        neuralNet.learn(trainingSet);

        NeuralNetworkEvaluationService neuralNetworkEvaluationService = new NeuralNetworkEvaluationService();
        neuralNetworkEvaluationService.add(MeanSquareErrorEvaluator.class, new MeanSquareErrorEvaluator());
        neuralNetworkEvaluationService.add(CrossEntropyEvaluator.class, new CrossEntropyEvaluator());
        neuralNetworkEvaluationService.add(MetricsEvaluator.class, MetricsEvaluator.createEvaluator(trainingSet));
        neuralNetworkEvaluationService.evaluate(neuralNet, trainingSet);

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
