/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.samples;

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.TransferFunctionType;

/**
 * This sample trains Multi Layer Perceptron network using Resilient Propagation
 * learning rule for the XOR problem.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class XorResilientPropagationSample {

    /**
     * Runs this sample
     */
    public static void main(String[] args) {
    	
        // create training set (logical XOR function)
        DataSet trainingSet = new DataSet(2, 1);
        trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));

        // create multi layer perceptron
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 3, 1);
        // set ResilientPropagation learning rule
        myMlPerceptron.setLearningRule(new ResilientPropagation()); 
       
        // learn the training set
        System.out.println("Training neural network...");
        myMlPerceptron.learn(trainingSet);

        int iterations = ((SupervisedLearning)myMlPerceptron.getLearningRule()).getCurrentIteration();        
        System.out.println("Learned in "+iterations+" iterations");
        
        // test perceptron
        System.out.println("Testing trained neural network");
        testNeuralNetwork(myMlPerceptron, trainingSet);

    }

    /**
     * Prints network output for each element from the specified training set.
     * @param neuralNet neural network
     * @param trainingSet training set
     */
    public static void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        for(DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString( testSetRow.getInput() ) );
            System.out.println(" Output: " + Arrays.toString( networkOutput) );
        }
    }

}
