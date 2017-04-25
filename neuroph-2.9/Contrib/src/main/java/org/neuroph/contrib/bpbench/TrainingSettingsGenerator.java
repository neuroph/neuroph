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
 *
 * @author Mladen
 */
public class TrainingSettingsGenerator {

    private double minLearningRate;
    private double maxLearningRate;
    private double learningRateStep;

    private int minHiddenNeurons;
    private int maxHiddenNeurons;
    private int hiddenNeuronsStep;
    
    private double momentum;
    private double maxError;
    private int maxIterations;
    private boolean batchMode;

    private Properties settings;
    
    public TrainingSettingsGenerator() {
        settings = new Properties();
    }
    
    
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

    
    
    public List<TrainingSettings> generateSettings(){
        List<TrainingSettings> list = new ArrayList<>();
        double learningRateStart = minLearningRate;
        int hiddenNeuronStart = minHiddenNeurons;
        for (double i = learningRateStart; i < maxLearningRate; i+=learningRateStep) {
            for (int j = hiddenNeuronStart; j < maxHiddenNeurons; j+=hiddenNeuronsStep) {
                list.add(new TrainingSettings(i, this.momentum, j, this.maxIterations, this.maxError, this.batchMode));
            }
        }
        
        return list;
    }

    public Properties getSettings() {
        return settings;
    }

    public void setSettings(Properties settings) {
        this.settings = settings;
        setUpGenerator();
    }

    private void setUpGenerator() {
        this.batchMode = Boolean.parseBoolean(settings.getProperty(BackpropagationSettings.BATCH_MODE, "false"));
        this.hiddenNeuronsStep = Integer.parseInt(settings.getProperty(BackpropagationSettings.HIDDEN_NEURONS_STEP, "0"));
        this.learningRateStep = Double.parseDouble(settings.getProperty(BackpropagationSettings.LEARNING_RATE_STEP, "0.1"));
        this.maxError = Double.parseDouble(settings.getProperty(BackpropagationSettings.MAX_ERROR, "0.1"));
        this.maxHiddenNeurons = Integer.parseInt(settings.getProperty(BackpropagationSettings.MAX_HIDDEN_NEURONS, "1"));
        this.maxIterations = Integer.parseInt(settings.getProperty(BackpropagationSettings.MAX_ITERATIONS, "10000"));
        this.maxLearningRate = Double.parseDouble(settings.getProperty(BackpropagationSettings.MAX_LEARNING_RATE, "1"));
        this.minHiddenNeurons = Integer.parseInt(settings.getProperty(BackpropagationSettings.MIN_HIDDEN_NEURONS, "0"));
        this.minLearningRate = Double.parseDouble(settings.getProperty(BackpropagationSettings.MIN_LEARNING_RATE, "0.001"));
        this.momentum = Double.parseDouble(settings.getProperty(BackpropagationSettings.MOMENTUM, "0"));
    }

    
}
