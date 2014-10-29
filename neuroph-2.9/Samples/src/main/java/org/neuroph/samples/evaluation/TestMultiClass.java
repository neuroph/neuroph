package org.neuroph.samples.evaluation;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.samples.evaluation.error.CrossEntropyEvaluator;
import org.neuroph.samples.evaluation.error.MSEEvaluator;
import org.neuroph.samples.evaluation.performance.MultiClassEvaluator;

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

        GenericEvaluator genericEvaluator = new GenericEvaluator();
        genericEvaluator.add(MSEEvaluator.class, new MSEEvaluator());
        genericEvaluator.add(CrossEntropyEvaluator.class, new CrossEntropyEvaluator());
        genericEvaluator.add(MultiClassEvaluator.class, new MultiClassEvaluator(new String[]{"1", "2", "3"}, 3));

        genericEvaluator.evaluate(neuralNet, irisDataSet);


        System.out.println("METRICS: ");
//        System.out.println(genericEvaluator.resultFor(MSEEvaluator.class).getEvaluationResult().getError());
//        System.out.println(genericEvaluator.resultFor(CrossEntropyEvaluator.class).getEvaluationResult().getError());
//        System.out.println(genericEvaluator.resultFor(MultiClassEvaluator.class).getEvaluationResult().getAccuracy());
//        System.out.println(genericEvaluator.resultFor(MultiClassEvaluator.class).getEvaluationResult().getError());
//        System.out.println(genericEvaluator.resultFor(MultiClassEvaluator.class).getEvaluationResult().getPrecision());
//        System.out.println(genericEvaluator.resultFor(MultiClassEvaluator.class).getEvaluationResult().getRecall());
        System.out.println(genericEvaluator.resultFor(MultiClassEvaluator.class).getEvaluationResult().getfScore());
    }

}
