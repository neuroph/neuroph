package org.neuroph.contrib.convolution.learning;

import org.neuroph.contrib.convolution.ConvolutionLayer;
import org.neuroph.contrib.convolution.Layer2D;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.learning.BackPropagation;

public class ConvolutionBackpropagation extends BackPropagation {

	private static final long serialVersionUID = -7134947805154423695L;

	protected void calculateErrorAndUpdateHiddenNeurons() {
		Layer[] layers = neuralNetwork.getLayers();
		for (int layerIdx = layers.length - 2; layerIdx > 0; layerIdx--) {
			for (Neuron neuron : layers[layerIdx].getNeurons()) {
				double neuronError = this.calculateHiddenNeuronError(neuron);
				neuron.setError(neuronError);
				if (layers[layerIdx] instanceof ConvolutionLayer) {
					this.updateNeuronWeights(neuron);
				}
			} // for
		} // for
	}

	@Override
	protected double calculateHiddenNeuronError(Neuron neuron) {
		double totalError = super.calculateHiddenNeuronError(neuron);
		Layer2D parentLayer = (Layer2D) neuron.getParentLayer();
		double weight = parentLayer.getDimension().getHeight() * parentLayer.getDimension().getWidth();
		return totalError / weight;
	}

}
