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
INTRODUCTION TO THE PROBLEM AND DATA SET INFORMATION:
 * This data set was generated to model psychological experimental results. 
 * Each example is classified as having the balance scale tip to the right, tip to the left, or be balanced.
 * The attributes are the left weight, the left distance, the right weight, and the right distance. 
 * The correct way to find the class is the greater of (left-distance * left-weight) and (right-distance * right-weight). If they are equal, it is balanced.

ATTRIBUTE INFORMATION:

Inputs:
1. Left-Weight: 5 (1, 2, 3, 4, 5), nominal values
2. Left-Distance: 5 (1, 2, 3, 4, 5), nominal values 
3. Right-Weight: 5 (1, 2, 3, 4, 5), nominal values 
4. Right-Distance: 5 (1, 2, 3, 4, 5), nominal values

Output:
5. Class Name: 3 (L, B, R), nominal values
 
The original data set that will be used in this experiment can be found at link http://archive.ics.uci.edu/ml/datasets/Balance+Scale 
*/


public class BalanceScaleSample  implements LearningEventListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new BalanceScaleSample()).run();
    }

    public void run() {

        System.out.println("Creating training set...");
        String trainingSetFileName = "data_sets/balance_scale_data.txt";
        int inputsCount = 20;
        int outputsCount = 3;

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
        neuralNet.save("MyNeuralNetBalanceScale.nnet");

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

    @Override
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        System.out.println(bp.getCurrentIteration() + ". iteration | Total network error: " + bp.getTotalNetworkError());
    }

    
}

