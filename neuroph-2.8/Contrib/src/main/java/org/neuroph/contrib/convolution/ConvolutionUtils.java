package org.neuroph.contrib.convolution;

import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.util.ConnectionFactory;

public class ConvolutionUtils {

	// public enum Layer2DType {
	// CONVOLUTION, POOLING
	// }

	// public static ConvolutionLayer createConvolutionLayer(FeatureMapsLayer
	// fromLayer, Kernel kernel) {
	// Layer2D.Dimension fromDimension = fromLayer.getDimension();
	//
	// int toMapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
	// int toMapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
	// Layer2D.Dimension toDimension = new Layer2D.Dimension(toMapWidth,
	// toMapHeight);
	// ConvolutionLayer toLayer = new ConvolutionLayer(kernel, toDimension);
	//
	// return toLayer;
	// return new ConvolutionLayer(fromLayer, kernel);
	// }

	/**
	 * Creates and returns instance of Layer2D with specified dimension and
	 * neuron properties
	 * 
	 * @param mapDimension
	 * @param neuronProperties
	 * @return
	 */
	// public static Layer2D createFeatureMap(Layer2D.Dimension mapDimension,
	// NeuronProperties neuronProperties) {
	// Layer2D featureMap = new Layer2D(mapDimension);
	//
	// for (int i = 0; i < mapDimension.getHeight() * mapDimension.getWidth();
	// i++) {
	// Neuron neuron = NeuronFactory.createNeuron(neuronProperties);
	// featureMap.addNeuron(neuron);
	// }
	//
	// return featureMap;
	// }

	/**
	 * Creates next convolutional or pooling layer in convolutional network for
	 * given kernel
	 * 
	 * @param fromLayer
	 * @param convolutionKernel
	 * @param type
	 * @return
	 * @deprecated
	 */
	// public static FeatureMapsLayer createNextLayer(FeatureMapsLayer
	// fromLayer, Kernel kernel, Layer2DType type) {
	// Layer2D.Dimension fromDimension = fromLayer.getDimension();
	// Layer2D.Dimension toDimension;
	// FeatureMapsLayer toLayer;
	//
	// if (type == Layer2DType.CONVOLUTION) {
	// int toMapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
	// int toMapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
	// toDimension = new Layer2D.Dimension(toMapWidth, toMapHeight);
	// toLayer = new ConvolutionLayer(kernel, toDimension);
	// } else { // POOLING layer
	// int toMapWidth = fromDimension.getWidth() / kernel.getWidth();
	// int toMapHeight = fromDimension.getHeight() / kernel.getHeight();
	// toDimension = new Layer2D.Dimension(toMapWidth, toMapHeight);
	// toLayer = new PoolingLayer(kernel, toDimension);
	// }
	//
	// return toLayer;
	// }

	// use constructor call instead this method
	// public static FeatureMapsLayer creteInputLayer(MapDimension dimension) {
	// return new InputMapLayer(dimension);
	// }

	// umesto ove dve metode dodati jednu createFeatureMapsLayer(Dimension,
	// howManyMaps,) - ovo mozda ima smisla i da se radi iz konstruktora
	// public static void addFeatureMap(FeatureMapsLayer mapsLayer) {
	// Layer2D.Dimension mapDimension = mapsLayer.getDimension();
	// Layer2D featureMap = new Layer2D(mapDimension);
	//
	// for (int i = 0; i < mapDimension.getHeight() * mapDimension.getWidth();
	// i++) {
	// Neuron neuron =
	// NeuronFactory.createNeuron(mapsLayer.getNeuronProperties());
	// featureMap.addNeuron(neuron);
	// }
	// mapsLayer.addFeatureMap(featureMap);
	// }

	// public static void addFeatureMaps(FeatureMapsLayer mapsLayer, int
	// numberOfLayers) {
	// for (int i = 0; i < numberOfLayers; i++) { // da li ovde treba isti layer
	// da se doda nekoliko puta?
	// addFeatureMap(mapsLayer);
	// }
	// }

	public static void fullConectMapLayers(FeatureMapsLayer fromLayer, FeatureMapsLayer toLayer) {
		if (toLayer instanceof ConvolutionLayer) {
			for (int i = 0; i < fromLayer.getNumberOfMaps(); i++) {
				for (int j = 0; j < toLayer.getNumberOfMaps(); j++) {
					connectFeatureMaps(fromLayer, toLayer, i, j);
				}
			}
		} else {
			for (int i = 0; i < toLayer.getNumberOfMaps(); i++) {
				connectFeatureMaps(fromLayer, toLayer, i, i);
			}
		}
	}

