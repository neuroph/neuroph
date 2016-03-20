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
package org.neuroph.util;

import java.util.List;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;

/**
 * A CODEC encodes and decodes neural networks, much like the more standard
 * definition of a CODEC encodes and decodes audio/video.
 * 
 * This CODEC can encode a neural network to an array of doubles. It can also
 * decode this array of doubles back into a neural network. This is very useful
 * for both simulated annealing and genetic algorithms.
 * 
 * @author Jeff Heaton (http://www.heatonresearch.com)
 */
public class NeuralNetworkCODEC {

	/**
	 * Private constructor.
	 */
	private NeuralNetworkCODEC() {

	}

	/**
	 * Encode a network to an array.
	 * @param network The network to encode.
	 */
	public static void network2array(NeuralNetwork network, double[] array) {
		int index = 0;

                 List<Layer> layers = network.getLayers();
		for (Layer layer : layers) {
			for (Neuron neuron : layer.getNeurons()) {
				for (Connection connection : neuron.getOutConnections()) {
					array[index++] = connection.getWeight().getValue();
				}
			}
		}
	}

	/**
	 * Decode a network from an array.
	 * @param array The array used to decode.
	 * @param network The network to decode into.
	 */
	public static void array2network(double[] array, NeuralNetwork network) {
		int index = 0;
                
                List<Layer> layers = network.getLayers();
                for (Layer layer : layers) {
			for (Neuron neuron : layer.getNeurons()) {
				for (Connection connection : neuron.getOutConnections()) {
					connection.getWeight().setValue(array[index++]);
					//connection.getWeight().setPreviousValue(array[index++]);
				}
			}
		}
	}

	/**
	 * Determine the array size for the given neural network.
	 * @param network The neural network to determine for.
	 * @return The size of the array necessary to hold that network.
	 */
	public static int determineArraySize(NeuralNetwork network) {
		int result = 0;

                List<Layer> layers = network.getLayers();
		for (Layer layer : layers) {
			for (Neuron neuron : layer.getNeurons()) {
				result+=neuron.getOutConnections().size();
			}
		}
		return result;
	}
}
