package org.neuroph.adapters.encog;

import java.util.HashSet;
import java.util.Set;
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

/**
 *
 * @author zoran
 */
public class NeurophEncogAdapter {
    MultiLayerPerceptron neurophNeuralNet;
    FlatNetwork encogNeuralNet;

    public NeurophEncogAdapter(MultiLayerPerceptron neurophNeuralNet) {
        this.neurophNeuralNet = neurophNeuralNet;
        this.encogNeuralNet = flattenMultiLayerPerceptron(neurophNeuralNet);
    }
    
    

    public MultiLayerPerceptron getNeurophNeuralNet() {
        return neurophNeuralNet;
    }

    public FlatNetwork getEncogNeuralNet() {
        return encogNeuralNet;
    }
    
	/**
	 * Flatten a multi-layer perceptron.
	 * @param network The network to flatten.
	 * @return True, if the network was successfully flattened.
	 */
	private static boolean flattenMultiLayerPerceptron(
			MultiLayerPerceptron network) {

		FlatLayer[] flatLayers = new FlatLayer[network.getLayers().length];

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
			} else if (neuron.getClass() == BiasNeuron.class) {
                          biasCount++;
                         }
		}

		if (transferFunctions.size() > 1) {
                    return null;
                }

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
		} else {
                    return null;
                 }

		if (biasCount > 1) {
                    return null;
                }

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

		for (int layerIndex = network.getLayersCount() - 1; layerIndex > 0; layerIndex--) {
			Layer layer = network.getLayerAt(layerIndex);

			for (Neuron neuron : layer.getNeurons()) {
				for (Connection connection : neuron.getInputConnections()) {
					if (index >= weights.length)
						throw new EncogEngineError("Weight size mismatch.");

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

		for (int layerIndex = network.getLayers().size() - 1; layerIndex > 0; layerIndex--) {
			Layer layer = network.getLayers().get(layerIndex);

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
