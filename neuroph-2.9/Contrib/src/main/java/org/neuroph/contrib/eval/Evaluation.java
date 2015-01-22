package org.neuroph.contrib.eval;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.learning.BackPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import org.neuroph.contrib.eval.classification.ClassificationMeasure;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;

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
        for (DataSetRow dataRow : dataSet.getRows()) {      // iterate all dataset rows
             neuralNetwork.setInput(dataRow.getInput());    // apply input to neural network
             neuralNetwork.calculate();                     // and calculate neural network    
            
            // feed actual neural network along with desired output to all evaluators
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
       // use logger here  - see how to make it print out
        // http://saltnlight5.blogspot.com/2013/08/how-to-configure-slf4j-with-different.html
        System.out.println("##############################################################################");
//        System.out.println("Errors: ");
        System.out.println("MeanSquare Error: " + evaluation.getEvaluator(ErrorEvaluator.class).getResult());
        System.out.println("##############################################################################");
        System.out.println("Confusion Matrix: ");
        
        ClassificationMetricsEvaluator evaluator = evaluation.getEvaluator(ClassificationMetricsEvaluator.MultiClassEvaluator.class);
        ConfusionMatrix confusionMatrix = evaluator.getConfusionMatrix();
        System.out.println(confusionMatrix.toString());
        
        System.out.println("##############################################################################");
        System.out.println("Classification metrics: ");        
        ClassificationMetrics[] metrics = evaluator.getResult();
       
//        ClassificationMetrics[] measures = ClassificationMetrics.createFromMatrix(confusionMatrix);
        for(ClassificationMetrics cm : metrics)
            System.out.println(cm.toString());
        
//        System.out.println("Accuracy: " + result.getAccuracy());
//        System.out.println("Error Rate: " + result.getError());
//        System.out.println("Precision: " + result.getPrecision());
//        System.out.println("Recall: " + result.getRecall());
//        System.out.println("FScore: " + result.getFScore());
        System.out.println("##############################################################################");
    }

}
