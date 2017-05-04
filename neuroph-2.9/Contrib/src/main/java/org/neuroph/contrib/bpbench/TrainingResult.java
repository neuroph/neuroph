/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import org.neuroph.contrib.eval.classification.ConfusionMatrix;

/**
 * Class that holds results of each individual training
 *
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class TrainingResult {

    /**
     * Number of iterations needed to learn dataset
     */
    private int trainingIterations;
    /**
     * Total error of neural network on end of training
     */
    private double totalError;
    /**
     * Confusion matrix generated in this training
     */
    private ConfusionMatrix confusionMatrix;

    /**
     * Create instance of training results
     */
    public TrainingResult() {
    }

    /**
     * Create instance of training results using parameters
     *
     * @param trainingIterations
     * @param error
     * @param confusionMatrix
     */
    public TrainingResult(int trainingIterations, double error, ConfusionMatrix confusionMatrix) {
        this.trainingIterations = trainingIterations;
        this.totalError = error;
        this.confusionMatrix = confusionMatrix;
        
    }

    /**
     * Return total error of neural network
     *
     * @return total error
     */
    public double getError() {
        return totalError;
    }

    /**
     * Sets given error
     *
     * @param error
     */
    public void setError(double error) {
        this.totalError = error;
    }

    /**
     * Return training iterations
     *
     * @return training iterations
     */
    public int getTrainingIterations() {
        return trainingIterations;
    }

    /**
     * Set training iterations
     *
     * @param trainingIterations
     */
    public void setTrainingIterations(int trainingIterations) {
        this.trainingIterations = trainingIterations;
    }

    /**
     * Return confusion matrix
     *
     * @return confusion matrix
     */
    public ConfusionMatrix getConfusionMatrix() {
        return confusionMatrix;
    }

    /**
     * Sets confusion matrix
     *
     * @param confusionMatrix
     */
    public void setConfusionMatrix(ConfusionMatrix confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }

}
