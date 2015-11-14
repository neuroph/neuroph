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
package org.neuroph.samples.forestCover;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;

public class Evaluate {

    private Config config;

    int[] count = new int[8];
    int[] correct = new int[8];
    int unpredicted = 0;

    public Evaluate(Config config) {
        this.config = config;
    }

    public void evaluate() {
        System.out.println("Evaluating neural network...");
        //Loading neural network from file
        MultiLayerPerceptron neuralNet = (MultiLayerPerceptron) NeuralNetwork.createFromFile(config.getTrainedNetworkFileName());

        //Load normalized balanced data set from file
        DataSet dataSet = DataSet.load(config.getTestFileName());

        //Testing neural network
        testNeuralNetwork(neuralNet, dataSet);

    }

    //Testing neural network
    public void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        for (DataSetRow testSetRow : testSet.getRows()) {

            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();

            //Finding network output
            double[] networkOutput = neuralNet.getOutput();
            int predicted = maxOutput(networkOutput);

            //Finding actual output
            double[] networkDesiredOutput = testSetRow.getDesiredOutput();
            int ideal = maxOutput(networkDesiredOutput);

            //Colecting data for network evaluation
            keepScore(predicted, ideal);
        }

        System.out.println("Total cases: " + this.count[7] + ". ");
        System.out.println("Correct cases: " + this.correct[7] + ". ");
        System.out.println("Incorrectly predicted cases: " + (this.count[7] - this.correct[7] - unpredicted) + ". ");
        System.out.println("Unrecognized cases: " + unpredicted + ". ");

        double percentTotal = (double) this.correct[7] * 100 / (double) this.count[7];
        System.out.println("Predicted correctly: " + formatDecimalNumber(percentTotal) + "%. ");

        for (int i = 0; i < correct.length - 1; i++) {
            double p = (double) this.correct[i] * 100.0 / (double) this.count[i];
            System.out.println("Tree type: " + (i + 1) + " - Correct/total: "
                    + this.correct[i] + "/" + count[i] + "(" + formatDecimalNumber(p) + "%). ");
        }
    }

    //Metod determines the maximum output. Maximum output is network prediction for one row. 
    public static int maxOutput(double[] array) {
        double max = array[0];
        int index = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                index = i;
                max = array[i];
            }
        }
        //If maximum is less than 0.5, that prediction will not count.
        if (max < 0.5) {
            return -1;
        }
        return index;
    }

    //Colecting data to evaluate network.
    public void keepScore(int actual, int ideal) {
        count[ideal]++;
        count[7]++;

        if (actual == ideal) {
            correct[ideal]++;
            correct[7]++;
        }
        if (actual == -1) {
            unpredicted++;
        }
    }

    //Formating decimal number to have 3 decimal places
    private String formatDecimalNumber(double number) {
        return new BigDecimal(number).setScale(2, RoundingMode.HALF_UP).toString();
    }

}
