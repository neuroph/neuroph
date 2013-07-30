
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
 DATA SET INFORMATION:
Given are the variable name, variable type, the measurement unit and a brief description. The concrete compressive strength is the regression problem. The order of this listing corresponds to the order of numerals along the rows of the database. 

Name -- Data Type -- Measurement -- Description 

Cement (component 1) -- quantitative -- kg in a m3 mixture -- Input Variable 
Blast Furnace Slag (component 2) -- quantitative -- kg in a m3 mixture -- Input Variable 
Fly Ash (component 3) -- quantitative -- kg in a m3 mixture -- Input Variable 
Water (component 4) -- quantitative -- kg in a m3 mixture -- Input Variable 
Superplasticizer (component 5) -- quantitative -- kg in a m3 mixture -- Input Variable 
Coarse Aggregate (component 6) -- quantitative -- kg in a m3 mixture -- Input Variable 
Fine Aggregate (component 7)	 -- quantitative -- kg in a m3 mixture -- Input Variable 
Age -- quantitative -- Day (1~365) -- Input Variable 
Concrete compressive strength -- quantitative -- MPa -- Output Variable 
 
 The original data set that will be used in this experiment can be found at link http://archive.ics.uci.edu/ml/datasets/Concrete+Compressive+Strength
 */

public class ConcreteStrenghtTestSample implements LearningEventListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new ConcreteStrenghtTestSample()).run();
    }

    public void run() {

        System.out.println("Creating training set...");
        String trainingSetFileName = "data_sets/concrete_strenght_test_data.txt";
        int inputsCount = 8;
        int outputsCount = 1;

        // create training set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, ",");
       
        
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
        neuralNet.save("MyNeuralConcreteStrenght.nnet");

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
