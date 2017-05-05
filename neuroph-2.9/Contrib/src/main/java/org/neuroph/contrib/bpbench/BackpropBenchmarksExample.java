/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

/**
 * Example of backpropagation benchmark
 *
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class BackpropBenchmarksExample {

    public static void main(String[] args) throws IOException {

        BackPropBenchmarks bpb = new BackPropBenchmarks();
        bpb.setNoOfRepetitions(3);
        
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 4, 7, 3);
        
        DataSet trainingSet = DataSet.createFromFile("iris_data_normalised.txt", 4, 3, ",");

        TrainingSettingsGenerator generator = new TrainingSettingsGenerator();
      
        Properties prop = new Properties();
        prop.setProperty(BackpropagationSettings.MIN_LEARNING_RATE, "0.1");
        prop.setProperty(BackpropagationSettings.MAX_LEARNING_RATE, "0.4");
        prop.setProperty(BackpropagationSettings.LEARNING_RATE_STEP, "0.5");
        prop.setProperty(BackpropagationSettings.MIN_HIDDEN_NEURONS, "9");
        prop.setProperty(BackpropagationSettings.MAX_HIDDEN_NEURONS, "10");
        prop.setProperty(BackpropagationSettings.HIDDEN_NEURONS_STEP, "1");
        prop.setProperty(BackpropagationSettings.MOMENTUM, "0.5");
        prop.setProperty(BackpropagationSettings.MAX_ERROR, "0.1");
        prop.setProperty(BackpropagationSettings.MAX_ITERATIONS, "10000");
        prop.setProperty(BackpropagationSettings.BATCH_MODE, "true");
      
        generator.setSettings(prop);

        List<TrainingSettings> settingsCollection = generator.generateSettings();
        List<Class<? extends AbstractTraining>> trainingTypeCollection = new ArrayList<>();
        trainingTypeCollection.add(BackpropagationTraining.class);
        trainingTypeCollection.add(MomentumTraining.class);
        
        bpb.startBenchmark(trainingTypeCollection, settingsCollection, trainingSet, mlp);
        bpb.saveResults("C:\\Users\\Mladen\\Desktop\\test123");
      
    }
}
