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
 * NOTE: NOT WORKING!
 * @author Ivana Bajovic
 */

/*
 DATA SET INFORMATION:
 * This database contains 5 numeric-valued attributes.
 * Some instances could be placed in either category 0 or 1. 
 * We have followed the authors' suggestion, placing them in each category with equal probability.
 * We have replaced the actual values of the attributes (i.e., hobby has values chess, sports and stamps) with numeric values. 

 ATTRIBUTE INFORMATION:
 -- 1. name: distinct for each instance and represented numerically -- Input Variable
 -- 2. hobby: nominal values ranging between 1 and 3 -- Input Variable
 -- 3. age: nominal values ranging between 1 and 4 -- Input Variable
 -- 4. educational level: nominal values ranging between 1 and 4 -- Input Variable
 -- 5. marital status: nominal values ranging between 1 and 4 -- Input Variable
 -- 6. class: nominal value between 1 and 3 -- Output Variable

 The original data set that will be used in this experiment can be found at link http://archive.ics.uci.edu/ml/datasets/Hayes-Roth 
 */
public class ConceptLearningAndClassificationSample implements LearningEventListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new ConceptLearningAndClassificationSample()).run();
    }

    public void run() {

        System.out.println("Creating training set...");
        String trainingSetFileName = "data_sets/concept_learning_and_classification_data_1.txt";
        int inputsCount = 15;
        int outputsCount = 3;

        // create training set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, ",", false);
       //dataSet.normalize();

        
        System.out.println("Creating neural network...");
        // create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 10, outputsCount);


        // attach listener to learning rule
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.addListener(this);

        // set learning rate and max error
        learningRule.setLearningRate(0.2);
        learningRule.setMomentum(0.7);
        learningRule.setMaxError(0.01);

        System.out.println("Training network...");
        // train the network with training set
        neuralNet.learn(dataSet);

        System.out.println("Training completed.");
        System.out.println("Testing network...");

        testNeuralNetwork(neuralNet, dataSet);

        System.out.println("Saving network");
        // save neural network to file
        neuralNet.save("MyNeuralNetConceptLearning.nnet");

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
