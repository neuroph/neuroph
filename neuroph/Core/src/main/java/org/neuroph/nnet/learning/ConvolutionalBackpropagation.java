package org.neuroph.nnet.learning;

import java.util.List;
import org.neuroph.core.Connection;
import org.neuroph.nnet.comp.layer.ConvolutionalLayer;
import org.neuroph.nnet.comp.layer.FeatureMapLayer;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.TransferFunction;

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

        
        // ova mora da se overriduje jer glavna uzima izvod //  ali ova treba samo za pooling sloj 
    @Override     
    protected double calculateHiddenNeuronError(Neuron neuron) {

        // for convolutional layers use standard backprop formula
        if (neuron.getParentLayer() instanceof ConvolutionalLayer ) {
            return super.calculateHiddenNeuronError(neuron);
        }
                
        // for pooling layer just transfer error without using tranfer function derivative
        double deltaSum = 0d;
        for (Connection connection : neuron.getOutConnections()) {
            double delta = connection.getToNeuron().getError()
                    * connection.getWeight().value;
            deltaSum += delta; // weighted delta sum from the next layer
        } // for

       return deltaSum;
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
