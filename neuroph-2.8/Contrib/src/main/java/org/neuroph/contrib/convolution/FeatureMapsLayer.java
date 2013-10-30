package org.neuroph.contrib.convolution;

import java.util.Arrays;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.util.NeuronProperties;

public class FeatureMapsLayer extends Layer {

	private static final long serialVersionUID = -6706741997689639209L;
        
        /**
         * 
         */
	protected Kernel kernel;

        /**
         * 
         */
        protected Layer2D.Dimension mapDimension;        
        
        /**
         * 
         */
        private Layer2D[] featureMaps;        


        
	public FeatureMapsLayer(Kernel kernel) {
		this.kernel = kernel;
		featureMaps = new Layer2D[0];
	}        
        
        /**
         * 
         * @param kernel kernel used for all feature maps in this layer
         * @param mapDimension mapDimension of feature maps  in this layer
         */
	public FeatureMapsLayer(Kernel kernel,  Layer2D.Dimension dimension) {
		this.mapDimension = dimension;
		this.kernel = kernel;
		featureMaps = new Layer2D[0];
	}

	public void addFeatureMap(Layer2D featureMap) {
		if (featureMap == null) {
			throw new NeurophException("FeatureMap cant be null!");
		}
		featureMaps = Arrays.copyOf(featureMaps, featureMaps.length + 1);
		featureMaps[featureMaps.length - 1] = featureMap;

		
		//Copy map to the existing container!!!
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

	public Layer2D getFeatureMap(int index) {
		return featureMaps[index];
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

	public  Layer2D.Dimension getDimension() {
		return mapDimension;
	}

        

}
