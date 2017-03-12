package org.neuroph.samples.ionosphere;


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
http://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.data

 1. Title: Johns Hopkins University Ionosphere database

2. Source Information:
   -- Donor: Vince Sigillito (vgs@aplcen.apl.jhu.edu)
   -- Date: 1989
   -- Source: Space Physics Group
              Applied Physics Laboratory
              Johns Hopkins University
              Johns Hopkins Road
              Laurel, MD 20723 

3. Past Usage:
   -- Sigillito, V. G., Wing, S. P., Hutton, L. V., \& Baker, K. B. (1989).
      Classification of radar returns from the ionosphere using neural 
      networks. Johns Hopkins APL Technical Digest, 10, 262-266.

      They investigated using backprop and the perceptron training algorithm
      on this database.  Using the first 200 instances for training, which
      were carefully split almost 50% positive and 50% negative, they found
      that a "linear" perceptron attained 90.7%, a "non-linear" perceptron
      attained 92%, and backprop an average of over 96% accuracy on the 
      remaining 150 test instances, consisting of 123 "good" and only 24 "bad"
      instances.  (There was a counting error or some mistake somewhere; there
      are a total of 351 rather than 350 instances in this domain.) Accuracy
      on "good" instances was much higher than for "bad" instances.  Backprop
      was tested with several different numbers of hidden units (in [0,15])
      and incremental results were also reported (corresponding to how well
      the different variants of backprop did after a periodic number of 
      epochs).

      David Aha (aha@ics.uci.edu) briefly investigated this database.
      He found that nearest neighbor attains an accuracy of 92.1%, that
      Ross Quinlan's C4 algorithm attains 94.0% (no windowing), and that
      IB3 (Aha \& Kibler, IJCAI-1989) attained 96.7% (parameter settings:
      70% and 80% for acceptance and dropping respectively).

4. Relevant Information:
   This radar data was collected by a system in Goose Bay, Labrador.  This
   system consists of a phased array of 16 high-frequency antennas with a
   total transmitted power on the order of 6.4 kilowatts.  See the paper
   for more details.  The targets were free electrons in the ionosphere.
   "Good" radar returns are those showing evidence of some type of structure 
   in the ionosphere.  "Bad" returns are those that do not; their signals pass
   through the ionosphere.  

   Received signals were processed using an autocorrelation function whose
   arguments are the time of a pulse and the pulse number.  There were 17
   pulse numbers for the Goose Bay system.  Instances in this databse are
   described by 2 attributes per pulse number, corresponding to the complex
   values returned by the function resulting from the complex electromagnetic
   signal.

5. Number of Instances: 351

6. Number of Attributes: 34 plus the class attribute
   -- All 34 predictor attributes are continuous

7. Attribute Information:     
   -- All 34 are continuous, as described above
   -- The 35th and 36th class variables: 0 (absence) or 1 (presence)
    1,0 => "good"
    0,1 => "bad"

8. Missing Values: None

 *The original data set description can be found at link:
http://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.names

 */
public class IonosphereSample implements LearningEventListener {

    //Important for evaluating network result
    public int[] count = new int[3];
    public int[] correct = new int[3];
    int unpredicted = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        (new IonosphereSample()).run();
    }

    public void run() {

        System.out.println("Creating training and test set from file...");
        String trainingSetFileName = "data_sets/ionosphere.txt";
        int inputsCount = 34;
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
//        for (int i = 0; i < 21; i++) {
        System.out.println("Creating neural network...");
        //Create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 16, 8, outputsCount);
//            System.out.println("HIDDEN COUNT: " + i);
        //attach listener to learning rule
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.addListener(this);

        learningRule.setLearningRate(0.2);
        learningRule.setMaxError(0.001);
        learningRule.setMaxIterations(10000);

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

        System.out.println("Total cases: " + this.count[2] + ". ");
        System.out.println("Correctly predicted cases: " + this.correct[2] + ". ");
        System.out.println("Incorrectly predicted cases: " + (this.count[2] - this.correct[2] - unpredicted) + ". ");
        System.out.println("Unrecognized cases: " + unpredicted + ". ");
        double percentTotal = (double) this.correct[2] * 100 / (double) this.count[2];
        System.out.println("Predicted correctly: " + formatDecimalNumber(percentTotal) + "%. ");

        double percentM = (double) this.correct[0] * 100.0 / (double) this.count[0];
        System.out.println("Prediction for 'Good' => (Correct/total): "
                + this.correct[0] + "/" + count[0] + "(" + formatDecimalNumber(percentM) + "%). ");

        double percentB = (double) this.correct[1] * 100.0 / (double) this.count[1];
        System.out.println("Prediction for 'Bad' => (Correct/total): "
                + this.correct[1] + "/" + count[1] + "(" + formatDecimalNumber(percentB) + "%). ");
        this.count = new int[3];
        this.correct = new int[3];
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
