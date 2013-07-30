
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
Introduction to the problem:

We will use Neuroph framework for training the neural network that uses Glass Identification data set. Glass Identification data set was generated to help in criminological investigation. At the scene of the crime, the glass left can be used as evidence, but only if it is correctly identified. Each example is classified as building_windows_float_processed, building_windows_non_float_processed, vehicle_windows_float_processed, vehicle_windows_non_float_processed (none in this database), containers, tableware and headlamps.
The attributes are RI: refractive index, Na: Sodium (unit measurement: weight percent in corresponding oxide, as are attributes 4-10), Mg: Magnesium, Al: Aluminum, Si: Silicon, K: Potassium, Ca: Calcium, Ba: Barium, Fe: Iron. 
Main goal of this experiment is to train neural network to classify this 7 types of glass. 
Data set contains 214 instances , 9 numeric attributes and class name. Each instance has one of 7 possible classes.

Data set information:

1. RI: refractive index (numeric value) -- Input Variable
2. Na: Sodium (unit measurement: weight percent in corresponding oxide, as are attributes 4-10) (nominal value) -- Input Variable
3. Mg: Magnesium (numeric value) -- Input Variable 
4. Al: Aluminum (numeric value) -- Input Variable
5. Si: Silicon (numeric value) -- Input Variable
6. K: Potassium (numeric value) -- Input Variable
7. Ca: Calcium (numeric value) -- Input Variable
8. Ba: Barium (numeric value) -- Input Variable
9. Fe: Iron (numeric value) -- Input Variable
10. Class Name: (nominal value) -- Output Variable
    1-building_windows_float_processed
    2-building_windows_non_float_processed
    3-vehicle_windows_float_processed
    4-vehicle_windows_non_float_processed
    5-containers
    6-tableware
    7-headlamps
 
Class Name is nominal value - last 7 digits of data set represent class:
1 0 0 0 0 0 0 represent building_windows_float_processed
0 1 0 0 0 0 0 building_windows_non_float_processed class  
0 0 1 0 0 0 0 vehicle_windows_float_processed class
0 0 0 1 0 0 0 vehicle_windows_non_float_processed (none in this database)
0 0 0 0 1 0 0 containers class
0 0 0 0 0 1 0 tableware class
0 0 0 0 0 0 1 headlamps class 

 */

public class GlassIdentificationSample implements LearningEventListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new GlassIdentificationSample()).run();
    }

    public void run() {

        System.out.println("Creating training set...");

        String trainingSetFileName = "data_sets/glass_identification_data.txt";
        int inputsCount = 9;
        int outputsCount = 7;

        // create training set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, "\t");
        //dataSet.normalize();
        
        System.out.println("Creating neural network...");
        // create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 22, outputsCount);
       
        
        // attach listener to learning rule
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.addListener(this);

        // set learning rate and max error
        learningRule.setLearningRate(0.1);
        learningRule.setMaxError(0.01);

        System.out.println("Training network...");
        // train the network with training set
        neuralNet.learn(dataSet);

        System.out.println("Training completed.");
        System.out.println("Testing network...");

        testNeuralNetwork(neuralNet, dataSet);

        System.out.println("Saving network");
        // save neural network to file
        neuralNet.save("MyNeuralGlassIdentification.nnet");

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
