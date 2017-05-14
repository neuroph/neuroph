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

package org.neuroph.nnet;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.comp.neuron.InputOutputNeuron;
import org.neuroph.nnet.learning.BinaryHebbianLearning;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Hopfield neural network.
 * Notes: try to use [1, -1] activation levels, sgn as transfer function, or real numbers for activation
 * @author Zoran Sevarac <sevarac@gmail.com>
 */

public class Hopfield extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 2L;

	/**
	 * Creates new Hopfield network with specified neuron number
	 * 
	 * @param neuronsCount
	 *            neurons number in Hopfied network
	 */
	public Hopfield(int neuronsCount) {

		// init neuron settings for hopfield network
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("neuronType", InputOutputNeuron.class);
		neuronProperties.setProperty("bias", new Double(0));
		neuronProperties.setProperty("transferFunction", TransferFunctionType.STEP);
		neuronProperties.setProperty("transferFunction.yHigh", new Double(1));
		neuronProperties.setProperty("transferFunction.yLow", new Double(0));

		this.createNetwork(neuronsCount, neuronProperties);
	}

	/**
	 * Creates new Hopfield network with specified neuron number and neuron
	 * properties
	 * 
	 * @param neuronsCount
	 *            neurons number in Hopfied network
	 * @param neuronProperties
	 *            neuron properties
	 */
	public Hopfield(int neuronsCount, NeuronProperties neuronProperties) {
		this.createNetwork(neuronsCount, neuronProperties);
	}

	/**
	 * Creates Hopfield network architecture
	 * 
	 * @param neuronsCount
	 *            neurons number in Hopfied network
	 * @param neuronProperties
	 *            neuron properties
	 */
	private void createNetwork(int neuronsCount, NeuronProperties neuronProperties) {

		// set network type
		this.setNetworkType(NeuralNetworkType.HOPFIELD);

		// createLayer neurons in layer
		Layer layer = LayerFactory.createLayer(neuronsCount, neuronProperties);

		// createLayer full connectivity in layer
		ConnectionFactory.fullConnect(layer, 0.1);

		// add layer to network
		this.addLayer(layer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set Hopfield learning rule for this network
		//this.setLearningRule(new HopfieldLearning(this));	
		this.setLearningRule(new BinaryHebbianLearning());			
	}

}
