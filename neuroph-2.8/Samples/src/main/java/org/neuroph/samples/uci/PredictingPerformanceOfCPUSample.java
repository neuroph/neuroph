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
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;

/*
 * @author Ivana Bajovic
 */

/*
 INTRODUCTION TO THE PROBLEM AND DATA SET INFORMATION:
 The objective is to train the neural network to predict relative performance of a CPU using some characteristics
 that are used as input, and subsequently comparing that result with existing performance that is published and 
 relative performance that is estimated using linear regression method.
 The original data set that will be used in this experiment can be found at link http://archive.ics.uci.edu/ml/datasets/Computer+Hardware.
 The data set contains 209 instances with the total of 9 attributes.
 
 ATTRIBUTE INFORMATION:
 
 1. (ignored) vendor name: 30 
 (adviser, amdahl,apollo, basf, bti, burroughs, c.r.d, cambex, cdc, dec, 
 dg, formation, four-phase, gould, honeywell, hp, ibm, ipl, magnuson, 
 microdata, nas, ncr, nixdorf, perkin-elmer, prime, siemens, sperry, 
 sratus, wang) 
 2. (input) Model Name: many unique symbols 
 3. (input) MYCT: machine cycle time in nanoseconds (integer) 
 4. (input) MMIN: minimum main memory in kilobytes (integer) 
 5. (input) MMAX: maximum main memory in kilobytes (integer) 
 6. (input) CACH: cache memory in kilobytes (integer) 
 7. (input) CHMIN: minimum channels in units (integer) 
 8. (input) CHMAX: maximum channels in units (integer) 
 9. (input) PRP: published relative performance (integer) 
 10. (output) ERP: estimated relative performance from the original article (integer)

 */
//beskonacna petlja
public class PredictingPerformanceOfCPUSample implements LearningEventListener{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new PredictingPerformanceOfCPUSample()).run();
    }

    public void run() {

        System.out.println("Creating training set...");
        String trainingSetFileName = "data_sets/cpu_data.txt";
        int inputsCount = 7;
        int outputsCount = 1;

        // create training set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, ",", false);
        Normalizer normalizer = new MaxNormalizer();
        normalizer.normalize(dataSet);


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
        neuralNet.save("MyNeuralNetCPU.nnet");

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
