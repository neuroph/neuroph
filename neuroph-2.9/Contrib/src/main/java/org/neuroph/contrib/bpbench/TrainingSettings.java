/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

/**
 * Holds settings for individual training
 *
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class TrainingSettings {

    /**
     * Learning rate of the training
     */
    private double learningRate;
    /**
     * Momentum of the training
     */
    private double momentum;
    /**
     * Number of neurons in hidden layer
     */
    private int hiddenNeurons;
    /**
     * Maximum allowed iterations of the training
     */
    private int maxIterations;
    /**
     * Maximum allowed error of the training
     */
    private double maxError;
    /**
     * Batch mode of the training
     */
    private boolean batchMode;
    /**
     * Decrease factor
     */
    private double decreaseFactor;
    /**
     * Increase factor
     */
    private double increaseFactor;
    /**
     * Initial delta
     */
    private double initialDelta;
    /**
     * Maximum delta
     */
    private double maxDelta;
    /**
     * Minimum delta
     */
    private double minDelta;

    /**
     * Create instance of training settings with given parameters
     *
     * @param learningRate
     * @param momentum
     * @param hiddenNeurons
     * @param maxIterations
     * @param maxError
     * @param batchMode
     */
    public TrainingSettings(double learningRate, double momentum, int hiddenNeurons, int maxIterations, double maxError, boolean batchMode) {
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.hiddenNeurons = hiddenNeurons;
        this.maxIterations = maxIterations;
        this.maxError = maxError;
        this.batchMode = batchMode;
    }

    /**
     * Return batch mode state
     *
     * @return batch mode
     */
    public boolean isBatchMode() {
        return batchMode;
    }

    /**
     * Set batch mode for this training
     *
     * @param batchMode
     */
    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    /**
     * Return maximum number of iterations for this training
     *
     * @return maximum iterations
     */
    public int getMaxIterations() {
        return maxIterations;
    }

    /**
     * Set maximum number of iterations for this training
     *
     * @param maxIterations
     */
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    /**
     * Return maximum total error for this training
     *
     * @return maximum error
     */
    public double getMaxError() {
        return maxError;
    }

    /**
     * Set maximum total error for this training
     *
     * @param maxError
     */
    public void setMaxError(double maxError) {
        this.maxError = maxError;
    }

    /**
     * Return learning rate for this training
     *
     * @return learning rate
     */
    public double getLearningRate() {
        return learningRate;
    }

    /**
     * Set learning rate for this training
     *
     * @param learningRate
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Return momentum for this training
     *
     * @return momentum
     */
    public double getMomentum() {
        return momentum;
    }

    /**
     * Set momentum for this training
     *
     * @param momentum
     */
    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    /**
     * Return number of neurons in hidden layer for this training
     *
     * @return
     */
    public int getHiddenNeurons() {
        return hiddenNeurons;
    }

    /**
     * Set number of neurons in hidden layer for this training
     *
     * @param hiddenNeurons
     */
    public void setHiddenNeurons(int hiddenNeurons) {
        this.hiddenNeurons = hiddenNeurons;
    }

    /**
     * Return decrease factor for resilient propagation
     *
     * @return decrease factor
     */
    public double getDecreaseFactor() {
        return decreaseFactor;
    }

    /**
     * Set decrease factor for resilient propagation
     *
     * @param decreaseFactor
     */
    public void setDecreaseFactor(double decreaseFactor) {
        this.decreaseFactor = decreaseFactor;
    }

    /**
     * Return increase factor for resilient propagation
     *
     * @return increase factor
     */
    public double getIncreaseFactor() {
        return increaseFactor;
    }

    /**
     * Set increase factor for resilient propagation
     *
     * @param increaseFactor
     */
    public void setIncreaseFactor(double increaseFactor) {
        this.increaseFactor = increaseFactor;
    }

    /**
     * Return initial delta for resilient propagation
     *
     * @return initial delta
     */
    public double getInitialDelta() {
        return initialDelta;
    }

    /**
     * Set initial delta for resilient propagation
     *
     * @param initialDelta
     */
    public void setInitialDelta(double initialDelta) {
        this.initialDelta = initialDelta;
    }

    /**
     * Return maximum delta for resilient propagation
     *
     * @return maximum delta
     */
    public double getMaxDelta() {
        return maxDelta;
    }

    /**
     * Set maximum delta for resilient propagation
     *
     * @param maxDelta
     */
    public void setMaxDelta(double maxDelta) {
        this.maxDelta = maxDelta;
    }

    /**
     * Return minimum delta for resilient propagation
     *
     * @return minimum delta
     */
    public double getMinDelta() {
        return minDelta;
    }

    /**
     * Set minimum delta for resilient propagation
     *
     * @param minDelta
     */
    public void setMinDelta(double minDelta) {
        this.minDelta = minDelta;
    }

}
