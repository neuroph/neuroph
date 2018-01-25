package org.neuroph.samples.breastCancer;

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
import java.util.List;
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
 31 (30 real-valued input features, 
 1 output feature - 1,0 for M = malignant / benign cancer)

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
 31: 1/0 M = malignant / benign

 *The original data set description can be found at link:
 https://archive.ics.uci.edu/ml/machine-learning-databases/breast-cancer-wisconsin/wdbc.names

 */
public class BreastCancerSample implements LearningEventListener {

    // for evaluating classification result
    int total, correct, incorrect;
    
    // if output is greater then this value it is considered as malign
    float classificationThreshold = 0.5f;

    public void run() {

        System.out.println("Creating training and test set from file...");
        String trainingSetFileName = "data_sets/breast_cancer.txt";
        int inputsCount = 30;
        int outputsCount = 1;

        //Create data set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, ",");
        dataSet.shuffle();

        //Normalizing data set
        Normalizer normalizer = new MaxNormalizer();
        normalizer.normalize(dataSet);

        //Creatinig training set (70%) and test set (30%)
        List<DataSet> trainingAndTestSet = dataSet.split(70, 30);
        DataSet trainingSet = trainingAndTestSet.get(0);
        DataSet testSet = trainingAndTestSet.get(1);

        System.out.println("Creating neural network...");
        //Create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 16, outputsCount);

        //attach listener to learning rule
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.addListener(this);

        learningRule.setLearningRate(0.3);
        learningRule.setMaxError(0.01);
        learningRule.setMaxIterations(500);

        System.out.println("Training network...");
        //train the network with training set
        neuralNet.learn(trainingSet);

        System.out.println("Testing network...");
        testNeuralNetwork(neuralNet, testSet);

    }

    public void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {
        System.out.println("********************** TEST RESULT **********************");
        for (DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();

            // get network output
            double[] networkOutput = neuralNet.getOutput();
            int predicted = interpretOutput(networkOutput);

            // get target/desired output
            double[] desiredOutput = testSetRow.getDesiredOutput();
            int target = (int)desiredOutput[0];

            // count predictions
            countPredictions(predicted, target);
        }

        System.out.println("Total cases: " + total + ". ");
        System.out.println("Correctly predicted cases: " + correct);
        System.out.println("Incorrectly predicted cases: " + incorrect);
        double percentTotal = (correct / (double)total) * 100;
        System.out.println("Predicted correctly: " + formatDecimalNumber(percentTotal) + "%. ");
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

    public int interpretOutput(double[] array) {
        if (array[0] >= classificationThreshold) {
            return 1;
        }else {
            return 0;
        }
    }

    public void countPredictions(int prediction, int target) {        
        if (prediction == target) {
            correct++;
        } else {
            incorrect++;
        }
        total++;
    }

    //Formating decimal number to have 3 decimal places
    public String formatDecimalNumber(double number) {
        return new BigDecimal(number).setScale(4, RoundingMode.HALF_UP).toString();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new BreastCancerSample()).run();
    }    
    
}