	//
	// public static void fullConectMapLayers(FeatureMapsLayer fromLayer,
	// FeatureMapsLayer toLayer) {
	// if (fromLayer instanceof ConvolutionLayer || fromLayer instanceof
	// InputMapLayer) {
	// for (int i = 0; i < fromLayer.getNumberOfMaps(); i++) {
	// for (int j = 0; j < toLayer.getNumberOfMaps(); j++) {
	// connectFeatureMaps(fromLayer, toLayer, i, j);
	// }
	// }
	// } else {
	// for (int i = 0; i < fromLayer.getNumberOfMaps(); i++) {
	// connectFeatureMaps(fromLayer, toLayer, i, i);
	// }
	// }
	// }

	/**
	 * Creates connections between two feature maps
	 * 
	 * @param fromLayer
	 * @param toLayer
	 * @param fromFeatureMapIndex
	 * @param toFeatureMapIndex
	 * 
	 *            TODO: use Layer2D objects here
	 */
	public static void connectFeatureMaps(FeatureMapsLayer fromLayer, FeatureMapsLayer toLayer,
			int fromFeatureMapIndex, int toFeatureMapIndex) {
		Layer2D fromMap = fromLayer.getFeatureMap(fromFeatureMapIndex);
		Layer2D toMap = toLayer.getFeatureMap(toFeatureMapIndex);
		// Kernel kernel = toLayer.getKernel();
		toLayer.connectMaps(fromMap, toMap);

		// if (toLayer instanceof ConvolutionLayer) {
		// connectConvolutionLayerMaps(fromMap, toMap, kernel);
		// } else {
		// connectPoolingLayer(fromMap, toMap, kernel);
		// }

	}

	// private static void connectPoolingLayer(Layer2D fromMap, Layer2D toMap,
	// Kernel kernel) {
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
	// }
	// }
	// }
	// }
	//
	// }
	//
	// /**
	// * Creates connesctions between two convolutional layers
	// * @param fromMap
	// * @param toMap
	// * @param kernel
	// */
	// private static void connectConvolutionLayerMaps(Layer2D fromMap, Layer2D
	// toMap, Kernel kernel) {
	// int numberOfSharedWeights = kernel.getArea();
	// // create shared weights array
	// Weight[] weights = new Weight[numberOfSharedWeights];
	// for (int i = 0; i < numberOfSharedWeights; i++) {
	// Weight weight = new Weight();
	// weight.randomize(-1, 1);
	// weights[i] = weight;
	// }
	//
	// for (int x = 0; x < toMap.getWidth(); x++) {
	// for (int y = 0; y < toMap.getHeight(); y++) {
	// Neuron toNeuron = toMap.getNeuronAt(x, y);
	// for (int dy = 0; dy < kernel.getHeight(); dy++) {
	// for (int dx = 0; dx < kernel.getWidth(); dx++) {
	// int fromX = x + dx;
	// int fromY = y + dy;
	// int currentWeightIndex = dx + dy * kernel.getHeight();
	// Neuron fromNeuron = fromMap.getNeuronAt(fromX, fromY);
	// ConnectionFactory.createConnection(fromNeuron, toNeuron,
	// weights[currentWeightIndex]);
	// }
	// }
	// }
	// }
	// }

	//
	// for (int x = 0; x < fromMap.getWidth()-kernel.getWidth(); x++) { // upper
	// limit changed -kernel.getWidth()
	// for (int y = 0; y < toMap.getHeight()-kernel.getHeight(); y++) { //
	// -kernel.getHeight()
	// Neuron fromNeuron = fromMap.getNeuronAt(x, y);
	// for (int ky = 0; ky < kernel.getHeight(); ky++) {
	// for (int kx = 0; kx < kernel.getWidth(); kx++) {
	// int toX = x + kx;
	// int toY = y + ky;
	// int currentWeightIndex = kx + ky * kernel.getWidth(); // ovde je bilo
	// getHeight
	// Neuron toNeuron = fromMap.getNeuronAt(toX, toY);
	// ConnectionFactory.createConnection(fromNeuron, toNeuron,
	// weights[currentWeightIndex]);
	// }
	// }
	// }
	// }

}
