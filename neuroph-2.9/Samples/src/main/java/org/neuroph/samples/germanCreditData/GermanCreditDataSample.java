package org.neuroph.samples.germanCreditData;


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

 *Data set that will be used in this experiment: Statlog (German Credit Data)
 The original data set that will be used in this experiment can be found at link: 
https://archive.ics.uci.edu/ml/machine-learning-databases/statlog/german/german.data-numeric

 1. Title: German Credit data

2. Source Information

Professor Dr. Hans Hofmann  
Institut f"ur Statistik und "Okonometrie  
Universit"at Hamburg  
FB Wirtschaftswissenschaften  
Von-Melle-Park 5    
2000 Hamburg 13 

3. Number of Instances:  1000

Two datasets are provided.  the original dataset, in the form provided
by Prof. Hofmann, contains categorical/symbolic attributes and
is in the file "german.data".   
 
For algorithms that need numerical attributes, Strathclyde University 
produced the file "german.data-numeric".  This file has been edited 
and several indicator variables added to make it suitable for 
algorithms which cannot cope with categorical variables.   Several
attributes that are ordered categorical (such as attribute 17) have
been coded as integer.    This was the form used by StatLog.


6. Number of Attributes german: 20 (7 numerical, 13 categorical)
   Number of Attributes german.numer: 24 (24 numerical)
    **In this sample we used german.numer data set
    25. and 26. class variables: 0 (absence) or 1 (presence)
    1,0 => "good". 
    0,1 => "bad"


7.  Attribute description for german
    
Attribute 1:  (qualitative)
	       Status of existing checking account
               A11 :      ... <    0 DM
	       A12 : 0 <= ... <  200 DM
	       A13 :      ... >= 200 DM /
		     salary assignments for at least 1 year
               A14 : no checking account

Attribute 2:  (numerical)
	      Duration in month

Attribute 3:  (qualitative)
	      Credit history
	      A30 : no credits taken/
		    all credits paid back duly
              A31 : all credits at this bank paid back duly
	      A32 : existing credits paid back duly till now
              A33 : delay in paying off in the past
	      A34 : critical account/
		    other credits existing (not at this bank)

Attribute 4:  (qualitative)
	      Purpose
	      A40 : car (new)
	      A41 : car (used)
	      A42 : furniture/equipment
	      A43 : radio/television
	      A44 : domestic appliances
	      A45 : repairs
	      A46 : education
	      A47 : (vacation - does not exist?)
	      A48 : retraining
	      A49 : business
	      A410 : others

Attribute 5:  (numerical)
	      Credit amount

Attibute 6:  (qualitative)
	      Savings account/bonds
	      A61 :          ... <  100 DM
	      A62 :   100 <= ... <  500 DM
	      A63 :   500 <= ... < 1000 DM
	      A64 :          .. >= 1000 DM
              A65 :   unknown/ no savings account

Attribute 7:  (qualitative)
	      Present employment since
	      A71 : unemployed
	      A72 :       ... < 1 year
	      A73 : 1  <= ... < 4 years  
	      A74 : 4  <= ... < 7 years
	      A75 :       .. >= 7 years

Attribute 8:  (numerical)
	      Installment rate in percentage of disposable income

Attribute 9:  (qualitative)
	      Personal status and sex
	      A91 : male   : divorced/separated
	      A92 : female : divorced/separated/married
              A93 : male   : single
	      A94 : male   : married/widowed
	      A95 : female : single

Attribute 10: (qualitative)
	      Other debtors / guarantors
	      A101 : none
	      A102 : co-applicant
	      A103 : guarantor

Attribute 11: (numerical)
	      Present residence since

Attribute 12: (qualitative)
	      Property
	      A121 : real estate
	      A122 : if not A121 : building society savings agreement/
				   life insurance
              A123 : if not A121/A122 : car or other, not in attribute 6
	      A124 : unknown / no property

Attribute 13: (numerical)
	      Age in years

Attribute 14: (qualitative)
	      Other installment plans 
	      A141 : bank
	      A142 : stores
	      A143 : none

Attribute 15: (qualitative)
	      Housing
	      A151 : rent
	      A152 : own
	      A153 : for free

Attribute 16: (numerical)
              Number of existing credits at this bank

Attribute 17: (qualitative)
	      Job
	      A171 : unemployed/ unskilled  - non-resident
	      A172 : unskilled - resident
	      A173 : skilled employee / official
	      A174 : management/ self-employed/
		     highly qualified employee/ officer

Attribute 18: (numerical)
	      Number of people being liable to provide maintenance for

Attribute 19: (qualitative)
	      Telephone
	      A191 : none
	      A192 : yes, registered under the customers name

Attribute 20: (qualitative)
	      foreign worker
	      A201 : yes
	      A202 : no

 *The original data set description can be found at link:
https://archive.ics.uci.edu/ml/machine-learning-databases/statlog/german/german.doc

 */
public class GermanCreditDataSample implements LearningEventListener {

    //Important for evaluating network result
    public int[] count = new int[3];
    public int[] correct = new int[3];
    int unpredicted = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        (new GermanCreditDataSample()).run();
    }

    public void run() {

        System.out.println("Creating training and test set from file...");
        String trainingSetFileName = "data_sets/german credit data.txt";
        int inputsCount = 24;
        int outputsCount = 2;

        //Create data set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, " ");
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
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 12, 6, outputsCount);

        //attach listener to learning rule
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.addListener(this);

        learningRule.setLearningRate(0.01);
        learningRule.setMaxError(0.001);
        learningRule.setMaxIterations(10000);

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
        System.out.println("Prediction for 'Good credit risk' => (Correct/total): "
                + this.correct[0] + "/" + count[0] + "(" + formatDecimalNumber(percentM) + "%). ");

        double percentB = (double) this.correct[1] * 100.0 / (double) this.count[1];
        System.out.println("Prediction for 'Bad credit risk' => (Correct/total): "
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
