/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import java.io.IOException;
import java.util.Properties;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Mladen
 */
public class BackpropBenchmarksExample {
      public static void main(String[] args) throws IOException {
       
        BackPropBenchmarks bpb = new BackPropBenchmarks();
        bpb.setNoOfRepetitions(3);
        DataSet trainingSet = DataSet.createFromFile("iris_data_normalised.txt", 4, 3, ",");
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 4, 7, 3);
        
        TrainingSettingsGenerator generator = new TrainingSettingsGenerator();
        Properties prop = new Properties();
     
        prop.setProperty(BackpropagationSettings.MIN_LEARNING_RATE, "0.1");
        prop.setProperty(BackpropagationSettings.MAX_LEARNING_RATE, "1.0");
        prop.setProperty(BackpropagationSettings.LEARNING_RATE_STEP, "0.3");
        prop.setProperty(BackpropagationSettings.MIN_HIDDEN_NEURONS, "5");
        prop.setProperty(BackpropagationSettings.MAX_HIDDEN_NEURONS, "10");
        prop.setProperty(BackpropagationSettings.HIDDEN_NEURONS_STEP, "1");
        prop.setProperty(BackpropagationSettings.MOMENTUM, "0");
        prop.setProperty(BackpropagationSettings.MAX_ERROR, "0.1");
        prop.setProperty(BackpropagationSettings.MAX_ITERATIONS, "1000");
        prop.setProperty(BackpropagationSettings.BATCH_MODE, "true");
        generator.setSettings(prop);
        
        for (TrainingSettings settings : generator.generateSettings()) {
            
            Training t = new BackpropagationTraining(trainingSet, settings);
            Training t2 = new MomentumTraining(mlp,trainingSet, settings);
            bpb.addTraining(t);
            bpb.addTraining(t2);
           
        }
        
         bpb.run();

    }
}
