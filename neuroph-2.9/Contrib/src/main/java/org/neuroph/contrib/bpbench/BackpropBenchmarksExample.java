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
        //Stavi konstante u posebnu klasu
        prop.setProperty("minLearningRate", "0.1");
        prop.setProperty("maxLearningRate", "1.0");
        prop.setProperty("learningRateStep", "0.3");
        prop.setProperty("minHiddenNeurons", "5");
        prop.setProperty("maxHiddenNeurons", "10");
        prop.setProperty("hiddenNeuronsStep", "1");
        prop.setProperty("momentum", "0");
        prop.setProperty("maxError", "0.1");
        prop.setProperty("maxIterations", "1000");
        prop.setProperty("batchMode", "true");
        generator.setSettings(prop);
        
        for (TrainingSettings settings : generator.generateSettings()) {
            //drugi konstruktor bez NN
            Training t = new BackpropagationTraining(trainingSet, settings);
            Training t2 = new MomentumTraining(null,trainingSet, settings);
            bpb.addTraining(t);
            bpb.addTraining(t2);
           
        }
        
         bpb.run();

    }
}
