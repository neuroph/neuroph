/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.util.random;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;

/**
 * This class provides NguyenWidrow randmization technique, which gives very good results
 * for Multi Layer Perceptrons trained with back propagation family of learning rules.
 * Based on NguyenWidrowRandomizer from Encog
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class NguyenWidrowRandomizer extends RangeRandomizer {

    public NguyenWidrowRandomizer(double min, double max) {
        super(min, max);
    }

    @Override
    public void randomize(NeuralNetwork neuralNetwork) {
        super.randomize(neuralNetwork);

        int inputNeuronsCount = neuralNetwork.getInputNeurons().length;
        int hiddenNeuronsCount = 0;

        for (int i = 1; i < neuralNetwork.getLayersCount() - 1; i++) {
            hiddenNeuronsCount += neuralNetwork.getLayerAt(i).getNeuronsCount();
        }

        double beta = 0.7 * Math.pow(hiddenNeuronsCount, 1.0 / inputNeuronsCount); // should we use the total number of hidden neurons or different norm for each layer


        for (Layer layer : neuralNetwork.getLayers()) {
            // Calculate the Euclidean Norm for the weights: norm += value * value - suma vadrata tezina u layeru
            double norm = 0.0;
            for (Neuron neuron : layer.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    double weight = connection.getWeight().getValue();
                    norm += weight * weight;
                }
            }
            norm = Math.sqrt(norm);

            // Rescale the weights using beta and the norm: beta * value / norm            
            for (Neuron neuron : layer.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    double weight = connection.getWeight().getValue();
                    weight = beta * weight / norm;
                    connection.getWeight().setValue(weight);
                }
            }
        }

    }
}