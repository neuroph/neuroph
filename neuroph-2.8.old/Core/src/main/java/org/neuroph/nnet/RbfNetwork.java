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
import org.neuroph.core.input.Difference;
import org.neuroph.core.transfer.Gaussian;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.util.*;

/**
 * Radial basis function neural network.
 * 
 * TODO: learning for rbf layer: k-means clustering
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class RbfNetwork extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new RbfNetwork with specified number of neurons in input, rbf and output layer
	 * 
	 * @param inputNeuronsCount
	 *		number of neurons in input layer
	 * @param rbfNeuronsCount
	 *		number of neurons in rbf layer
	 * @param outputNeuronsCount
	 *		number of neurons in output layer
	 */
	public RbfNetwork(int inputNeuronsCount, int rbfNeuronsCount, int outputNeuronsCount) {
		this.createNetwork(inputNeuronsCount, rbfNeuronsCount, outputNeuronsCount);
	}

	/**
	 * Creates RbfNetwork architecture with specified number of neurons in input
	 * layer, output layer and transfer function
	 * 
	 * @param inputNeuronsCount
	 *		number of neurons in input layer
	 * @param rbfNeuronsCount
	 *		number of neurons in rbf layer
	 * @param outputNeuronsCount
	 *		number of neurons in output layer
	 */
	private void createNetwork(int inputNeuronsCount, int rbfNeuronsCount,
			int outputNeuronsCount) {
		// init neuron settings for this network
		NeuronProperties rbfNeuronProperties = new NeuronProperties();
		rbfNeuronProperties.setProperty("inputFunction", Difference.class);
		rbfNeuronProperties.setProperty("transferFunction", Gaussian.class);

		// set network type code
		this.setNetworkType(NeuralNetworkType.RBF_NETWORK);

		// create input layer
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, TransferFunctionType.LINEAR);
		this.addLayer(inputLayer);

		// create rbf layer
		Layer rbfLayer = LayerFactory.createLayer(rbfNeuronsCount, rbfNeuronProperties);
		this.addLayer(rbfLayer);

		// create output layer
		Layer outputLayer = LayerFactory.createLayer(outputNeuronsCount, TransferFunctionType.LINEAR);
		this.addLayer(outputLayer);

		// create full conectivity between input and rbf layer
		ConnectionFactory.fullConnect(inputLayer, rbfLayer);
		// create full conectivity between rbf and output layer
		ConnectionFactory.fullConnect(rbfLayer, outputLayer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set appropriate learning rule for this network
		this.setLearningRule(new LMS());
	}

}