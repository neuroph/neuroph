package org.neuroph.contrib.autotrain;

/**
 *
 * @author Milan Brkic - milan.brkic1@yahoo.com
 */
public class TrainingSettings {
    
    private double learningRate;
    private double momentum;
    private int hiddenNeurons;  // todo: add more hidden layers
    private double maxError;
    private int maxIterations;
    
// procenat za trening i test
    private int trainingSet;
    private int testSet;
    
    
    
    public TrainingSettings(double learningRate, double momentum, int hiddenNeurons) {
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.hiddenNeurons = hiddenNeurons;
    }

    TrainingSettings() {
        this.momentum = 0.7;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public TrainingSettings setLearningRate(double learningRate) {
        this.learningRate = learningRate;
        return this;
    }

    public double getMomentum() {
        return momentum;
    }

    public TrainingSettings setMomentum(double momentum) {
        this.momentum = momentum;
        return this;
    }

    public int getHiddenNeurons() {
        return hiddenNeurons;
    }

    public TrainingSettings setHiddenNeurons(int hiddenNeurons) {
        this.hiddenNeurons = hiddenNeurons;
       return this;
     }

    public double getMaxError() {
        return maxError;
    }

    public TrainingSettings setMaxError(double maxError) {
        this.maxError = maxError;
        return this;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public TrainingSettings setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this;
    }

    
    public int getTrainingSet() {
        return trainingSet;
    }

    public TrainingSettings setTrainingSet(int trainingSet) {
        this.trainingSet = trainingSet;
        return this;
    }

    public int getTestSet() {
        return testSet;
    }

    public TrainingSettings setTestSet(int testSet) {
        this.testSet = testSet;
        return this;
    }
    
    
}
