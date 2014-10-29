package org.neuroph.samples.evaluation;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.samples.evaluation.error.CrossEntropyEvaluator;
import org.neuroph.samples.evaluation.error.MSEEvaluator;
import org.neuroph.samples.evaluation.performance.BinaryClassEvaluator;
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

        GenericEvaluator genericEvaluator = new GenericEvaluator();
        genericEvaluator.add(MSEEvaluator.class, new MSEEvaluator());
        genericEvaluator.add(CrossEntropyEvaluator.class, new CrossEntropyEvaluator());
        genericEvaluator.add(BinaryClassEvaluator.class, new BinaryClassEvaluator(0));
        genericEvaluator.evaluate(neuralNet, trainingSet);


        System.out.println("METRICS: ");
        System.out.println(genericEvaluator.resultFor(MSEEvaluator.class).getEvaluationResult().getError());
        System.out.println(genericEvaluator.resultFor(CrossEntropyEvaluator.class).getEvaluationResult().getError());
        System.out.println(genericEvaluator.resultFor(BinaryClassEvaluator.class).getEvaluationResult().getAccuracy());
        System.out.println(genericEvaluator.resultFor(BinaryClassEvaluator.class).getEvaluationResult().getError());
        System.out.println(genericEvaluator.resultFor(BinaryClassEvaluator.class).getEvaluationResult().getPrecision());
        System.out.println(genericEvaluator.resultFor(BinaryClassEvaluator.class).getEvaluationResult().getRecall());
        System.out.println(genericEvaluator.resultFor(BinaryClassEvaluator.class).getEvaluationResult().getfScore());

    }

}
