package org.neuroph.contrib.convolution;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;

/**
 * Provide Hiton, leCun, AndwNg implementation
 * 
 */
public class ConvolutionNeuralNetwork extends NeuralNetwork<BackPropagation> {

	private static final long serialVersionUID = -1393907449047650509L;

	public ConvolutionNeuralNetwork() {
		this.setLearningRule(new MomentumBackpropagation());
	}
	

	public void connectLayers(FeatureMapsLayer fromLayer, FeatureMapsLayer toLayer, int fromFeatureMapIndex, int toFeatureMapIndex) {
		ConvolutionUtils.connectFeatureMaps(fromLayer, toLayer, fromFeatureMapIndex, toFeatureMapIndex);
	}

	@Override
	public void setInput(double... inputVector) throws VectorSizeMismatchException {
		// if (inputVector.length != getInputNeurons().length) {
		// throw new
		// VectorSizeMismatchException("Input vector size does not match network input dimension!");
		// }
		// It would be good if i could do the following:
		// int i = 0;
		// Layer inputLayer =getLayerAt(0);
		// for (Neuron neuron : inputLayer.getNeurons()){
		// neuron.setInput(inputVector[i++]);
		// }
		// But for that getNeuron must be non final method

		FeatureMapsLayer inputLayer = (FeatureMapsLayer) getLayerAt(0);
		int currentNeuron = 0;
		for (int i = 0; i < inputLayer.getNumberOfMaps(); i++) {
			Layer2D map = inputLayer.getFeatureMap(i);
			for (Neuron neuron : map.getNeurons()) {
				if (!(neuron instanceof BiasNeuron))
					neuron.setInput(inputVector[currentNeuron++]);
			}
		}
	}

}
