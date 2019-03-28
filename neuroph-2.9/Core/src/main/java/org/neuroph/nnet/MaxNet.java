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
import org.neuroph.nnet.comp.layer.CompetitiveLayer;
import org.neuroph.nnet.comp.neuron.CompetitiveNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Max Net neural network with competitive learning rule.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MaxNet extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new Maxnet network with specified neuron number
	 * 
	 * @param neuronsCount
	 *            number of neurons in MaxNet network (same number in input and output layer)
	 */
	public MaxNet(int neuronsCount) {
		this.createNetwork(neuronsCount);
	}

	/**
	 * Creates MaxNet network architecture
	 * 
	 * @param neuronNum
	 *            neuron number in network
	 * @param neuronProperties
	 *            neuron properties
	 */
	private void createNetwork(int neuronsCount) {

		// set network type
		this.setNetworkType(NeuralNetworkType.MAXNET);

		// createLayer input layer in layer
		Layer inputLayer = LayerFactory.createLayer(neuronsCount,
				new NeuronProperties());
		this.addLayer(inputLayer);

		// createLayer properties for neurons in output layer
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("neuronType", CompetitiveNeuron.class);
		neuronProperties.setProperty("transferFunction", TransferFunctionType.RAMP);

		// createLayer full connectivity in competitive layer
		CompetitiveLayer competitiveLayer = new CompetitiveLayer(neuronsCount, neuronProperties);

		// add competitive layer to network
		this.addLayer(competitiveLayer);

		double competitiveWeight = -(1 / (double) neuronsCount);
		// createLayer full connectivity within competitive layer
		ConnectionFactory.fullConnect(competitiveLayer, competitiveWeight, 1);

		// createLayer forward connectivity from input to competitive layer
		ConnectionFactory.forwardConnect(inputLayer, competitiveLayer, 1);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);
	}

}
