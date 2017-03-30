/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Mladen
 */
public class TrainingStatistics {
    //Iterations statistics
    private int minIterations = Integer.MAX_VALUE;
    private int maxIterations = Integer.MIN_VALUE;
    private double meanIterations;
    private double stdIterations;
    //Error statistics
    private double minError = Integer.MAX_VALUE;
    private double maxError = Integer.MIN_VALUE;
    private double meanError;
    private double stdError;
    
    private List<TrainingResult> trainingResults = new ArrayList<>();
    
    
    
    public void addData(TrainingResult result){
        if(result.getNoOfIterations() < minIterations) minIterations = result.getNoOfIterations();
        if(result.getNoOfIterations() > maxIterations) maxIterations = result.getNoOfIterations();
        if(result.getError()< minError) minError = result.getError();
        if(result.getError() > maxError) maxError = result.getError();
        trainingResults.add(result);
       
    }
    public void calculateParameters(){
        calculateMean();
        calculateStd();
    }
    private void calculateMean(){
        int sumIter = 0;
        double sumError = 0;
        for (int i = 0; i < trainingResults.size(); i++) {
            sumError += trainingResults.get(i).getError();
            sumIter += trainingResults.get(i).getNoOfIterations();
        }
        meanError = sumError / trainingResults.size();
        meanIterations = sumIter / trainingResults.size();
    }
    private void calculateStd(){
        if(meanError == 0 || meanIterations == 0) calculateMean();
        double sumError = 0;
        int sumIter = 0;
        for (TrainingResult trainingResult : trainingResults) {
            sumError += Math.pow(trainingResult.getError()-meanError, 2);
            sumIter += Math.pow(trainingResult.getNoOfIterations()-meanIterations, 2);
        }
        stdError = Math.sqrt(sumError/trainingResults.size());
        stdIterations = Math.sqrt(sumIter/trainingResults.size());
    }
    
    public TrainingStatistics() {
        minIterations = Integer.MAX_VALUE;
        maxIterations = Integer.MIN_VALUE;
    }


    public int getMinIterations() {
        return minIterations;
    }

    public void setMinIterations(int minIterations) {
        this.minIterations = minIterations;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public List<TrainingResult> getTrainingResults() {
        return trainingResults;
    }

    public void setTrainingResults(List<TrainingResult> noOfIterations) {
        this.trainingResults = noOfIterations;
    }

    public double getMeanIterations() {
        return meanIterations;
    }

    public void setMeanIterations(double meanIterations) {
        this.meanIterations = meanIterations;
    }

    public double getStdIterations() {
        return stdIterations;
    }

    public void setStdIterations(double stdIterations) {
        this.stdIterations = stdIterations;
    }

    @Override
    public String toString() {
        return "TrainingStatistics{" + "minIterations=" + minIterations + ", maxIterations=" + maxIterations + ", meanIterations=" + meanIterations + ", stdIterations=" + stdIterations + ", minError=" + minError + ", maxError=" + maxError + ", meanError=" + meanError + ", stdError=" + stdError + ", trainingList=" + trainingResults + '}';
    }

  

 
    
}
