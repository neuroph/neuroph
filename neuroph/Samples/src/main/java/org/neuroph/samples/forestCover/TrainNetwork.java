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
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;

public class TrainNetwork implements LearningEventListener {

    private Config config;

    public TrainNetwork(Config config) {
        this.config = config;
    }

    //Creating and saving neural network to file
    public void createNeuralNetwork() {
        System.out.println("Creating neural network... ");
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(config.getInputCount(), config.getFirstHiddenLayerCount(), config.getSecondHiddenLayerCount(), config.getOutputCount());
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.setLearningRate(0.01);
        learningRule.setMaxError(0.1);
        learningRule.setMaxIterations(1000);
        System.out.println("Saving neural network to file... ");
        neuralNet.save(config.getTrainedNetworkFileName());
        System.out.println("Neural network successfully saved!");
    }

    //Training neural network with normalized balanced training data set
    public void train() {
        System.out.println("Training neural network... ");
        MultiLayerPerceptron neuralNet = (MultiLayerPerceptron) NeuralNetwork.createFromFile(config.getTrainedNetworkFileName());

        DataSet dataSet = DataSet.load(config.getNormalizedBalancedFileName());
        neuralNet.getLearningRule().addListener(this);
        neuralNet.learn(dataSet);
        System.out.println("Saving trained neural network to file... ");
        neuralNet.save(config.getTrainedNetworkFileName());
        System.out.println("Neural network successfully saved!");
    }

    //Formating decimal number to have 3 decimal places
    private String formatDecimalNumber(double number) {
        return new BigDecimal(number).setScale(5, RoundingMode.HALF_UP).toString();
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

}
