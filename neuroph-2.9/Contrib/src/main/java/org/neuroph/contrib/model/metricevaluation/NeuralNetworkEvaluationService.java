package org.neuroph.contrib.model.metricevaluation;

import org.neuroph.contrib.model.metricevaluation.domain.MetricResult;
import org.neuroph.contrib.model.metricevaluation.evaluators.ErrorEvaluator;
import org.neuroph.contrib.model.metricevaluation.evaluators.MetricsEvaluator;
import org.neuroph.contrib.model.metricevaluation.evaluators.NeurophEvaluator;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.learning.BackPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Evaluation service used to run different evaluators on trained model
 */
public class NeuralNetworkEvaluationService {

    private static Logger LOG = LoggerFactory.getLogger(NeuralNetworkEvaluationService.class);

    private Map<Class<?>, NeurophEvaluator> evaluators = new HashMap<>();


    /**
     * @param neuralNetwork trained neural network
     * @param dataSet       test data set used for evaluation
     */
    public void evaluate(NeuralNetwork neuralNetwork, DataSet dataSet) {
        for (DataSetRow dataRow : dataSet.getRows()) {
            forwardPass(neuralNetwork, dataRow);
            for (NeurophEvaluator evaluator : evaluators.values()) {
                evaluator.processResult(neuralNetwork.getOutput(), dataRow.getDesiredOutput());
            }
        }
    }

    /**
     * @param type
     * @param instance
     * @param <T>
     */
    public <T extends NeurophEvaluator> void add(Class<T> type, T instance) {
        if (type == null)
            throw new NullPointerException("Type is null. Please make sure that you pass correct class.");
        evaluators.put(type, instance);

    }

    /**
     * @param type concrete evaluator class
     * @return result of evaluation for given Evaluator type
     */
    public <T extends NeurophEvaluator> T resultFor(Class<T> type) {
        return type.cast(evaluators.get(type));
    }


    /**
     * Out of the box method (util) which computes all metrics for given neural network and test data set
     */
    public static void completeEvaluation(NeuralNetwork<BackPropagation> neuralNet, DataSet dataSet) {

        NeuralNetworkEvaluationService neuralNetworkEvaluationService = new NeuralNetworkEvaluationService();
        neuralNetworkEvaluationService.add(ErrorEvaluator.class, new ErrorEvaluator(new MeanSquaredError()));
        neuralNetworkEvaluationService.add(MetricsEvaluator.class, MetricsEvaluator.createEvaluator(dataSet));

        neuralNetworkEvaluationService.evaluate(neuralNet, dataSet);

        LOG.info("#################################################");
        LOG.info("Errors: ");
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


    private void forwardPass(NeuralNetwork neuralNetwork, DataSetRow dataRow) {
        neuralNetwork.setInput(dataRow);
        neuralNetwork.calculate();
    }

}
