package org.neuroph.contrib.evaluation;

import org.neuroph.contrib.evaluation.domain.MetricResult;
import org.neuroph.contrib.evaluation.evaluators.ErrorEvaluator;
import org.neuroph.contrib.evaluation.evaluators.MetricsEvaluator;
import org.neuroph.contrib.learning.CrossEntropyError;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.contrib.evaluation.evaluators.NeurophEvaluator;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.learning.BackPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NeuralNetworkEvaluationService {

    private static Logger LOG = LoggerFactory.getLogger(NeuralNetworkEvaluationService.class);


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
        neuralNetwork.setInput(dataRow);
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
//        neuralNetworkEvaluationService.add(ErrorEvaluator.class, new ErrorEvaluator(new MeanSquaredError()));
        neuralNetworkEvaluationService.add(ErrorEvaluator.class, new ErrorEvaluator(new MeanSquaredError()));
        neuralNetworkEvaluationService.add(MetricsEvaluator.class, MetricsEvaluator.createEvaluator(dataSet));

        neuralNetworkEvaluationService.evaluate(neuralNet, dataSet);

       LOG.info("#################################################");
        LOG.info("Errors: ");
//        System.out.println("MSE Error: " + neuralNetworkEvaluationService.resultFor(MeanSquareErrorEvaluator.class).getEvaluationResult().getError());
        LOG.info("MeanSquare Error: " + neuralNetworkEvaluationService.resultFor(ErrorEvaluator.class).getEvaluationResult());
        LOG.info("#################################################");
        LOG.info("Metrics: ");
        MetricResult result = neuralNetworkEvaluationService.resultFor(MetricsEvaluator.class).getEvaluationResult();
        LOG.info("Accuracy: " + result.getAccuracy());
        LOG.info("Error Rate: " + result.getError());
        LOG.info("Precision: " + result.getPrecision());
        LOG.info("Recall: " + result.getRecall());
        LOG.info("FScore: " + result.getFScore());
        LOG.info("#################################################");
    }


}
