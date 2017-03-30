/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

/**
 *
 * @author Mladen
 */
public class TrainingSettings {

    private double learningRate;
    private double momentum;
    private int hiddenNeurons;
    private int maxIterations;
    private double maxError;
    private boolean batchMode;
    
    public TrainingSettings(double learningRate, double momentum, int hiddenNeurons, int maxIterations, double maxError, boolean batchMode) {
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.hiddenNeurons = hiddenNeurons;
        this.maxIterations = maxIterations;
        this.maxError = maxError;
        this.batchMode = batchMode;
    }

    public boolean isBatchMode() {
        return batchMode;
    }

    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public double getMaxError() {
        return maxError;
    }

    public void setMaxError(double maxError) {
        this.maxError = maxError;
    }

  
    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public int getHiddenNeurons() {
        return hiddenNeurons;
    }

    public void setHiddenNeurons(int hiddenNeurons) {
        this.hiddenNeurons = hiddenNeurons;
    }

}
