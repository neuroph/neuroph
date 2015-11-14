package org.neuroph.samples.brestCancer;

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
import java.math.BigDecimal;
import java.math.RoundingMode;
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

/**
 *
 * @author Ivan Petrovic
 */
/*
 INTRODUCTION TO THE PROBLEM AND DATA SET INFORMATION:

 *Data set that will be used in this experiment: Wisconsin Diagnostic Breast Cancer (WDBC)
 The original data set that will be used in this experiment can be found at link: 
 https://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wdbc.data

 *Creators: 
 -   r. William H. Wolberg, General Surgery Dept., University of Wisconsin,  Clinical Sciences Center, 
 Madison, WI 53792 wolberg@eagle.surgery.wisc.edu
    
 -   W. Nick Street, Computer Sciences Dept., University of Wisconsin, 1210 West Dayton St., 
 Madison, WI 53706 treet@cs.wisc.edu  608-262-6619

 -   Olvi L. Mangasarian, Computer Sciences Dept., University of Wisconsin, 1210 West Dayton St., 
 Madison, WI 53706 olvi@cs.wisc.edu 

 *See also: 
 -   http://www.cs.wisc.edu/~olvi/uwmp/mpml.html
 -   http://www.cs.wisc.edu/~olvi/uwmp/cancer.html

 *Result: 
 -   predicting field 2, diagnosis: B = benign, M = malignant 
 -   sets are linearly separable using all 30 input features

 *Relevant information: 
 Features are computed from a digitized image of a fine needle aspirate (FNA) of a breast mass. 
 They describe characteristics of the cell nuclei present in the image. Separating plane described above 
 was obtained using Multisurface Method-Tree (MSM-T), a classification method which uses linear
 programming to construct a decision tree. Relevant features were selected using an exhaustive search 
 in the space of 1-4	features and 1-3 separating planes.

 *Number of instances: 569

 *Number of attributes: 
 32 (30 real-valued input features, 
 2 output features - 1,0 for M = malignant cancer and 0,1 for B = benign cancer)

 *Missing attribute values: none

 *Class distribution: 357 benign, 212 malignant

 ATTRIBUTE INFORMATION:

 Inputs:
 1-30 attributes: 
 Ten real-valued features are computed for each cell nucleus:
 a) radius (mean of distances from center to points on the perimeter)
 b) texture (standard deviation of gray-scale values)
 c) perimeter
 d) area
 e) smoothness (local variation in radius lengths)
 f) compactness (perimeter^2 / area - 1.0)
 g) concavity (severity of concave portions of the contour)
 h) concave points (number of concave portions of the contour)
 i) symmetry 
 j) fractal dimension ("coastline approximation" - 1)

 Output:
 31 and 32: 
 1,0 M = malignant
 0,1 B = benign

 *The original data set description can be found at link:
 https://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wdbc.names

 */
public class BrestCancerSample implements LearningEventListener {

    //Important for evaluating network result
    public int[] count = new int[3];
    public int[] correct = new int[3];
    int unpredicted = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        (new BrestCancerSample()).run();
    }

    public void run() {

        System.out.println("Creating training and test set from file...");
        String trainingSetFileName = "data_sets/breast cancer.txt";
        int inputsCount = 30;
        int outputsCount = 2;

        //Create data set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, ",");
        dataSet.shuffle();

        //Normalizing data set
        Normalizer normalizer = new MaxNormalizer();
        normalizer.normalize(dataSet);

        //Creatinig training set (70%) and test set (30%)
        DataSet[] trainingAndTestSet = dataSet.createTrainingAndTestSubsets(70, 30);
        DataSet trainingSet = trainingAndTestSet[0];
        DataSet testSet = trainingAndTestSet[1];

        System.out.println("Creating neural network...");
        //Create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 16, outputsCount);

        //attach listener to learning rule
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.addListener(this);

        learningRule.setLearningRate(0.3);
        learningRule.setMaxError(0.001);
        learningRule.setMaxIterations(5000);

        System.out.println("Training network...");
        //train the network with training set
        neuralNet.learn(trainingSet);

        System.out.println("Testing network...\n\n");
        testNeuralNetwork(neuralNet, testSet);

        System.out.println("Done.");

        System.out.println("**************************************************");

    }

    public void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        System.out.println("**************************************************");
        System.out.println("**********************RESULT**********************");
        System.out.println("**************************************************");
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

        System.out.println("Total cases: " + this.count[2] + ". ");
        System.out.println("Correctly predicted cases: " + this.correct[2] + ". ");
        System.out.println("Incorrectly predicted cases: " + (this.count[2] - this.correct[2] - unpredicted) + ". ");
        System.out.println("Unrecognized cases: " + unpredicted + ". ");
        double percentTotal = (double) this.correct[2] * 100 / (double) this.count[2];
        System.out.println("Predicted correctly: " + formatDecimalNumber(percentTotal) + "%. ");

        double percentM = (double) this.correct[0] * 100.0 / (double) this.count[0];
        System.out.println("Prediction for 'M (malignant)' => (Correct/total): "
                + this.correct[0] + "/" + count[0] + "(" + formatDecimalNumber(percentM) + "%). ");

        double percentB = (double) this.correct[1] * 100.0 / (double) this.count[1];
        System.out.println("Prediction for 'B (benign)' => (Correct/total): "
                + this.correct[1] + "/" + count[1] + "(" + formatDecimalNumber(percentB) + "%). ");
    }

    @Override
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        if (event.getEventType().equals(LearningEvent.Type.LEARNING_STOPPED)) {
            double error = bp.getTotalNetworkError();
            System.out.println("Training completed in " + bp.getCurrentIteration() + " iterations, ");
            System.out.println("With total error: " + formatDecimalNumber(error));
        } else {
            System.out.println("Iteration: " + bp.getCurrentIteration() + " | Network error: " + bp.getTotalNetworkError());
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
    public void keepScore(int prediction, int ideal) {
        count[ideal]++;
        count[2]++;

        if (prediction == ideal) {
            correct[ideal]++;
            correct[2]++;
        }
        if (prediction == -1) {
            unpredicted++;
        }
    }

    //Formating decimal number to have 3 decimal places
    public String formatDecimalNumber(double number) {
        return new BigDecimal(number).setScale(4, RoundingMode.HALF_UP).toString();
    }
}
