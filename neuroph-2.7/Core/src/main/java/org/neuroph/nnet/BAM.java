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
 * Bidirectional Associative Memory
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class BAM extends NeuralNetwork {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an instance of BAM network with specified number of neurons
         * in input and output layers.
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 */
	public BAM(int inputNeuronsCount, int outputNeuronsCount) {

		// init neuron settings for BAM network
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("neuronType", InputOutputNeuron.class);
		neuronProperties.setProperty("bias", new Double(0));
		neuronProperties.setProperty("transferFunction", TransferFunctionType.STEP);
		neuronProperties.setProperty("transferFunction.yHigh", new Double(1));
		neuronProperties.setProperty("transferFunction.yLow", new Double(0));

		this.createNetwork(inputNeuronsCount, outputNeuronsCount, neuronProperties);
	}	
	
	/**
	 * Creates BAM network architecture
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 * @param neuronProperties
	 *            neuron properties
	 */
	private void createNetwork(int inputNeuronsCount, int outputNeuronsCount,  NeuronProperties neuronProperties) {

                // set network type
		this.setNetworkType(NeuralNetworkType.BAM);

		// create input layer
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, neuronProperties);
		// add input layer to network
		this.addLayer(inputLayer);

		// create output layer
		Layer outputLayer = LayerFactory.createLayer(outputNeuronsCount, neuronProperties);	
		// add output layer to network
		this.addLayer(outputLayer);
		
		// create full connectivity from in to out layer	
		ConnectionFactory.fullConnect(inputLayer, outputLayer);		
		// create full connectivity from out to in layer
		ConnectionFactory.fullConnect(outputLayer, inputLayer);
				
		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set Hebbian learning rule for this network
		this.setLearningRule(new BinaryHebbianLearning());			
	}		
}
