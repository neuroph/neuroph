package org.neuroph.eval;

import java.util.ArrayList;
import java.util.List;
import org.neuroph.eval.classification.ClassificationMetrics;

/**
 *
 * @author zoran
 */
public class CrossValidationResult {
    List<EvaluationResult> results;  
    ClassificationMetrics.Stats average; 
    int numberOfFolds;
    int numberOfInstances;
    
    public CrossValidationResult() {        
        results = new ArrayList<>();
    }
    
    public void addEvaluationResult(EvaluationResult result) {
        results.add(result);
    }
    
    // add statistics here? 
    
    // calculate avg, max, min, variation, std,  for 
     
    // should we also calculate stats for each class???           
         
    /**
     * Calculate average over all classes and datasets
     */
    public void calculateStatistics() {
        
        average = new ClassificationMetrics.Stats();
       // ClassificationMetrics.Stats max = new ClassificationMetrics.Stats();

        double count = 0;

        List<String> classLabels = new ArrayList<>();
        // avg max min variation std
        for (EvaluationResult er : results) {
            ClassificationMetrics[] ccm = er.getClassificationMetricses();
            for (ClassificationMetrics cm : ccm) {
                average.accuracy += cm.getAccuracy();
                average.precision += cm.getPrecision();
                average.recall += cm.getRecall();
                average.fScore += cm.getFMeasure();
                average.mserror += er.getMeanSquareError();
                average.correlationCoefficient += cm.getMatthewsCorrelationCoefficient();

                if(!classLabels.contains(cm.getClassLabel()))
                    classLabels.add(cm.getClassLabel());
            }
            count++;
        }
        count = count * classLabels.size(); // * classes count
        average.accuracy = average.accuracy / count;
        average.precision = average.precision / count;
        average.recall = average.recall / count;
        average.fScore = average.fScore / count;
        average.mserror = average.mserror / count;
        average.correlationCoefficient = average.correlationCoefficient / count;
                                
    }       
    
    public ClassificationMetrics.Stats getAverages() {
        return average;
    } 

    @Override
    public String toString() {
        return "CrossValidationResult{" + "results=" + results + ", average=" + average + '}';
    }
    
    public void printResult() {
        System.out.println("=== Cross validation result ===");
        System.out.println("Instances: " + numberOfInstances);
        System.out.println("Number of folds: " + numberOfFolds);
        System.out.println("\n");
        System.out.println("=== Summary ===");
        System.out.println("Mean squared error: " + average.mserror);
        System.out.println("Accuracy: " + average.accuracy);
        System.out.println("Precision: " + average.precision);
        System.out.println("Recall: " +average.recall);
        System.out.println("FScore: " + average.fScore);
        System.out.println("Correlation coefficient: " + average.correlationCoefficient);        
    }   
    
    
    
    
}
