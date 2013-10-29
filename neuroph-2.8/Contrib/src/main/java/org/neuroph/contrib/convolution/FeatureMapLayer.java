package org.neuroph.contrib.convolution;

import java.util.Arrays;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.util.NeuronProperties;

public abstract class FeatureMapLayer extends Layer {

	private static final long serialVersionUID = -6706741997689639209L;
	private FeatureMap[] featureMaps;
	// private allNeurons;

	protected Kernel kernel;
	protected MapDimension dimension;

	public FeatureMapLayer(Kernel kernel, MapDimension dimension) {
		this.dimension = dimension;
		this.kernel = kernel;
		featureMaps = new FeatureMap[0];
		// allNeurons = new Neuron[0];
	}

	public void addFeatureMap(FeatureMap featureMap) {
		if (featureMap == null) {
			throw new NeurophException("FeatureMap cant be null!");
		}
		featureMaps = Arrays.copyOf(featureMaps, featureMaps.length + 1);
		featureMaps[featureMaps.length - 1] = featureMap;

		// Copy map to the existing container!!!
		int totalNeuronCount = 0;
		for (FeatureMap map : featureMaps)
			totalNeuronCount += map.getNeuronsCount();

		Neuron[] allNeurons = new Neuron[totalNeuronCount];
		int currentPosition = 0;
		for (int i = 0; i < featureMaps.length; i++) {
			FeatureMap map = featureMaps[i];
			System.arraycopy(map.getNeurons(), 0, allNeurons, currentPosition, map.getNeuronsCount());
			currentPosition += map.getNeuronsCount();
		}
		neurons = allNeurons;

	}

	public FeatureMap getFeatureMap(int index) {
		return featureMaps[index];
	}

	public FeatureMap[] getFeatureMaps() {
		return featureMaps;
	}

	public int getNumberOfMaps() {
		return featureMaps.length;
	}

	public Neuron getNeuronAt(int x, int y, int mapIndex) {
		FeatureMap map = featureMaps[mapIndex];
		return map.getNeuronAt(x, y);
	}

	@Override
	public int getNeuronsCount() {
		int neuronCount = 0;
		for (FeatureMap map : featureMaps)
			neuronCount += map.getNeuronsCount();
		return neuronCount;
	}

	@Override
	public void calculate() {
		for (FeatureMap map : featureMaps) {
			map.calculate();
		}
	}

	public Kernel getKernel() {
		return kernel;
	}

	public MapDimension getDimension() {
		return dimension;
	}

	public abstract NeuronProperties getNeuronProperties();

}
