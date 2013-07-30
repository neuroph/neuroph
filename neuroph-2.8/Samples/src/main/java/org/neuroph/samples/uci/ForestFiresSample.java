
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
 *
 * @author Ivana Bajovic
 */

/*
Data Set Information:

In [Cortez and Morais, 2007], the output 'area' was first transformed with a ln(x+1) function. 
Then, several Data Mining methods were applied. After fitting the models, the outputs were 
post-processed with the inverse of the ln(x+1) transform. Four different input setups were 
used. The experiments were conducted using a 10-fold (cross-validation) x 30 runs. Two 
regression metrics were measured: MAD and RMSE. A Gaussian support vector machine (SVM) fed 
with only 4 direct weather conditions (temp, RH, wind and rain) obtained the best MAD value: 
12.71 +- 0.01 (mean and confidence interval within 95% using a t-student distribution). The 
best RMSE was attained by the naive mean predictor. An analysis to the regression error curve 
(REC) shows that the SVM model predicts more examples within a lower admitted error. In effect, 
the SVM model predicts better small fires, which are the majority.


Attribute Information:

1. X - x-axis spatial coordinate within the Montesinho park map: 1 to 9 -- input Variables
2. Y - y-axis spatial coordinate within the Montesinho park map: 2 to 9 -- input Variables
3. month - month of the year: 'jan' to 'dec' -- input Variables
4. day - day of the week: 'mon' to 'sun' -- input Variables
5. FFMC - FFMC index from the FWI system: 18.7 to 96.20 -- input Variables
6. DMC - DMC index from the FWI system: 1.1 to 291.3 -- input Variables
7. DC - DC index from the FWI system: 7.9 to 860.6 -- input Variables
8. ISI - ISI index from the FWI system: 0.0 to 56.10 -- input Variables
9. temp - temperature in Celsius degrees: 2.2 to 33.30 -- input Variables
10. RH - relative humidity in %: 15.0 to 100 -- input Variables
11. wind - wind speed in km/h: 0.40 to 9.40 -- input Variables
12. rain - outside rain in mm/m2 : 0.0 to 6.4 -- input Variables
13. area - the burned area of the forest (in ha): 0.00 to 1090.84 -- Output Variables
(this output variable is very skewed towards 0.0, thus it may make 
sense to model with the logarithm transform).
 
 The original data set that will be used in this experiment can be found at link http://archive.ics.uci.edu/ml/datasets/Forest+Fires
 */
public class ForestFiresSample implements LearningEventListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new ForestFiresSample()).run();
    }

    public void run() {

        System.out.println("Creating training set...");

        String trainingSetFileName = "data_sets/forest_fires_data.txt";
        int inputsCount = 29;
        int outputsCount = 1;

        // create training set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, ",");
       
        
        System.out.println("Creating neural network...");
        // create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 22, outputsCount);
       
        
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
        neuralNet.save("MyNeuralNetForestFires.nnet");

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

    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        System.out.println(bp.getCurrentIteration() + ". iteration | Total network error: " + bp.getTotalNetworkError());
    }
}
