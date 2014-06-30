package org.neuroph.nnet.learning;

import org.neuroph.nnet.comp.layer.ConvolutionalLayer;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.learning.BackPropagation;

public class ConvolutionalBackpropagation extends BackPropagation {

	private static final long serialVersionUID = -7134947805154423695L;

        @Override
	protected void calculateErrorAndUpdateHiddenNeurons() {
		Layer[] layers = neuralNetwork.getLayers();
		for (int layerIdx = layers.length - 2; layerIdx > 0; layerIdx--) {
			for (Neuron neuron : layers[layerIdx].getNeurons()) {
				double neuronError = this.calculateHiddenNeuronError(neuron);
				neuron.setError(neuronError);
				if (layers[layerIdx] instanceof ConvolutionalLayer) { // if it is convolutional layer c=adapt weughts, dont touch pooling. Pooling just propagate the error
					this.updateNeuronWeights(neuron);
				}
			} // for
		} // for
	}

        // why is thi seerror calculated this way. when many neurons update same weight
	@Override
	protected double calculateHiddenNeuronError(Neuron neuron) {
		double totalError = super.calculateHiddenNeuronError(neuron);
		Layer2D parentLayer = (Layer2D) neuron.getParentLayer();
		double weight = parentLayer.getDimensions().getHeight() * parentLayer.getDimensions().getWidth();
		return totalError / weight;
	}

}
