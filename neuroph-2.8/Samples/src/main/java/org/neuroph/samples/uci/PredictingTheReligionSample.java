
/**
 * Copyright 2013 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.neuroph.samples.uci;

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;

/**
 *
 * @author Ivana Bajovic
 */

/*
INTRODUCTION TO THE PROBLEM:

The objective of this problem is to create and train neural network to predict the religion of European countries, given some attributes as input. First we need data set. The data that we use in this experiment can be found at Europe. Data that are collected refere to 49 European countries. Each country has 26 input attributes and 1 output attribute.

INPUT ATTRIBUTES ARE:

Part of Europe where the country is
Area that the country coveres (in thousands of square km)
Population (in round millions)
Language
Number of vertical bars in the flag
Number of horizontal stripes in the flag
Number of different colours in the flag
If red present in the flag
If green present in the flag
If blue present in the flag
If gold present in the flag
If yellow present in the flag
If white present in the flag
If black present in the flag
If orange present in the flag
Predominant colour in the flag (tie-breaks decided by taking the topmost hue, if that fails then the most central hue, and if that fails the leftmost hue)
Number of circles in the flag
Number of (upright) crosses
Number of diagonal crosses
Number of sun or star symbols
If a crescent moon symbol present
If any triangles present
If an animate image (e.g., an eagle, a tree, a human hand) present
If any letters or writing on the flag (e.g., a motto or slogan)
Witch colour is in the top-left corner (moving right to decide tie-breaks)
Witch colour is in the bottom-right corner (moving left to decide tie-breaks)

OUTPUT ATTRIBUTES ARE:

Religions of each coutnry
 
 *******  The original data set that will be used in this experiment can be found at link http://neuroph.sourceforge.net/tutorials/Flags/original.txt
 */
public class PredictingTheReligionSample implements LearningEventListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new PredictingTheReligionSample()).run();
    }

    public void run() {

        System.out.println("Creating training set...");
        // get path to training set
        String trainingSetFileName = "data_sets/religion_data.txt";
        int inputsCount = 54;
        int outputsCount = 5;

        // create training set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, "\t");
       
        
        System.out.println("Creating neural network...");
        // create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 22, outputsCount);
       
        
        // attach listener to learning rule
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.addListener(this);

        // set learning rate and max error
        learningRule.setLearningRate(0.2);
        learningRule.setMaxError(0.01);

        System.out.println("Training network...");
        // train the network with training set
        neuralNet.learn(dataSet);

        System.out.println("Training completed.");
        System.out.println("Testing network...");

        testNeuralNetwork(neuralNet, dataSet);

        System.out.println("Saving network");
        // save neural network to file
        neuralNet.save("MyNeuralNetReligion.nnet");

        System.out.println("Done.");
    }

    public void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        for (DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString(testSetRow.getInput()));
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }
    }

    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        System.out.println(bp.getCurrentIteration() + ". iteration | Total network error: " + bp.getTotalNetworkError());
    }
}
