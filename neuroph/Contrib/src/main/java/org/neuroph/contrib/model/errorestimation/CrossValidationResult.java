package org.neuroph.contrib.model.errorestimation;

import java.util.ArrayList;
import java.util.List;
import org.neuroph.contrib.eval.EvaluationResult;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;

/**
 *
 * @author zoran
 */
public class CrossValidationResult {
    List<EvaluationResult> results;  
    ClassificationMetrics.Stats average; 
    
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
                                
    }       
    
    public ClassificationMetrics.Stats getAverages() {
        return average;
    } 

    @Override
    public String toString() {
        return "CrossValidationResult{" + "results=" + results + ", average=" + average + '}';
    }
    
    
    
    
}
