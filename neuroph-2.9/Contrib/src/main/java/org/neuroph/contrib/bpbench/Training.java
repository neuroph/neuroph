/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import java.util.ArrayList;
import org.neuroph.contrib.eval.ClassifierEvaluator;
import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Mladen
 */
public abstract class Training{

    private final NeuralNetwork neuralNet;
    private final DataSet dataset;

    private TrainingStatistics stats;
    private TrainingSettings settings;

    public abstract void testNeuralNet();
    public abstract LearningRule setParameters();

    public ConfusionMatrix createMatrix() {
        Evaluation eval = new Evaluation();
        String[] classLabels = new String[dataset.getOutputSize()];
        for (int i = 0; i < dataset.getOutputSize(); i++) {
            classLabels[i] = dataset.getColumnName(dataset.getInputSize() + i);
        }
        eval.addEvaluator(new ClassifierEvaluator.MultiClass(classLabels));
        return eval.evaluateDataSet(neuralNet, dataset).getConfusionMatrix();
    }

    public Training(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        this.neuralNet = neuralNet;
        this.dataset = dataset;
        this.stats = new TrainingStatistics();
        this.settings = settings;
    }

    public Training(DataSet dataset, TrainingSettings settings) {
        this.dataset = dataset;
        this.settings = settings;
        this.stats = new TrainingStatistics();
        this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, dataset.getInputSize(), settings.getHiddenNeurons(), dataset.getOutputSize());
    }

    public TrainingSettings getSettings() {
        return settings;
    }

    public void setSettings(TrainingSettings settings) {
        this.settings = settings;
    }

    public DataSet getDataset() {
        return dataset;
    }

    public NeuralNetwork getNeuralNet() {
        return neuralNet;
    }

  
    public TrainingStatistics getStats() {
        return stats;
    }

    public void setStats(TrainingStatistics stats) {
        this.stats = stats;
    }

}
