package org.neuroph.contrib;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.contrib.convolution.CNNFactory;
import org.neuroph.contrib.convolution.CNNFactory.Layer2DType;
import org.neuroph.contrib.convolution.FeatureMap;
import org.neuroph.contrib.convolution.FeatureMapLayer;
import org.neuroph.contrib.convolution.Kernel;
import org.neuroph.contrib.convolution.MapDimension;
import org.neuroph.contrib.convolution.PoolingLayer;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

public class PoolingLayerTest {

	FeatureMapLayer inputLayer;
	MapDimension inputDimension;
	Kernel kernel;

	@Before
	public void setUp() {
		kernel = new Kernel(1, 1);
		inputDimension = new MapDimension(1, 1);
		inputLayer = new PoolingLayer(kernel, inputDimension);
	}

	@Test
	public void testConnectTwoLayersWithOneFeatureMapOne2OneNeuronWithKernel1() {
		inputLayer = CNNFactory.creteInputLayer(inputDimension);
		FeatureMapLayer hiddenLayer = CNNFactory.createNextLayer(inputLayer, kernel, Layer2DType.POOLING);

		CNNFactory.addFeatureMap(inputLayer);
		CNNFactory.addFeatureMap(hiddenLayer);
		CNNFactory.connect(inputLayer, hiddenLayer, 0, 0);

		Neuron fromNeuron = inputLayer.getNeuronAt(0, 0, 0);
		Neuron toNeuron = hiddenLayer.getNeuronAt(0, 0, 0);
		Assert.assertEquals(fromNeuron.getOutConnections()[0], toNeuron.getInputConnections()[0]);
	}

	@Test
	public void testConnectTwoLayersWithOneFeatureMapFour2OneNeuronWithKernel3() {
		kernel = new Kernel(2, 2);
		inputDimension = new MapDimension(2, 2);

		inputLayer = CNNFactory.creteInputLayer(inputDimension);
		FeatureMapLayer hiddenLayer = CNNFactory.createNextLayer(inputLayer, kernel, Layer2DType.POOLING);

		CNNFactory.addFeatureMap(inputLayer);
		CNNFactory.addFeatureMap(hiddenLayer);
		CNNFactory.connect(inputLayer, hiddenLayer, 0, 0);

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
		inputDimension = new MapDimension(4, 4);
		int kernelWidth = 2;
		int kernelHeight = 2;
		kernel = new Kernel(kernelWidth, kernelHeight);

		inputLayer = CNNFactory.creteInputLayer(inputDimension);
		FeatureMapLayer hiddenLayer = CNNFactory.createNextLayer(inputLayer, kernel, Layer2DType.POOLING);

		CNNFactory.addFeatureMap(inputLayer);
		CNNFactory.addFeatureMap(hiddenLayer);
		CNNFactory.connect(inputLayer, hiddenLayer, 0, 0);

		FeatureMap fromMap = inputLayer.getFeatureMap(0);
		FeatureMap toMap = hiddenLayer.getFeatureMap(0);

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
