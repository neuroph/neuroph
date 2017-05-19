/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

/**
 * Class with constants for all settings used by learning algorithms
 * 
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class BackpropagationSettings {
    /**
     * Minimum learning rate 
     */
    public static final String MIN_LEARNING_RATE = "minLearningRate";
    
    /**
     * Maximum learning rate
     */
    
    public static final String MAX_LEARNING_RATE = "maxLearningRate";
    
    /**
     * Learning rate step 
     */
    
    public static final String LEARNING_RATE_STEP = "learningRateStep";
    
    /**
     * Minimum hidden neurons
     */
    
    public static final String MIN_HIDDEN_NEURONS = "minHiddenNeurons";
    /**
     * Maximum hidden neurons
     */
    
    public static final String MAX_HIDDEN_NEURONS = "maxHiddenNeurons";
    
    /**
     * Hidden neurons step
     */
    
    public static final String HIDDEN_NEURONS_STEP = "hiddenNeuronsStep";
    
    /**
     * Momentum used for momentum backpropagation algorithm  
     */
    
    public static final String MOMENTUM = "momentum";
    
    /**
     * Max error 
     */
    
    public static final String MAX_ERROR = "maxError";
    
    /**
     * Max iterations
     */
    
    public static final String MAX_ITERATIONS = "maxIterations";
   
    /**
     * Batch mode
     */
    
    public static final String BATCH_MODE = "batchMode";
    
    /**
     * Decrease factor for resilient propagation
     */
    public static final String DECREASE_FACTOR = "decreaseFactor";
    
    /**
     * Increase factor for resilient propagation
     */
    public static final String INCREASE_FACTOR = "increaseFactor";
    /**
     * Initial delta for resilient propagation
     */
    public static final String INITIAL_DELTA = "initialDelta";
    
    /**
     * Maximum delta for resilient propagation
     */
    public static final String MAX_DELTA = "maxDelta";
    /**
     * Minimum delta for resilient propagation
     */
    public static final String MIN_DELTA = "minDelta"; 
}
