package org.neuroph.contrib.convolution;

import org.neuroph.core.transfer.Linear;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.NeuronProperties;

public class InputMapLayer extends FeatureMapLayer {

	private static final long serialVersionUID = -4982081431101626705L;


	public InputMapLayer(MapDimension dimension) {
		super(null, dimension);
	}


	@Override
	public NeuronProperties getNeuronProperties() {
		NeuronProperties inputNeuronProperties = new NeuronProperties(InputNeuron.class, Linear.class);
		return inputNeuronProperties;
	}

}
