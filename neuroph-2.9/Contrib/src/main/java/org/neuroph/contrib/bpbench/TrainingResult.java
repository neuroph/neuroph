/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import org.neuroph.contrib.eval.classification.ConfusionMatrix;

/**
 *
 * @author Mladen
 */
public class TrainingResult {
    
    private int noOfIterations;
    private double totalError;
    private ConfusionMatrix confusionMatrix;
    
    public TrainingResult() {
    }

    public TrainingResult(int noOfIterations, double error, ConfusionMatrix confusionMatrix) {
        this.noOfIterations = noOfIterations;
        this.totalError = error;
        this.confusionMatrix = confusionMatrix;
    }

    public double getError() {
        return totalError;
    }

    public void setError(double error) {
        this.totalError = error;
    }

    public int getNoOfIterations() {
        return noOfIterations;
    }

    public void setNoOfIterations(int noOfIterations) {
        this.noOfIterations = noOfIterations;
    }

    public ConfusionMatrix getConfusionMatrix() {
        return confusionMatrix;
    }

    public void setConfusionMatrix(ConfusionMatrix confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }
    
    
}
