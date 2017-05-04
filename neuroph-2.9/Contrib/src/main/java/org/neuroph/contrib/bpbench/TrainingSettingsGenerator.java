/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class used for generating specific training settings using specter of values
 * for different parameters
 *
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class TrainingSettingsGenerator {

    /**
     * Minimum learning rate for generator
     */
    private double minLearningRate;
    /**
     * Maximum learning rate for generator
     */
    private double maxLearningRate;
    /**
     * Learning rate step for generator
     */
    private double learningRateStep;
    /**
     * Minimum number of neurons in hidden layer
     */
    private int minHiddenNeurons;
    /**
     * Maximum number of neurons in hidden layer
     */
    private int maxHiddenNeurons;
    /**
     * Step of increment of neurons in hidden layer
     */
    private int hiddenNeuronsStep;
    /**
     * Momentum for generator
     */
    private double momentum;
    /**
     * Maximum total error for generator
     */
    private double maxError;
    /**
     * Maximum number of iterations for generator
     */
    private int maxIterations;
    /**
     * Batch mode for generator
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
     * Settings for generator
     */
    private Properties settings;

    /**
     * Create instance of generator with new settings
     */
    public TrainingSettingsGenerator() {
        settings = new Properties();
    }

    /**
     * Create instance of generator with given parameters
     *
     * @param minLearningRate
     * @param maxLearningRate
     * @param learningRateStep
     * @param minHiddenNeurons
     * @param maxHiddenNeurons
     * @param hiddenNeuronsStep
     * @param momentum
     * @param maxError
     * @param maxIterations
     * @param batchMode
     */
    public TrainingSettingsGenerator(double minLearningRate, double maxLearningRate, double learningRateStep, int minHiddenNeurons, int maxHiddenNeurons, int hiddenNeuronsStep, double momentum, double maxError, int maxIterations, boolean batchMode) {
        if (minLearningRate > 0) {
            this.minLearningRate = minLearningRate;
        } else {
            this.minLearningRate = 0.1;
        }
        if (maxLearningRate > 0 && maxLearningRate > minLearningRate) {
            this.maxLearningRate = maxLearningRate;
        } else {
            this.maxLearningRate = 1;
        }
        if (learningRateStep > 0 && learningRateStep < 1) {
            this.learningRateStep = learningRateStep;
        } else {
            this.learningRateStep = 0.1;
        }
        if (minHiddenNeurons > 0) {
            this.minHiddenNeurons = minHiddenNeurons;
        } else {
            this.minHiddenNeurons = 1;
        }
        if (maxHiddenNeurons > 0 && maxHiddenNeurons > minHiddenNeurons) {
            this.maxHiddenNeurons = maxHiddenNeurons;
        } else {
            this.maxHiddenNeurons = 10;
        }
        if (hiddenNeuronsStep > 0) {
            this.hiddenNeuronsStep = hiddenNeuronsStep;
        } else {
            this.hiddenNeuronsStep = 1;
        }

        this.momentum = momentum;
        this.maxError = maxError;
        this.maxIterations = maxIterations;
        this.batchMode = batchMode;
    }

    /**
     * Method used for generating list of settings for training
     *
     * @return list of settings
     */
    public List<TrainingSettings> generateSettings() {
        List<TrainingSettings> list = new ArrayList<>();
        double learningRateStart = minLearningRate;
        int hiddenNeuronStart = minHiddenNeurons;
        for (double i = learningRateStart; i < maxLearningRate; i += learningRateStep) {
            for (int j = hiddenNeuronStart; j < maxHiddenNeurons; j += hiddenNeuronsStep) {
                list.add(new TrainingSettings(i, this.momentum, j, this.maxIterations, this.maxError, this.batchMode));
            }
        }

        return list;
    }

    /**
     * Return settings for generator
     *
     * @return
     */
    public Properties getSettings() {
        return settings;
    }

    /**
     * Set settings for generator
     *
     * @param settings
     */
    public void setSettings(Properties settings) {
        this.settings = settings;
        setUpGenerator();
    }

    /**
     * Set up generator using given settings or set default value
     */
    private void setUpGenerator() {
        this.batchMode = Boolean.parseBoolean(settings.getProperty(BackpropagationSettings.BATCH_MODE, "false"));
        this.hiddenNeuronsStep = Integer.parseInt(settings.getProperty(BackpropagationSettings.HIDDEN_NEURONS_STEP, "1"));
        this.learningRateStep = Double.parseDouble(settings.getProperty(BackpropagationSettings.LEARNING_RATE_STEP, "0.1"));
        this.maxError = Double.parseDouble(settings.getProperty(BackpropagationSettings.MAX_ERROR, "0.03"));
        this.maxHiddenNeurons = Integer.parseInt(settings.getProperty(BackpropagationSettings.MAX_HIDDEN_NEURONS, "1"));
        this.maxIterations = Integer.parseInt(settings.getProperty(BackpropagationSettings.MAX_ITERATIONS, "10000"));
        this.maxLearningRate = Double.parseDouble(settings.getProperty(BackpropagationSettings.MAX_LEARNING_RATE, "1"));
        this.minHiddenNeurons = Integer.parseInt(settings.getProperty(BackpropagationSettings.MIN_HIDDEN_NEURONS, "0"));
        this.minLearningRate = Double.parseDouble(settings.getProperty(BackpropagationSettings.MIN_LEARNING_RATE, "0.001"));
        this.momentum = Double.parseDouble(settings.getProperty(BackpropagationSettings.MOMENTUM, "0.3"));
        this.decreaseFactor = Double.parseDouble(settings.getProperty(BackpropagationSettings.DECREASE_FACTOR, "0.5"));
        this.increaseFactor = Double.parseDouble(settings.getProperty(BackpropagationSettings.INCREASE_FACTOR, "1.2"));
        this.initialDelta = Double.parseDouble(settings.getProperty(BackpropagationSettings.INITIAL_DELTA, "0.1"));
        this.maxDelta = Double.parseDouble(settings.getProperty(BackpropagationSettings.MAX_DELTA, "1.0"));
        this.minDelta = Double.parseDouble(settings.getProperty(BackpropagationSettings.MIN_DELTA, "1e-6"));
    }

}
