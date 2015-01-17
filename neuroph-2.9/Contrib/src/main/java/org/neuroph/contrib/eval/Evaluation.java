package org.neuroph.contrib.eval;

import org.neuroph.contrib.eval.ErrorEvaluator;
import org.neuroph.contrib.eval.ClassificationMetricsEvaluator;
import org.neuroph.contrib.eval.Evaluator;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.learning.BackPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;

/**
 * Evaluation service used to run different evaluators on trained model
 */
public class Evaluation {

    private static Logger LOG = LoggerFactory.getLogger(Evaluation.class);

    private Map<Class<?>, Evaluator> evaluators = new HashMap<>();

    

    /**
     * Runs evaluation procedure for given neural network and data set through all evaluatoors
     * Evaluation results are stored in evaluators
     * 
     * @param neuralNetwork trained neural network
     * @param dataSet       test data set used for evaluation
     */
    public void evaluateDataSet(NeuralNetwork neuralNetwork, DataSet dataSet) {
        for (DataSetRow dataRow : dataSet.getRows()) {
             neuralNetwork.setInput(dataRow.getInput());
             neuralNetwork.calculate();
             
            for (Evaluator evaluator : evaluators.values()) { // for now we have only kfold and mse
                evaluator.processNetworkResult(neuralNetwork.getOutput(), dataRow.getDesiredOutput());
            }
        }
    }

    /**
     * @param type
     * @param instance
     * @param <T>
     */
    public  void addEvaluator(Evaluator evaluator) { /* <T extends Evaluator>     |  Class<T> type, T instance */      
        evaluators.put(evaluator.getClass(), evaluator);
    }

    /**
     * @param type concrete evaluator class
     * @return result of evaluation for given Evaluator type
     */
    public <T extends Evaluator> T getEvaluator(Class<T> type) {
        return type.cast(evaluators.get(type));
    }

    
    /**
     * Return all evaluators used for evaluation 
     * @return 
     */
    public Map<Class<?>, Evaluator> getEvaluators() {
        return evaluators;
    }
    
    public double getMeanSquareError() {
       return getEvaluator(ErrorEvaluator.class).getResult();        
    }
    

    /**
     * Out of the box method (util) which computes all metrics for given neural network and test data set
     */
    public static void runFullEvaluation(NeuralNetwork<BackPropagation> neuralNet, DataSet dataSet) {

        Evaluation evaluation = new Evaluation();
        evaluation.addEvaluator(new ErrorEvaluator(new MeanSquaredError()));
        evaluation.addEvaluator(ClassificationMetricsEvaluator.createForDataSet(dataSet));

        evaluation.evaluateDataSet(neuralNet, dataSet);

        LOG.info("#################################################");
        LOG.info("Errors: ");
        LOG.info("MeanSquare Error: " + evaluation.getEvaluator(ErrorEvaluator.class).getResult());
        LOG.info("#################################################");
        LOG.info("Metrics: ");
        ClassificationMetrics result = evaluation.getEvaluator(ClassificationMetricsEvaluator.MultiClassEvaluator.class).getResult();
        LOG.info("Accuracy: " + result.getAccuracy());
        LOG.info("Error Rate: " + result.getError());
        LOG.info("Precision: " + result.getPrecision());
        LOG.info("Recall: " + result.getRecall());
        LOG.info("FScore: " + result.getFScore());
        LOG.info("#################################################");
    }

}
