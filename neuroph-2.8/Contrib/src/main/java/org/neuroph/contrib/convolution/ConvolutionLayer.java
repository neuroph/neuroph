package org.neuroph.contrib.convolution;

import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

public class ConvolutionLayer extends FeatureMapsLayer {

	private static final long serialVersionUID = -4619196904153707871L;

	public static NeuronProperties neuronProperties = new NeuronProperties();

	static {
		neuronProperties.setProperty("useBias", true);
		neuronProperties.setProperty("transferFunction", TransferFunctionType.SIGMOID);
		neuronProperties.setProperty("inputFunction", WeightedSum.class);
	}

	public ConvolutionLayer(FeatureMapsLayer fromLayer, Kernel kernel) {
		super(kernel);
		Layer2D.Dimension fromDimension = fromLayer.getDimension();

		int mapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
		int mapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
		this.mapDimension = new Layer2D.Dimension(mapWidth, mapHeight);
	}

	@Override
	public void connectMaps(Layer2D fromMap, Layer2D toMap) {

		int numberOfSharedWeights = kernel.getArea();
		// create shared weights array
		Weight[] weights = new Weight[numberOfSharedWeights];
		for (int i = 0; i < numberOfSharedWeights; i++) {
			Weight weight = new Weight();
			weight.randomize(-1, 1);
			weights[i] = weight;
		}

		for (int x = 0; x < toMap.getWidth(); x++) {
			for (int y = 0; y < toMap.getHeight(); y++) {
				Neuron toNeuron = toMap.getNeuronAt(x, y);
				for (int dy = 0; dy < kernel.getHeight(); dy++) {
					for (int dx = 0; dx < kernel.getWidth(); dx++) {
						int fromX = x + dx;
						int fromY = y + dy;
						int currentWeightIndex = dx + dy * kernel.getHeight();
						Neuron fromNeuron = fromMap.getNeuronAt(fromX, fromY);
						ConnectionFactory.createConnection(fromNeuron, toNeuron, weights[currentWeightIndex]);
					}
				}
			}
		}
	}

	// public ConvolutionLayer(Kernel kernel, Layer2D.Dimension mapDimension) {
	// super(kernel, mapDimension);
	// also specify how many maps so they can be created here

	// for (int i = 0; i < mapDimension.getHeight() * mapDimension.getWidth();
	// i++) {
	// Neuron neuron = NeuronFactory.createNeuron(neuronProperties);
	// addNeuron(neuron);
	// }
	// }

	// public ConvolutionLayer(FeatureMapsLayer fromLayer, Kernel kernel) {
	// Layer2D.Dimension fromDimension = fromLayer.getDimension();
	// Layer2D.Dimension toDimension;
	//
	// int toMapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
	// int toMapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
	// toDimension = new Layer2D.Dimension(toMapWidth, toMapHeight);
	// this(kernel, toDimension);
	//
	// }

}
