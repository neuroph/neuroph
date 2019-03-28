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
 * The lenses data set tries to predict whether a person will need soft contact lenses, hard contact lenses or no contacts, by determining relevant attributes of the client.
 * The data set has 4 attributes (age of the patient, spectacle prescription, notion on astigmatism, and information on tear production rate) plus an associated three-valued class, that gives the appropriate lens prescription for patient (hard contact lenses, soft contact lenses, no lenses). 
 * The data set contains 24 instances(class distribution: hard contact lenses- 4, soft contact lenses-5, no contact lenses- 15). 

 ATTRIBUTE INFORMATION:
 1. Age of the patient: 
 Nominal variable: (1) young, (2) pre-presbyopic, (3) presbyopic
 2. Spectacle prescription: 
 Nominal variable: (1) myope, (2) hypermetrope
 3. Astigmatic:
 Nominal variable: (1) no, (2) yes
 4. Tear production rate:
 Nominal variable: (1) reduced, (2) normal
 5. Class name: 
 Nominal variable: (1) the patient should be fitted with hard contact lenses, (2) the patient should be fitted with soft contact lenses, (3) the patient should not be fitted with contact lenses.
 
 1.,2.,3.,4. - inputs
 5. - output
 
 The original data set that will be used in this experiment can be found at link http://archive.ics.uci.edu/ml/datasets/Lenses 
 */
public class LensesClassificationSample implements LearningEventListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new LensesClassificationSample()).run();
    }

    public void run() {

        System.out.println("Creating training set...");
        
        String trainingSetFileName = "data_sets/lenses_data.txt";
        int inputsCount = 9;
        int outputsCount = 3;

        System.out.println("Creating training set...");
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, " ", false);


        System.out.println("Creating neural network...");
        // create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 16, outputsCount);


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
        neuralNet.save("MyNeuralNetLenses.nnet");

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
