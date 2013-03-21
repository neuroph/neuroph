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
import org.neuroph.nnet.learning.InstarLearning;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Instar neural network with Instar learning rule.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Instar extends NeuralNetwork {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new Instar with specified number of input neurons.
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 */
	public Instar(int inputNeuronsCount) {
		this.createNetwork(inputNeuronsCount);
	}	
	
	/**
	 * Creates Instar architecture with specified number of input neurons
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 */
	private void createNetwork(int inputNeuronsCount ) {

		// set network type
		this.setNetworkType(NeuralNetworkType.INSTAR);

		// init neuron settings for this type of network
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction", TransferFunctionType.STEP);
		
		// create input layer
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, neuronProperties);
		this.addLayer(inputLayer);

		// createLayer output layer
		neuronProperties.setProperty("transferFunction", TransferFunctionType.STEP);
		Layer outputLayer = LayerFactory.createLayer(1,	neuronProperties);
		this.addLayer(outputLayer);

		// create full conectivity between input and output layer
		ConnectionFactory.fullConnect(inputLayer, outputLayer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set appropriate learning rule for this network
		this.setLearningRule(new InstarLearning());
	}	
	
}
