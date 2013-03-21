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

package org.neuroph.adapters.encog;

import java.util.HashSet;
import java.util.Set;
import org.encog.Encog;
import org.encog.EncogError;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.neural.flat.FlatLayer;
import org.encog.neural.flat.FlatNetwork;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.transfer.Linear;
import org.neuroph.core.transfer.Sigmoid;
import org.neuroph.core.transfer.Tanh;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.plugins.PluginBase;

/**
 * The FlatNetworkPlugin allows a Neuroph multi-layer-perceptron to make use of the Encog Engine.
 * The Encog Engine makes use of flat, array-based, networks to train and process in a very efficient
 * manner.  
 * 
 * To make use of this plug-in, call the flattenNeuralNetworkNetwork method with the desired neural
 * network.  The network will now train with multithreaded RPROP.  The network can still be used as a
 * normal Neuroph network, however, if you change the structure of the network, you should reflatten it.
 * 
 * To remove the plugin, use the unFlattenNeuralNetworkNetwork method.
 * 
 * If you wish to use OpenCL/GPU processing, call the initCL method.
 * 
 * Finally, once you are ready to exit your application, you should call the shutdown
 * method to make sure that the threadpool and OpenCL resources have been properly shutdown.
 * 
 * @author Jeff Heaton (http://www.jeffheaton.com)
 */
public class FlatNetworkPlugin extends PluginBase {

	/**
	 * The serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the plugin.
	 */
	public static final String PLUGIN_NAME = "FlatNetworkPlugin";

	/**
	 * The flat network being used.
	 */
	public FlatNetwork flatNetwork;

	/**
	 * Construct a flat network plugin.
	 * @param network The flat network.
	 */
	public FlatNetworkPlugin(FlatNetwork network) {
		super(PLUGIN_NAME);
		this.flatNetwork = network;
	}

	/**
	 * @return The actual flat network being used.
	 */
	public FlatNetwork getFlatNetwork() {
		return this.flatNetwork;
	}

	/**
	 * This method is used to install the driver into a neural network.
	 * @param network The neural network to flatten.
	 * @return True if the flatening was successful.
	 */
	public static boolean flattenNeuralNetworkNetwork(NeuralNetwork network) {
		if (network instanceof MultiLayerPerceptron) {
			return flattenMultiLayerPerceptron((MultiLayerPerceptron) network);
		} else
			// failed to flaten the network
			return false;
	}

	/**
	 * Init the OpenCL.
	 */
	public static void initCL() {
		EncogEngine.getInstance().initCL();
	}
	
	/**
	 * Make sure that the threadpool and OpenCL are properly shutdown.
	 */
	public static void shutdown()
	{
		Encog.getInstance().shutdown();
	}

	/**
	 * Flatten a multi-layer perceptron.
	 * @param network The network to flatten.
	 * @return True, if the network was successfully flattened.
	 */
	private static boolean flattenMultiLayerPerceptron(
			MultiLayerPerceptron network) {

		FlatLayer[] flatLayers = new FlatLayer[network.getLayersCount()];

		int index = 0;

		for (Layer layer : network.getLayers()) {
			FlatLayer flatLayer = flattenLayer(layer);
			if (flatLayer == null)
				return false;
			flatLayers[index++] = flatLayer;
		}

		FlatNetwork flat = new FlatNetwork(flatLayers);
		FlatNetworkPlugin plugin = new FlatNetworkPlugin(flat);
		network.addPlugin(plugin);

		FlatNetworkLearning training = new FlatNetworkLearning(flat);
		network.setLearningRule(training);

		flattenWeights(flat, network);

		return true;
	}

	/**
	 * Flatten the specified layer.
	 * @param layer The layer to flatten.
	 * @return The flat layer that represents the provided layer.
	 */
	private static FlatLayer flattenLayer(Layer layer) {
		boolean inputLayer = false;
		Set<Class<?>> transferFunctions = new HashSet<Class<?>>();
		int neuronCount = 0;
		int biasCount = 0;
		TransferFunction transfer = null;

		for (Neuron neuron : layer.getNeurons()) {
			if (neuron.getClass() == InputNeuron.class)
				inputLayer = true;

			if (neuron.getClass() == Neuron.class
					|| neuron.getClass() == InputNeuron.class) {
				neuronCount++;

				transfer = neuron.getTransferFunction();
				transferFunctions.add(transfer.getClass());
			} else if (neuron.getClass() == BiasNeuron.class)
				biasCount++;
		}

		if (transferFunctions.size() > 1)
			return null;

		Class<?> t = transferFunctions.iterator().next();

		double slope = 1;

		ActivationFunction activation = null;
		
		if (inputLayer)
			activation = new ActivationLinear();
		else if (t == Linear.class) {
			slope = ((Linear) transfer).getSlope();
			activation = new ActivationLinear();
		} else if (t == Sigmoid.class) {
			slope = ((Sigmoid) transfer).getSlope();
			activation = new ActivationSigmoid();
		} else if (t == Tanh.class) {
			slope = ((Tanh) transfer).getSlope();
			activation = new ActivationTANH();
		} else
			return null;

		if (biasCount > 1)
			return null;

		double[] params = { slope };
		return new FlatLayer(activation, neuronCount, biasCount == 1 ? 1.0:0.0, params);
	}

	/**
	 * Replace all of the weights with FlatWeights.
	 * @param flatNetwork The flat network.
	 * @param network The neural network.
	 */
	private static void flattenWeights(FlatNetwork flatNetwork,
			NeuralNetwork network) {
		double[] weights = flatNetwork.getWeights();

		int index = 0;

		for (int layerIndex = network.getLayersCount(); layerIndex > 0; layerIndex--) {
			Layer layer = network.getLayerAt(layerIndex);

			for (Neuron neuron : layer.getNeurons()) {
				for (Connection connection : neuron.getInputConnections()) {
					if (index >= weights.length)
						throw new EncogError("Weight size mismatch.");

					Weight weight = connection.getWeight();
					FlatWeight flatWeight = new FlatWeight(weights, index++);
					flatWeight.setValue(weight.getValue());
					connection.setWeight(flatWeight);
				}
			}
		}
	}

	/**
	 * Remove the flat network plugin, and replace flat weights with regular Neuroph weights.
	 * 
	 * @param network The network to unflatten.
	 * @return True if unflattening was successful.
	 */
	public static boolean unFlattenNeuralNetworkNetwork(NeuralNetwork network) {

		for (int layerIndex = network.getLayersCount() - 1; layerIndex > 0; layerIndex--) {
			Layer layer = network.getLayerAt(layerIndex);

			for (Neuron neuron : layer.getNeurons()) {
				for (Connection connection : neuron.getInputConnections()) {
					Weight weight = connection.getWeight();

					if (weight instanceof FlatWeight) {
						Weight weight2 = new Weight(weight.getValue());
						//weight2.setPreviousValue(weight.getPreviousValue());                                                
                                                //weight2.getTrainingData().set(TrainingData.PREVIOUS_WEIGHT, weight.getTrainingData().get(TrainingData.PREVIOUS_WEIGHT));
                                                ((MomentumBackpropagation.MomentumWeightTrainingData)weight2.getTrainingData()).previousValue = ((MomentumBackpropagation.MomentumWeightTrainingData)weight2.getTrainingData()).previousValue;
						connection.setWeight(weight2);
					}
				}
			}
		}

		network.removePlugin(FlatNetworkPlugin.class);
		return true;
	}
}
