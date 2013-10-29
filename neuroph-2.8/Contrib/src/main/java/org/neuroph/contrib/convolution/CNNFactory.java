package org.neuroph.contrib.convolution;

import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuronFactory;

public class CNNFactory {

	public enum Layer2DType {
		CONVOLUTION, POOLING
	}

	public static FeatureMapLayer createNextLayer(FeatureMapLayer fromLayer, Kernel kernel, Layer2DType type) {
		MapDimension fromDimension = fromLayer.getDimension();
		MapDimension toDimension;
		FeatureMapLayer toLayer;

		if (type == Layer2DType.CONVOLUTION) {
			int toMapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
			int toMapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
			toDimension = new MapDimension(toMapWidth, toMapHeight);
			toLayer = new ConvolutionLayer(kernel, toDimension);
		} else {
			int toMapWidth = fromDimension.getWidth() / kernel.getWidth();
			int toMapHeight = fromDimension.getHeight() / kernel.getHeight();
			toDimension = new MapDimension(toMapWidth, toMapHeight);
			toLayer = new PoolingLayer(kernel, toDimension);
		}

		return toLayer;
	}

	public static FeatureMapLayer creteInputLayer(MapDimension dimension) {
		return new InputMapLayer(dimension);
	}

	public static void addFeatureMap(FeatureMapLayer mapLayer) {
		MapDimension mapDimension = mapLayer.getDimension();
		FeatureMap map = new FeatureMap(mapDimension);

		for (int i = 0; i < mapDimension.getHeight() * mapDimension.getWidth(); i++) {
			Neuron neuron = NeuronFactory.createNeuron(mapLayer.getNeuronProperties());
			map.addNeuron(neuron);
		}
		mapLayer.addFeatureMap(map);
	}

	public static void addFeatureMaps(FeatureMapLayer mapLayer, int numberOfLayers) {
		for (int i = 0; i < numberOfLayers; i++) {
			addFeatureMap(mapLayer);
		}
	}

	public static void fullConectMapLayers(FeatureMapLayer fromLayer, FeatureMapLayer toLayer) {
		if (toLayer instanceof ConvolutionLayer ) {
			for (int i = 0; i < fromLayer.getNumberOfMaps(); i++) {
				for (int j = 0; j < toLayer.getNumberOfMaps(); j++) {
 					connect(fromLayer, toLayer, i, j);
				}
			}
		} else {
			for (int i = 0; i < toLayer.getNumberOfMaps(); i++) {
				connect(fromLayer, toLayer, i, i);
			}
		}
	}

	public static void connect(FeatureMapLayer fromLayer, FeatureMapLayer toLayer, int fromFeatureMapIndex, int toFeatureMapIndex) {
		FeatureMap fromMap = fromLayer.getFeatureMap(fromFeatureMapIndex);
		FeatureMap toMap = toLayer.getFeatureMap(toFeatureMapIndex);
		Kernel kernel = toLayer.getKernel();

		if (toLayer instanceof ConvolutionLayer) {
			connectConvolutionLayer(fromMap, toMap, kernel);
		} else {
			connectPoolingLayer(fromMap, toMap, kernel);
		}

	}

	private static void connectPoolingLayer(FeatureMap fromMap, FeatureMap toMap, Kernel kernel) {
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

	private static void connectConvolutionLayer(FeatureMap fromMap, FeatureMap toMap, Kernel kernel) {
		int numberOfSharedWeights = kernel.area();
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

}
