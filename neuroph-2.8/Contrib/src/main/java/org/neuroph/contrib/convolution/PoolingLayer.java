package org.neuroph.contrib.convolution;

import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.input.Max;
import org.neuroph.core.transfer.Linear;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuronProperties;

public class PoolingLayer extends FeatureMapsLayer {

	private static final long serialVersionUID = -6771501759374920877L;

	public static NeuronProperties neuronProperties = new NeuronProperties();

	static {
		neuronProperties.setProperty("useBias", false);
		neuronProperties.setProperty("transferFunction", Linear.class);
		neuronProperties.setProperty("inputFunction", Max.class);
	}

	public PoolingLayer(FeatureMapsLayer fromLayer, Kernel kernel) {
		super(kernel);
		Layer2D.Dimension fromDimension = fromLayer.getDimension();

		int mapWidth = fromDimension.getWidth() / kernel.getWidth();
		int mapHeight = fromDimension.getHeight() / kernel.getHeight();
		this.mapDimension = new Layer2D.Dimension(mapWidth, mapHeight);
	}

	@Override
	public void connectMaps(Layer2D fromMap, Layer2D toMap) {
		int kernelWidth = kernel.getWidth();
		int kernelHeight = kernel.getHeight();

		for (int x = 0; x < fromMap.getWidth() - kernelWidth + 1; x += kernelWidth) {
			for (int y = 0; y < fromMap.getHeight() - kernelHeight + 1; y += kernelHeight) {
				Weight weight = new Weight();
				weight.setValue(1);
				Neuron toNeuron = toMap.getNeuronAt(x / kernelWidth, y / kernelHeight);
				for (int dy = 0; dy < kernelHeight; dy++) {
					for (int dx = 0; dx < kernelWidth; dx++) {
						int fromX = x + dx;
						int fromY = y + dy;
						Neuron fromNeuron = fromMap.getNeuronAt(fromX, fromY);
						ConnectionFactory.createConnection(fromNeuron, toNeuron, weight);
					}
				}
			}
		}
	}

	// @Override
	// public void connectTo(FeatureMapsLayer toLayer, int fromFeatureMapIndex,
	// int toFeatureMapIndex) {
	// FeatureMap fromMap = getFeatureMap(fromFeatureMapIndex);
	// FeatureMap toMap = toLayer.getFeatureMap(toFeatureMapIndex);
	//
	// // Max inputFunction = new Max();
	// // TransferFunction transferFunction = new PoollingTransferFunction();
	//
	// int kernelWidth = kernel.getWidth();
	// int kernelHeight = kernel.getHeight();
	//
	// for (int x = 0; x < fromMap.getWidth() - kernelWidth + 1; x +=
	// kernelWidth) {
	// for (int y = 0; y < fromMap.getHeight() - kernelHeight + 1; y +=
	// kernelHeight) {
	// Weight weight = new Weight();
	// weight.setValue(1);
	// Neuron toNeuron = toMap.getNeuronAt(x / kernelWidth, y / kernelHeight);
	// for (int dy = 0; dy < kernelHeight; dy++) {
	// for (int dx = 0; dx < kernelWidth; dx++) {
	// int fromX = x + dx;
	// int fromY = y + dy;
	// Neuron fromNeuron = fromMap.getNeuronAt(fromX, fromY);
	// ConnectionFactory.createConnection(fromNeuron, toNeuron, weight);
	// // toNeuron.setInputFunction(inputFunction);
	// // toNeuron.setTransferFunction(transferFunction);
	// }
	// }
	// }
	// }
	// }

}
