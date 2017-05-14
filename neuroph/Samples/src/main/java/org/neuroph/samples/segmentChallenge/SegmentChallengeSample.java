package org.neuroph.samples.segmentChallenge;

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
 The original training set that will be used in this experiment can be found at link: 
 https://archive.ics.uci.edu/ml/machine-learning-databases/image/segmentation.data

 The original test set that will be used in this experiment can be found at link: 
 https://archive.ics.uci.edu/ml/machine-learning-databases/image/segmentation.test

 1. Title: Image Segmentation data

 2. Source Information
 -- Creators: Vision Group, University of Massachusetts
 -- Donor: Vision Group (Carla Brodley, brodley@cs.umass.edu)
 -- Date: November, 1990
 
 3. Past Usage: None yet published

 4. Relevant Information:

 The instances were drawn randomly from a database of 7 outdoor 
 images.  The images were handsegmented to create a classification
 for every pixel.  

 Each instance is a 3x3 region.

 5. Number of Instances: Training data: 210  Test data: 2100

 6. Number of Attributes: 19 continuous attributes

 7. Attribute Information:

 1.  region-centroid-col:  the column of the center pixel of the region.
 2.  region-centroid-row:  the row of the center pixel of the region.
 3.  region-pixel-count:  the number of pixels in a region = 9.
 4.  short-line-density-5:  the results of a line extractoin algorithm that 
 counts how many lines of length 5 (any orientation) with
 low contrast, less than or equal to 5, go through the region.
 5.  short-line-density-2:  same as short-line-density-5 but counts lines
 of high contrast, greater than 5.
 6.  vedge-mean:  measure the contrast of horizontally
 adjacent pixels in the region.  There are 6, the mean and 
 standard deviation are given.  This attribute is used as
 a vertical edge detector.
 7.  vegde-sd:  (see 6)
 8.  hedge-mean:  measures the contrast of vertically adjacent
 pixels. Used for horizontal line detection. 
 9.  hedge-sd: (see 8).
 10. intensity-mean:  the average over the region of (R + G + B)/3
 11. rawred-mean: the average over the region of the R value.
 12. rawblue-mean: the average over the region of the B value.
 13. rawgreen-mean: the average over the region of the G value.
 14. exred-mean: measure the excess red:  (2R - (G + B))
 15. exblue-mean: measure the excess blue:  (2B - (G + R))
 16. exgreen-mean: measure the excess green:  (2G - (R + B))
 17. value-mean:  3-d nonlinear transformation
 of RGB. (Algorithm can be found in Foley and VanDam, Fundamentals
 of Interactive Computer Graphics)
 18. saturatoin-mean:  (see 17)
 19. hue-mean:  (see 17)

 8. Missing Attribute Values: None

 9. Class Distribution: 

 Classes:   1,0,0,0,0,0,0 -- brickface
            0,1,0,0,0,0,0 -- sky
            0,0,1,0,0,0,0 -- foliage
            0,0,0,1,0,0,0 -- cement
            0,0,0,0,1,0,0 -- window
            0,0,0,0,0,1,0 -- path
            0,0,0,0,0,0,1 -- grass
        
 30 instances per class for training data.
 300 instances per class for test data.

 *The original data set description can be found at link:
 https://archive.ics.uci.edu/ml/machine-learning-databases/image/segmentation.names

 */
public class SegmentChallengeSample implements LearningEventListener {

    //Important for evaluating network result
    public int[] count = new int[8];
    public int[] correct = new int[8];
    int unpredicted = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        (new SegmentChallengeSample()).run();
    }

    public void run() {

        System.out.println("Creating training and test set from file...");
        String trainingSetFileName = "data_sets/segment challenge.txt";
        String testSetFileName = "data_sets/segment test.txt";
        int inputsCount = 19;
        int outputsCount = 7;

        //Create training data set from file
        DataSet trainingSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, ",");
        System.out.println("Training set size: " + trainingSet.getRows().size());
        trainingSet.shuffle();
        trainingSet.shuffle();

        //Normalizing training data set
        Normalizer normalizer = new MaxNormalizer();
        normalizer.normalize(trainingSet);

        //Create test data set from file
        DataSet testSet = DataSet.createFromFile(testSetFileName, inputsCount, outputsCount, ",");
        System.out.println("Test set size: " + testSet.getRows().size());
        System.out.println("--------------------------------------------------");
        testSet.shuffle();
        testSet.shuffle();

        //Normalizing training data set
        normalizer.normalize(testSet);

        System.out.println("Creating neural network...");
        //Create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 17, 10, outputsCount);
        //attach listener to learning rule
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.addListener(this);

        learningRule.setLearningRate(0.01);
        learningRule.setMaxError(0.001);
        learningRule.setMaxIterations(12000);

        System.out.println("Training network...");
        //train the network with training set
        neuralNet.learn(trainingSet);

        System.out.println("Testing network...\n\n");
        testNeuralNetwork(neuralNet, testSet);

        System.out.println("Done.");
        System.out.println("**************************************************");
//        }
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

        System.out.println("Total cases: " + this.count[7] + ". ");
        System.out.println("Correctly predicted cases: " + this.correct[7] + ". ");
        System.out.println("Incorrectly predicted cases: " + (this.count[7] - this.correct[7] - unpredicted) + ". ");
        System.out.println("Unrecognized cases: " + unpredicted + ". ");
        double percentTotal = (double) this.correct[7] * 100 / (double) this.count[7];
        System.out.println("Predicted correctly: " + formatDecimalNumber(percentTotal) + "%. ");

        for (int i = 0; i < correct.length - 1; i++) {
            double p = (double) this.correct[i] * 100.0 / (double) this.count[i];
            System.out.println("Segment class: " + getClasificationClass(i + 1) + " - Correct/total: "
                    + this.correct[i] + "/" + count[i] + "(" + formatDecimalNumber(p) + "%). ");
        }

        this.count = new int[8];
        this.correct = new int[8];
        unpredicted = 0;
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
        count[7]++;

        if (prediction == ideal) {
            correct[ideal]++;
            correct[7]++;
        }
        if (prediction == -1) {
            unpredicted++;
        }
    }

    //Formating decimal number to have 3 decimal places
    public String formatDecimalNumber(double number) {
        return new BigDecimal(number).setScale(4, RoundingMode.HALF_UP).toString();
    }

    public String getClasificationClass(int i) {
        switch (i) {
            case 1:
                return "brickface";
            case 2:
                return "sky";
            case 3:
                return "foliage";
            case 4:
                return "cement";
            case 5:
                return "window";
            case 6:
                return "path";
            case 7:
                return "grass";
            default:
                return "error";
        }
    }
}
