package org.neuroph.contrib;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.contrib.convolution.ConvolutionUtils;
import org.neuroph.contrib.convolution.FeatureMapsLayer;
import org.neuroph.contrib.convolution.InputMapsLayer;
import org.neuroph.contrib.convolution.Kernel;
import org.neuroph.contrib.convolution.Layer2D;
import org.neuroph.contrib.convolution.PoolingLayer;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

public class PoolingLayerTest {

	FeatureMapsLayer inputLayer;
	Layer2D.Dimension inputDimension;
	Kernel kernel;

	@Before
	public void setUp() {
		kernel = new Kernel(1, 1);
		inputDimension = new Layer2D.Dimension(1, 1);
	}

	@Test
	public void testConnectTwoLayersWithOneFeatureMapOne2OneNeuronWithKernel1() {
		// given
		inputLayer = new InputMapsLayer(inputDimension);
		FeatureMapsLayer hiddenLayer = new PoolingLayer(inputLayer, kernel);
		Layer2D featureMap = new Layer2D(hiddenLayer.getDimension(), PoolingLayer.neuronProperties);
		hiddenLayer.addFeatureMap(featureMap);

		// when
		ConvolutionUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

		// then
		Neuron fromNeuron = inputLayer.getNeuronAt(0, 0, 0);
		Neuron toNeuron = hiddenLayer.getNeuronAt(0, 0, 0);
		Assert.assertEquals(fromNeuron.getOutConnections()[0], toNeuron.getInputConnections()[0]);
	}

	@Test
	public void testConnectTwoLayersWithOneFeatureMapFour2OneNeuronWithKernel3() {
		// given
		kernel = new Kernel(2, 2);
		inputDimension = new Layer2D.Dimension(2, 2);
		inputLayer = new InputMapsLayer(inputDimension);
		FeatureMapsLayer hiddenLayer = new PoolingLayer(inputLayer, kernel);
		Layer2D featureMap = new Layer2D(hiddenLayer.getDimension(), PoolingLayer.neuronProperties);
		hiddenLayer.addFeatureMap(featureMap);

		// when
		ConvolutionUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

		//then
		Neuron pooledNeuron = hiddenLayer.getNeurons()[0];
		Set<Weight> weights = new HashSet<Weight>();
		for (Connection connection : pooledNeuron.getInputConnections())
			weights.add(connection.getWeight());

		for (int i = 0; i < inputLayer.getNeurons().length; i++) {
			Neuron fromNeuron = inputLayer.getNeurons()[i];
			Assert.assertEquals(fromNeuron, pooledNeuron.getInputConnections()[i].getFromNeuron());
		}
		Assert.assertEquals(1, weights.size());
	}

	@Test
	public void testPoolingTwoLayersWithOneFeatureMap() {
		//given
		inputDimension = new Layer2D.Dimension(4, 4);
		int kernelWidth = 2;
		int kernelHeight = 2;
		kernel = new Kernel(kernelWidth, kernelHeight);

		inputLayer = new InputMapsLayer(inputDimension);
		FeatureMapsLayer hiddenLayer = new PoolingLayer(inputLayer, kernel);
		Layer2D featureMap = new Layer2D(hiddenLayer.getDimension(), PoolingLayer.neuronProperties);
		hiddenLayer.addFeatureMap(featureMap);

		// when
		ConvolutionUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

		//then
		Layer2D fromMap = inputLayer.getFeatureMap(0);
		Layer2D toMap = hiddenLayer.getFeatureMap(0);
		for (int x = 0; x < hiddenLayer.getDimension().getWidth(); x++) {
			for (int y = 0; y < hiddenLayer.getDimension().getHeight(); y++) {
				Neuron toNeuron = toMap.getNeuronAt(x, y);
				for (int i = 0; i < toNeuron.getInputConnections().length; i++) {
					int tempX = i % kernelWidth;
					int tempY = i / kernelHeight;
					Neuron fromNeuron = fromMap.getNeuronAt(tempX + (kernelWidth * x), tempY + (kernelHeight * y));
					Assert.assertEquals(toNeuron.getInputConnections()[i].getFromNeuron(), fromNeuron);
				}
			}
		}

		Set<Weight> weights = new HashSet<Weight>();
		for (Neuron neuron : hiddenLayer.getNeurons()) {
			for (Connection connection : neuron.getInputConnections()) {
				weights.add(connection.getWeight());
			}
		}

		int desiredTotalConnections = 4;
		Assert.assertEquals(desiredTotalConnections, weights.size());
		Assert.assertEquals(kernelHeight * kernelWidth, weights.size());
	}

}
