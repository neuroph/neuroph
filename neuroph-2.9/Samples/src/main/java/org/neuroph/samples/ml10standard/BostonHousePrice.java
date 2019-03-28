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
package org.neuroph.samples.ml10standard;

import java.util.Arrays;
import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.error.MeanAbsoluteError;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.Adaline;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.neuroph.util.data.norm.Normalizer;

/**
 *
 * @author Nevena Milenkovic
 */
/*
 INTRODUCTION TO THE PROBLEM AND DATA SET INFORMATION:

 1. Data set that will be used in this experiment: Boston House Price Dataset
    The Boston House Price Dataset involves the prediction of a house price in thousands of dollars given details of the house and its neighborhood.
    The original data set that will be used in this experiment can be found at link:
    https://raw.githubusercontent.com/jbrownlee/Datasets/master/housing.data

2. Reference:  U.S Census Service
   Harrison, D. and Rubinfeld, D.L. (1978) Hedonic prices and the demand for clean air. J. Environ. Economics and Management 5, 81–102.
   Belsley D.A., Kuh, E. and Welsch, R.E. (1980) Regression Diagnostics. Identifying Influential Data and Sources of Collinearity. New York: Wiley.

3. Number of instances: 506

4. Number of Attributes: 13 pluss class attributes (all are continuous values)

5. Attribute Information:
 Inputs:
 13 attributes:
 13 continuous features are computed for each house:
 1) CRIM: per capita crime rate by town.
 2) ZN: proportion of residential land zoned for lots over 25,000 sq.ft.
 3) INDUS: proportion of nonretail business acres per town.
 4) CHAS: Charles River dummy variable (= 1 if tract bounds river; 0 otherwise).
 5) NOX: nitric oxides concentration (parts per 10 million).
 6) RM: average number of rooms per dwelling.
 7) AGE: proportion of owner-occupied units built prior to 1940.
 8) DIS: weighted distances to five Boston employment centers.
 9)0 RAD: index of accessibility to radial highways.
 10) TAX: full-value property-tax rate per $10,000.
 11) PTRATIO: pupil-teacher ratio by town.
 12) B: 1000(Bk – 0.63)^2 where Bk is the proportion of blacks by town.
 13) LSTAT: % lower status of the population.

 14) Output: MEDV: Median value of owner-occupied homes in $1000s..

6. Missing Values: None.




 */
public class BostonHousePrice implements LearningEventListener {

    public static void main(String[] args) {
        (new BostonHousePrice()).run();
    }

    public void run() {
        System.out.println("Creating training set...");
        // get path to training set
        String trainingSetFileName = "data_sets/ml10standard/bostonhouse.txt";
        int inputsCount = 13;
        int outputsCount = 1;

        // create training set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, ",");
        Normalizer norm = new MaxNormalizer();
        norm.normalize(dataSet);
        dataSet.shuffle();

        List<DataSet> subSets = dataSet.split(60, 40);
        DataSet trainingSet = subSets.get(0);
        DataSet testSet = subSets.get(1);

        System.out.println("Creating neural network...");
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH, inputsCount, 2, 2, outputsCount);

        neuralNet.setLearningRule(new MomentumBackpropagation());
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.addListener(this);

        System.out.println("Training network...");
        // train the network with training set
        neuralNet.learn(trainingSet);
        System.out.println("Training completed.");
        System.out.println("Testing network...");

        System.out.println("Network performance on the test set");
        evaluate(neuralNet, testSet);

        System.out.println("Saving network");
        // save neural network to file
        neuralNet.save("nn1.nnet");

        System.out.println("Done.");

        System.out.println();
        System.out.println("Network outputs for test set");
        testNeuralNetwork(neuralNet, testSet);
    }

    // Displays inputs, desired output (from dataset) and actual output (calculated by neural network) for every row from data set.
    public void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        System.out.println("Showing inputs, desired output and neural network output for every row in test set.");

        for (DataSetRow testSetRow : testSet.getRows()) {

            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.println("Input: " + Arrays.toString(testSetRow.getInput()));
            System.out.println("Output: " + networkOutput[0]);
            System.out.println("Desired output" + Arrays.toString(networkOutput));

        }
    }

    // Evaluates performance of neural network.
    // Contains calculation of Confusion matrix for classification tasks or Mean Ssquared Error and Mean Absolute Error for regression tasks.
    // Difference in binary and multi class classification are made when adding Evaluator (MultiClass or Binary).
    public void evaluate(NeuralNetwork neuralNet, DataSet dataSet) {

        System.out.println("Calculating performance indicators for neural network.");

        MeanSquaredError mse = new MeanSquaredError();
        MeanAbsoluteError mae = new MeanAbsoluteError();

        for (DataSetRow testSetRow : dataSet.getRows()) {

            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();
            double[] desiredOutput = testSetRow.getDesiredOutput();
            mse.addPatternError(networkOutput, desiredOutput);
            mae.addPatternError(networkOutput, desiredOutput);
        }

        System.out.println("Mean squared error is: " + mse.getTotalError());
        System.out.println("Mean absolute error is: " + mae.getTotalError());
    }

    @Override
    public void handleLearningEvent(LearningEvent event) {
        MomentumBackpropagation bp = (MomentumBackpropagation) event.getSource();
        System.out.println(bp.getCurrentIteration() + ". iteration | Total network error: " + bp.getTotalNetworkError());
    }
}
