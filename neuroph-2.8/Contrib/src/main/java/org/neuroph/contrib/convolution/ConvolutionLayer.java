package org.neuroph.contrib.convolution;

import org.neuroph.core.input.WeightedSum;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

public class ConvolutionLayer extends FeatureMapLayer {

	private static final long serialVersionUID = -4619196904153707871L;

	public ConvolutionLayer(Kernel kernel, MapDimension dimension) {
		super(kernel, dimension);
	}

	@Override
	public NeuronProperties getNeuronProperties() {
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("useBias", true);
		neuronProperties.setProperty("transferFunction", TransferFunctionType.SIGMOID);
		neuronProperties.setProperty("inputFunction", WeightedSum.class);
		return neuronProperties;
	}
	
	
	

}
