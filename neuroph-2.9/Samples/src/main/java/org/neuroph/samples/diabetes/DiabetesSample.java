package org.neuroph.samples.diabetes;


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

 *Data set that will be used in this experiment: Pima Indians Diabetes
 The original data set that will be used in this experiment can be found at link: 
http://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.data

 1. Title: Pima Indians Diabetes Database

2. Sources:
   (a) Original owners: National Institute of Diabetes and Digestive and
                        Kidney Diseases
   (b) Donor of database: Vincent Sigillito (vgs@aplcen.apl.jhu.edu)
                          Research Center, RMI Group Leader
                          Applied Physics Laboratory
                          The Johns Hopkins University
                          Johns Hopkins Road
                          Laurel, MD 20707
                          (301) 953-6231
   (c) Date received: 9 May 1990

3. Past Usage:
    1. Smith,~J.~W., Everhart,~J.~E., Dickson,~W.~C., Knowler,~W.~C., \&
       Johannes,~R.~S. (1988). Using the ADAP learning algorithm to forecast
       the onset of diabetes mellitus.  In {\it Proceedings of the Symposium
       on Computer Applications and Medical Care} (pp. 261--265).  IEEE
       Computer Society Press.

       The diagnostic, binary-valued variable investigated is whether the
       patient shows signs of diabetes according to World Health Organization
       criteria (i.e., if the 2 hour post-load plasma glucose was at least 
       200 mg/dl at any survey  examination or if found during routine medical
       care).   The population lives near Phoenix, Arizona, USA.

       Results: Their ADAP algorithm makes a real-valued prediction between
       0 and 1.  This was transformed into a binary decision using a cutoff of 
       0.448.  Using 576 training instances, the sensitivity and specificity
       of their algorithm was 76% on the remaining 192 instances.

4. Relevant Information:
      Several constraints were placed on the selection of these instances from
      a larger database.  In particular, all patients here are females at
      least 21 years old of Pima Indian heritage.  ADAP is an adaptive learning
      routine that generates and executes digital analogs of perceptron-like
      devices.  It is a unique algorithm; see the paper for details.

5. Number of Instances: 768

6. Number of Attributes: 8 plus class 

7. For Each Attribute: (all numeric-valued)
   1. Number of times pregnant
   2. Plasma glucose concentration a 2 hours in an oral glucose tolerance test
   3. Diastolic blood pressure (mm Hg)
   4. Triceps skin fold thickness (mm)
   5. 2-Hour serum insulin (mu U/ml)
   6. Body mass index (weight in kg/(height in m)^2)
   7. Diabetes pedigree function
   8. Age (years)
   9 and 10. class variables: 0 (absence) or 1 (presence)

8. Missing Attribute Values: No

9. Class Distribution: (class value 1,0 is interpreted as "tested positive for
   diabetes")

   Class Value  Number of instances
   negative            500
   positive            268

10. Brief statistical analysis:

    Attribute number:    Mean:   Standard Deviation:
    1.                     3.8     3.4
    2.                   120.9    32.0
    3.                    69.1    19.4
    4.                    20.5    16.0
    5.                    79.8   115.2
    6.                    32.0     7.9
    7.                     0.5     0.3
    8.                    33.2    11.8


 *The original data set description can be found at link:
http://archive.ics.uci.edu/ml/machine-learning-databases/pima-indians-diabetes/pima-indians-diabetes.names

 */
public class DiabetesSample implements LearningEventListener {

    // for evaluating classification result
    int total, correct, incorrect;
    
    // if output is greater then this value it is considered as malign
    float classificationThreshold = 0.5f;



    public void run() {

        System.out.println("Creating training and test set from file...");
        String trainingSetFileName = "data_sets/diabetes.txt";
        int inputsCount = 8;
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
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 20, 10, outputsCount);
        //attach listener to learning rule
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.addListener(this);

        learningRule.setLearningRate(0.6);
        learningRule.setMaxError(0.07);
        learningRule.setMaxIterations(100000);

        System.out.println("Training network...");
        //train the network with training set
        neuralNet.learn(trainingSet);

        System.out.println("Testing network...");
        testNeuralNetwork(neuralNet, testSet);

    }

    public void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        System.out.println("**********************RESULT**********************");
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
        (new DiabetesSample()).run();
    }    
}
