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
 DATA SET INFORMATION:
 * Each record is an example of a hand consisting of five playing cards drawn from a standard deck of 52.
 * Each card is described using two attributes (suit and rank), for a total of 10 predictive attributes. 
 * There is one Class attribute that describes the "Poker Hand".
 * The order of cards is important, which is why there are 480 possible Royal Flush hands as compared to 4.

 ATTRIBUTE INFORMATION:
 1. S1 "Suit of card #1" 
 Ordinal (1-4) representing {Hearts, Spades, Diamonds, Clubs} 

 2. C1 "Rank of card #1" 
 Numerical (1-13) representing (Ace, 2, 3, ... , Queen, King) 

 3. S2 "Suit of card #2" 
 Ordinal (1-4) representing {Hearts, Spades, Diamonds, Clubs} 

 4. C2 "Rank of card #2" 
 Numerical (1-13) representing (Ace, 2, 3, ... , Queen, King) 

 5. S3 "Suit of card #3" 
 Ordinal (1-4) representing {Hearts, Spades, Diamonds, Clubs} 

 6. C3 "Rank of card #3" 
 Numerical (1-13) representing (Ace, 2, 3, ... , Queen, King) 

 7. S4 "Suit of card #4" 
 Ordinal (1-4) representing {Hearts, Spades, Diamonds, Clubs} 

 8. C4 "Rank of card #4" 
 Numerical (1-13) representing (Ace, 2, 3, ... , Queen, King) 

 9. S5 "Suit of card #5" 
 Ordinal (1-4) representing {Hearts, Spades, Diamonds, Clubs} 

 10. C5 "Rank of card 5" 
 Numerical (1-13) representing (Ace, 2, 3, ... , Queen, King) 

 11. CLASS "Poker Hand" 
 Ordinal (0-9) :
 0: Nothing in hand; not a recognized poker hand 
 1: One pair; one pair of equal ranks within five cards 
 2: Two pairs; two pairs of equal ranks within five cards 
 3: Three of a kind; three equal ranks within five cards 
 4: Straight; five cards, sequentially ranked with no gaps 
 5: Flush; five cards with the same suit 
 6: Full house; pair + different rank three of a kind 
 7: Four of a kind; four equal ranks within five cards 
 8: Straight flush; straight + flush 
 9: Royal flush; {Ace, King, Queen, Jack, Ten} + flush 
 
 1.,2.,3.,4.,5.,6.,7.,8.,9.,10. - inputs
 11. - output

 The original data set that will be used in this experiment can be found at link http://archive.ics.uci.edu/ml/datasets/Poker+Hand
 */
public class PredictingPokerHandsSample implements LearningEventListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new PredictingPokerHandsSample()).run();
    }

    public void run() {

        System.out.println("Creating training set...");
        
        String trainingSetFileName = "data_sets/predicting_poker_hands_data.txt";
        int inputsCount = 85;
        int outputsCount = 9;

        // create training set from file
        DataSet dataSet = DataSet.createFromFile(trainingSetFileName, inputsCount, outputsCount, "\t", false);

        System.out.println("Creating neural network...");
        // create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(inputsCount, 65, outputsCount);


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
        neuralNet.save("MyNeuralNetPokerHands.nnet");

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
