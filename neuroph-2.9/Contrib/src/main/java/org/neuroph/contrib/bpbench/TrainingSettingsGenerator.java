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
public class TrainingSettingsGenerator {

    double minLearningRate;
    double maxLearningRate;
    double learningRateStep;

    int minHiddenNeurons;
    int maxHiddenNeurons;
    int hiddenNeuronsStep;
    
    double momentum;
    double maxError;
    int maxIterations;
    boolean batchMode;
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

}
