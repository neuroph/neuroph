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
import org.neuroph.nnet.learning.UnsupervisedHebbianLearning;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Hebbian neural network with unsupervised Hebbian learning algorithm.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class UnsupervisedHebbianNetwork extends NeuralNetwork {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * Creates an instance of Unsuervised Hebian net with specified number 
	 * of neurons in input and output layer
	 * 
	 * @param inputNeuronsNum
	 *            number of neurons in input layer
	 * @param outputNeuronsNum
	 *            number of neurons in output layer
	 */
	public UnsupervisedHebbianNetwork(int inputNeuronsNum, int outputNeuronsNum) {
		this.createNetwork(inputNeuronsNum, outputNeuronsNum,
			TransferFunctionType.LINEAR);

	}

	/**
	 * Creates an instance of Unsuervised Hebian net with specified number
	 * of neurons in input layer and output layer, and transfer function
	 * 
	 * @param inputNeuronsNum
	 *            number of neurons in input layer
	 * @param outputNeuronsNum
	 *            number of neurons in output layer
	 * @param transferFunctionType
	 *            transfer function type id
	 */
	public UnsupervisedHebbianNetwork(int inputNeuronsNum, int outputNeuronsNum,
		TransferFunctionType transferFunctionType) {
		this.createNetwork(inputNeuronsNum, outputNeuronsNum,
			transferFunctionType);
	}

	/**
	 * Creates an instance of Unsuervised Hebian net with specified number
	 * of neurons in input layer and output layer, and transfer function
	 * 
	 * @param inputNeuronsNum
	 *            number of neurons in input layer
	 * @param outputNeuronsNum
	 *            number of neurons in output layer
	 * @param transferFunctionType
	 *            transfer function type
	 */
	private void createNetwork(int inputNeuronsNum, int outputNeuronsNum,
		TransferFunctionType transferFunctionType) {

		// init neuron properties
		NeuronProperties neuronProperties = new NeuronProperties();
//		neuronProperties.setProperty("bias", new Double(-Math
//				.abs(Math.random() - 0.5))); // Hebbian network cann not work
		// without bias
		neuronProperties.setProperty("transferFunction", transferFunctionType);
		neuronProperties.setProperty("transferFunction.slope", new Double(1));

		// set network type code
		this.setNetworkType(NeuralNetworkType.UNSUPERVISED_HEBBIAN_NET);

		// createLayer input layer
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsNum,
			neuronProperties);
		this.addLayer(inputLayer);

		// createLayer output layer
		Layer outputLayer = LayerFactory.createLayer(outputNeuronsNum,
			neuronProperties);
		this.addLayer(outputLayer);

		// createLayer full conectivity between input and output layer
		ConnectionFactory.fullConnect(inputLayer, outputLayer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set appropriate learning rule for this network
		this.setLearningRule(new UnsupervisedHebbianLearning());
	//this.setLearningRule(new OjaLearning(this));
	}
}

