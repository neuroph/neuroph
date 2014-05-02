package org.neuroph.contrib;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.nnet.comp.ConvolutionalUtils;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.comp.layer.InputMapsLayer;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.nnet.comp.layer.PoolingLayer;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

public class PoolingLayerTest {

	FeatureMapsLayer inputLayer;
	Layer2D.Dimensions inputDimension;
	Kernel kernel;

	@Before
	public void setUp() {
		kernel = new Kernel(1, 1);
		inputDimension = new Layer2D.Dimensions(1, 1);
	}

	@Test
	public void testConnectTwoLayersWithOneFeatureMapOne2OneNeuronWithKernel1() {
		// given
		inputLayer = new InputMapsLayer(inputDimension, 1);
		FeatureMapsLayer hiddenLayer = new PoolingLayer(inputLayer, kernel);
		Layer2D featureMap = new Layer2D(hiddenLayer.getMapDimensions(), PoolingLayer.DEFAULT_NEURON_PROP);
		hiddenLayer.addFeatureMap(featureMap);

		// when
		ConvolutionalUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

		// then
		Neuron fromNeuron = inputLayer.getNeuronAt(0, 0, 0);
		Neuron toNeuron = hiddenLayer.getNeuronAt(0, 0, 0);
		Assert.assertEquals(fromNeuron.getOutConnections()[0], toNeuron.getInputConnections()[0]);
	}

	@Test
	public void testConnectTwoLayersWithOneFeatureMapFour2OneNeuronWithKernel3() {
		// given
		kernel = new Kernel(2, 2);
		inputDimension = new Layer2D.Dimensions(2, 2);
		inputLayer = new InputMapsLayer(inputDimension, 1);
		FeatureMapsLayer hiddenLayer = new PoolingLayer(inputLayer, kernel);
		Layer2D featureMap = new Layer2D(hiddenLayer.getMapDimensions(), PoolingLayer.DEFAULT_NEURON_PROP);
		hiddenLayer.addFeatureMap(featureMap);

		// when
		ConvolutionalUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

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
		inputDimension = new Layer2D.Dimensions(4, 4);
		int kernelWidth = 2;
		int kernelHeight = 2;
		kernel = new Kernel(kernelWidth, kernelHeight);

		inputLayer = new InputMapsLayer(inputDimension, 1);
		FeatureMapsLayer hiddenLayer = new PoolingLayer(inputLayer, kernel);
		Layer2D featureMap = new Layer2D(hiddenLayer.getMapDimensions(), PoolingLayer.DEFAULT_NEURON_PROP);
		hiddenLayer.addFeatureMap(featureMap);

		// when
		ConvolutionalUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

		//then
		Layer2D fromMap = inputLayer.getFeatureMap(0);
		Layer2D toMap = hiddenLayer.getFeatureMap(0);
		for (int x = 0; x < hiddenLayer.getMapDimensions().getWidth(); x++) {
			for (int y = 0; y < hiddenLayer.getMapDimensions().getHeight(); y++) {
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
