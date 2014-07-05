package org.neuroph.training;

import java.util.ArrayList;

/**
 * http://www.mathsisfun.com/data/standard-deviation.html
 * @author zoran
 */
public class StatsTask extends Task {

    Stats iterationStats, totalErrorStats;
    double resultCount = 0;
    
    // min, max, mean, variance, deviation
    
    public StatsTask(String name) {
        super(name);
        iterationStats = new Stats("Training Iterations");
        totalErrorStats = new Stats("Total Error");
    }

    @Override
    public void execute() {
        ArrayList<TrainingResult> trainingResults = (ArrayList<TrainingResult>)getParentProcess().getVar("trainingResults");
 
        resultCount = trainingResults.size();        
        iterationStats.count = resultCount;
        totalErrorStats.count = resultCount;
        double iterationsSum = 0, totalErrorSum = 0;
        
        for(TrainingResult result : trainingResults) {
           iterationsSum += result.getIterations();
           totalErrorSum += result.getTotalError();
           
           // find min iterations
           if (result.getIterations() < iterationStats.min) {
               iterationStats.min = result.getIterations();
           }
           
           // find max iterations
           if (result.getIterations() > iterationStats.max) {
               iterationStats.max = result.getIterations();
           }           
           
           // find min error
           if (result.getTotalError() <  totalErrorStats.min) {
               totalErrorStats.min = result.getTotalError();
           }
           
           // find max error
           if (result.getTotalError() > totalErrorStats.max) {
               totalErrorStats.max = result.getTotalError();
           }                      
           
                      
        }        
        
        // calculate mean
        
        iterationStats.mean = iterationsSum / resultCount;
        totalErrorStats.mean = totalErrorSum / resultCount;
        
        
        // calculate variance - The average of the squared differences from the mean.
        double iterationSquaredDiffSum = 0;
        double totalErrorSquaredDiffSum = 0;
        for(TrainingResult result : trainingResults) {
            iterationSquaredDiffSum += (iterationStats.mean - result.getIterations() ) * (iterationStats.mean - result.getIterations() );            
            totalErrorSquaredDiffSum += (totalErrorStats.mean - result.getTotalError()) * (totalErrorStats.mean - result.getTotalError() );                        
        }
        
        iterationStats.variance = iterationSquaredDiffSum / resultCount;
        totalErrorStats.variance = totalErrorSquaredDiffSum / resultCount;                
           
        
        // calculate standard deviation as squared root of variance
        
        iterationStats.standardDeviation = Math.sqrt(iterationStats.variance);
        totalErrorStats.standardDeviation = Math.sqrt(totalErrorStats.variance);
                
        getParentProcess().setVar("iterationStats", iterationStats);
        getParentProcess().setVar("totalErrorStats", totalErrorStats);                
    }
        
}
