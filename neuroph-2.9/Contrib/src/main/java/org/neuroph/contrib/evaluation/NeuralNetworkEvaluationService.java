package org.neuroph.contrib.evaluation;

import org.neuroph.contrib.evaluation.domain.MetricResult;
import org.neuroph.contrib.evaluation.evaluators.CrossEntropyEvaluator;
import org.neuroph.contrib.evaluation.evaluators.MeanSquareErrorEvaluator;
import org.neuroph.contrib.evaluation.evaluators.MetricsEvaluator;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.contrib.evaluation.evaluators.NeurophEvaluator;
import org.neuroph.nnet.learning.BackPropagation;

import java.util.*;

public class NeuralNetworkEvaluationService {

    private Map<Class<?>, NeurophEvaluator> evaluators = new HashMap<>();


    public void evaluate(NeuralNetwork neuralNetwork, DataSet dataSet) {
        for (DataSetRow dataRow : dataSet.getRows()) {
            forwardPass(neuralNetwork, dataRow);
            for (NeurophEvaluator evaluator : evaluators.values()) {
                evaluator.processResult(neuralNetwork.getOutput(), dataRow.getDesiredOutput());
            }
        }
    }

    private void forwardPass(NeuralNetwork neuralNetwork, DataSetRow dataRow) {
        neuralNetwork.setInput(dataRow.getInput());
        neuralNetwork.calculate();
    }


    public <T extends NeurophEvaluator> void add(Class<T> type, T instance) {
        if (type == null)
            throw new NullPointerException("Type is null. Please make sure that you pass correct class.");
        evaluators.put(type, instance);

    }

    public <T extends NeurophEvaluator> T resultFor(Class<T> type) {
        return type.cast(evaluators.get(type));
    }

    public static void completeEvaluation(NeuralNetwork<BackPropagation> neuralNet, DataSet dataSet) {

        NeuralNetworkEvaluationService neuralNetworkEvaluationService = new NeuralNetworkEvaluationService();
        neuralNetworkEvaluationService.add(MeanSquareErrorEvaluator.class, new MeanSquareErrorEvaluator());
        neuralNetworkEvaluationService.add(CrossEntropyEvaluator.class, new CrossEntropyEvaluator());
        neuralNetworkEvaluationService.add(MetricsEvaluator.class, MetricsEvaluator.createEvaluator(dataSet));

        neuralNetworkEvaluationService.evaluate(neuralNet, dataSet);


        System.out.println("#################################################");
        System.out.println("Errors: ");
        System.out.println("MSE Error: " + neuralNetworkEvaluationService.resultFor(MeanSquareErrorEvaluator.class).getEvaluationResult().getError());
        System.out.println("CrossEntropy Error: " + neuralNetworkEvaluationService.resultFor(CrossEntropyEvaluator.class).getEvaluationResult().getError());
        System.out.println("#################################################");
        System.out.println("Metrics: ");
        MetricResult result = neuralNetworkEvaluationService.resultFor(MetricsEvaluator.class).getEvaluationResult();
        System.out.println("Accuracy: " + result.getAccuracy());
        System.out.println("Error Rate: " + result.getError());
        System.out.println("Precision: " + result.getPrecision());
        System.out.println("Recall: " + result.getRecall());
        System.out.println("FScore: " + result.getfScore());
        System.out.println("#################################################");
    }


}
