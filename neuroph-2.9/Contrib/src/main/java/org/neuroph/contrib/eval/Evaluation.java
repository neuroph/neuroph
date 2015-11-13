package org.neuroph.contrib.eval;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.learning.BackPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;

/**
 * Evaluation service used to run different evaluators on trained neural network
 */
public class Evaluation {

    private static Logger LOGGER = LoggerFactory.getLogger("neuroph");

    private Map<Class<?>, Evaluator> evaluators = new HashMap<>();

    public Evaluation() {
          addEvaluator(new ErrorEvaluator(new MeanSquaredError()));
    }

    /**
     * Runs evaluation procedure for given neural network and data set through all evaluatoors
     * Evaluation results are stored in evaluators
     * 
     * @param neuralNetwork trained neural network
     * @param dataSet       test data set used for evaluation
     */
    public EvaluationResult evaluateDataSet(NeuralNetwork neuralNetwork, DataSet dataSet) {
        // first reset all evaluators
        for (Evaluator evaluator : evaluators.values()) { // for now we have only classification metrics and mse
                evaluator.reset();
        }        
        
        for (DataSetRow dataRow : dataSet.getRows()) {      // iterate all dataset rows
             neuralNetwork.setInput(dataRow.getInput());    // apply input to neural network
             neuralNetwork.calculate();                     // and calculate neural network    
            
            // feed actual neural network along with desired output to all evaluators
            for (Evaluator evaluator : evaluators.values()) { // for now we have only kfold and mse
                evaluator.processNetworkResult(neuralNetwork.getOutput(), dataRow.getDesiredOutput());
            }
        }
        
        // we should iterate all evaluators and getresults here- its hardcoded for now
        ConfusionMatrix confusionMatrix = getEvaluator(ClassifierEvaluator.MultiClass.class).getResult();
        double meanSquaredError = getEvaluator(ErrorEvaluator.class).getResult();
                   
        EvaluationResult result = new EvaluationResult();
        result.setDataSet(dataSet);
        result.setConfusionMatrix(confusionMatrix);
        result.setMeanSquareError(meanSquaredError);
        
         // add neural network here too and maybe dataset too?

        return result;                
    }

    /**
    *
    */
    public  void addEvaluator(Evaluator evaluator) { /* <T extends Evaluator>     |  Class<T> type, T instance */      
        if (evaluator == null) throw new IllegalArgumentException("Evaluator cannot be null!");
            
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
        // take onlu output column names here
        evaluation.addEvaluator(new ClassifierEvaluator.MultiClass(dataSet.getColumnNames())); // these two should be added by default

        evaluation.evaluateDataSet(neuralNet, dataSet);
       // use logger here  - see how to make it print out
        // http://saltnlight5.blogspot.com/2013/08/how-to-configure-slf4j-with-different.html
        LOGGER.info("##############################################################################");
//        LOGGER.info("Errors: ");
        LOGGER.info("MeanSquare Error: " + evaluation.getEvaluator(ErrorEvaluator.class).getResult());
        LOGGER.info("##############################################################################");
        ClassifierEvaluator classificationEvaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = classificationEvaluator.getResult();        
        
        LOGGER.info("Confusion Matrix: \r\n"+confusionMatrix.toString());
              
        
        LOGGER.info("##############################################################################");
        LOGGER.info("Classification metrics: ");        
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);      
        for(ClassificationMetrics cm : metrics)
            LOGGER.info(cm.toString());

        LOGGER.info("##############################################################################");
    }

}
