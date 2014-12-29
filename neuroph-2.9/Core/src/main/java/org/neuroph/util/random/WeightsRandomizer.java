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

import java.util.Random;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.comp.layer.Layer2D;

/**
 * Basic weights randomizer, iterates and randomizes all connection weights in network.
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class WeightsRandomizer {

    /**
     * Random number genarator used by randomizers
     */
    protected Random randomGenerator;

    /**
     * Create a new instance of WeightsRandomizer
     */
    public WeightsRandomizer() {
        this.randomGenerator = new Random();
    }

    /**
     * Create a new instance of WeightsRandomizer with specified random generator
     * If you use the same random generators, you'll get the same random sequences
     *
     * @param randomGenerator random geneartor to use for randomizing weights
     */
    public WeightsRandomizer(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    /**
     * Gets random generator used to generate random values
     *
     * @return random generator used to generate random values
     */
    public Random getRandomGenerator() {
        return randomGenerator;
    }

    public void setRandomGenerator(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }


    /**
     * Iterates and randomizes all layers in specified network
     *
     * @param neuralNetwork neural network to randomize
     */
    public void randomize(NeuralNetwork neuralNetwork) {
        for (Layer layer : neuralNetwork.getLayers()) {
                this.randomize(layer);
        }
    }

    /**
     * Iterate and randomizes all neurons in specified layer
     *
     * @param layer layer to randomize
     */
    public void randomize(Layer layer) {
        for (Neuron neuron : layer.getNeurons()) {
            randomize(neuron);
        }
    }

    /**
     * Iterates and randomizes all connection weights in specified neuron
     *
     * @param neuron neuron to randomize
     */
    public void randomize(Neuron neuron) {
        int numberOfInputConnections = neuron.getInputConnections().length;
        double coefficient = 1d / Math.sqrt(numberOfInputConnections);
        coefficient = coefficient == 0 ? 1 : coefficient;
        for (Connection connection : neuron.getInputConnections()) {
//            connection.getWeight().setValue(coefficient * nextRandomWeight());
            connection.getWeight().setValue(nextRandomWeight());

        }
    }

    /**
     * Returns next random value from random generator, that will be used to initialize weight
     *
     * @return next random value fro random generator
     */
    protected double nextRandomWeight() {
        return randomGenerator.nextDouble();
    }
}
