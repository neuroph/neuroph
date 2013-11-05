package org.neuroph.contrib.convolution;

import java.util.Arrays;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.exceptions.NeurophException;

/**
 * This class represents an array of 2 dimensional layers (Layer2D instances)
 * and it is base class for Convolution and Pooling layers.
 * 
 * @author Boris Fulurija
 * @author Zoran Sevarac
 */
public abstract class FeatureMapsLayer extends Layer {


	private static final long serialVersionUID = -6706741997689639209L;

	/**
	 * Kernel used for all 2D layers (feature maps)
	 */
	protected Kernel kernel;

	/**
	 * Dimension for all 2D layers (feature maps)
	 */
	protected Layer2D.Dimension mapDimension;

	/**
	 * Array of feature maps (instances of Layer2D)
	 */
	private Layer2D[] featureMaps;

	public FeatureMapsLayer(Kernel kernel) {
		this.kernel = kernel;
		featureMaps = new Layer2D[0];
	}

	/**
	 * 
	 * @param kernel
	 *            kernel used for all feature maps in this layer
	 * @param inputDimension
	 *            mapDimension of feature maps in this layer
	 */
	public FeatureMapsLayer(Kernel kernel, Layer2D.Dimension dimension) {
		this.mapDimension = dimension;
		this.kernel = kernel;
		featureMaps = new Layer2D[0];
	}

	/**
	 * Adds feature map (2d layer) to this layer
	 * 
	 * @param featureMap
	 *            to add
	 */
	public void addFeatureMap(Layer2D featureMap) {
		if (featureMap == null) {
			throw new NeurophException("FeatureMap cant be null!");
		}
		featureMaps = Arrays.copyOf(featureMaps, featureMaps.length + 1);
		featureMaps[featureMaps.length - 1] = featureMap;

		// Copy map to the existing container!!!
		int totalNeuronCount = 0;
		for (Layer2D map : featureMaps)
			totalNeuronCount += map.getNeuronsCount();

		Neuron[] allNeurons = new Neuron[totalNeuronCount];
		int currentPosition = 0;
		for (int i = 0; i < featureMaps.length; i++) {
			Layer2D map = featureMaps[i];
			System.arraycopy(map.getNeurons(), 0, allNeurons, currentPosition, map.getNeuronsCount());
			currentPosition += map.getNeuronsCount();
		}
		neurons = allNeurons;

	}

        /**
         * Retruns feature map (Layer2D) at specified index
         * @param index index of feature map
         * @return feature map (Layer2D) at specified index
         */
	public Layer2D getFeatureMap(int index) {
		return featureMaps[index];
	}

        
        public void setFeatureMaps(Layer2D[] featureMaps) {
            this.featureMaps = featureMaps;
        }
        
        
        

	public int getNumberOfMaps() {
		return featureMaps.length;
	}

	public Neuron getNeuronAt(int x, int y, int mapIndex) {
		Layer2D map = featureMaps[mapIndex];
		return map.getNeuronAt(x, y);
	}

	@Override
	public int getNeuronsCount() {
		int neuronCount = 0;
		for (Layer2D map : featureMaps)
			neuronCount += map.getNeuronsCount();
		return neuronCount;
	}

	@Override
	public void calculate() {
		for (Layer2D map : featureMaps) {
			map.calculate();
		}
	}

	public Kernel getKernel() {
		return kernel;
	}

	public Layer2D.Dimension getDimension() {
		return mapDimension;
	}
	
	public abstract void connectMaps(Layer2D fromMap, Layer2D toMap);

}
