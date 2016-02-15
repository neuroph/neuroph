package org.neuroph.nnet.learning;

import java.util.List;
import org.neuroph.nnet.comp.layer.ConvolutionalLayer;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;

public class ConvolutionalBackpropagation extends MomentumBackpropagation {

	private static final long serialVersionUID = -7134947805154423695L;

        @Override
	protected void calculateErrorAndUpdateHiddenNeurons() {
		List<Layer> layers = neuralNetwork.getLayers();
		for (int layerIdx = layers.size() - 2; layerIdx > 0; layerIdx--) {
			for (Neuron neuron : layers.get(layerIdx).getNeurons()) {
				double neuronError = this.calculateHiddenNeuronError(neuron);
				neuron.setError(neuronError);
				if (layers.get(layerIdx) instanceof ConvolutionalLayer) { // if it is convolutional layer c=adapt weughts, dont touch pooling. Pooling just propagate the error
					this.updateNeuronWeights(neuron);
				}
			} // for
		} // for
	}

//	@Override
//	protected double calculateHiddenNeuronError(Neuron neuron) {
//		double totalError = super.calculateHiddenNeuronError(neuron);
//
//        if (neuron.getParentLayer() instanceof  Layer2D) {
//            Layer2D parentLayer = (Layer2D) neuron.getParentLayer();
//            double weight = parentLayer.getHeight() * parentLayer.getWidth();
//            return totalError / weight;
//        }
//        return totalError;
//	}

}
