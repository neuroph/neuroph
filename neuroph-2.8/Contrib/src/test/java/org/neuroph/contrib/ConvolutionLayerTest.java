package org.neuroph.contrib;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.contrib.convolution.ConvolutionUtils;
import org.neuroph.contrib.convolution.ConvolutionUtils.Layer2DType;
import org.neuroph.contrib.convolution.ConvolutionLayer;
import org.neuroph.contrib.convolution.Layer2D;
import org.neuroph.contrib.convolution.FeatureMapsLayer;
import org.neuroph.contrib.convolution.InputMapsLayer;
import org.neuroph.contrib.convolution.Kernel;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.nnet.comp.neuron.InputNeuron;

public class ConvolutionLayerTest {

	FeatureMapsLayer inputLayer;
	Kernel kernel;
	Layer2D.Dimension mapDimension;
	int defaultMapHeight = 1;
	int defaultMapWidth = 1;

	@Before
	public void setUp() {
		kernel = new Kernel(1, 1);
		mapDimension = new Layer2D.Dimension(1, 1);
		inputLayer = new ConvolutionLayer(kernel, mapDimension);
	}

	@Test
	public void testConnectTwoLayersWithOneFeatureMapOne2OneNeuronWithKernel1() {
		kernel = new Kernel(1, 1);
		inputLayer = new InputMapsLayer(mapDimension);
		FeatureMapsLayer hiddenLayer = ConvolutionUtils.createNextLayer(inputLayer, kernel, Layer2DType.CONVOLUTION);

		ConvolutionUtils.addFeatureMap(inputLayer);
		ConvolutionUtils.addFeatureMap(hiddenLayer);
		ConvolutionUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

		Neuron fromNeuron = inputLayer.getNeuronAt(0, 0, 0);
		Neuron toNeuron = hiddenLayer.getNeuronAt(0, 0, 0);
		Assert.assertEquals(fromNeuron.getOutConnections()[0], toNeuron.getInputConnections()[0]);
	}

	@Test
	public void testConnectTwoLayersWithOneFeatureMapFour2OneNeuronWithKernel3() {
		kernel = new Kernel(3, 3);
		Layer2D.Dimension inputDimension = new Layer2D.Dimension(3, 3);
		Layer2D.Dimension hiddenDimension = new Layer2D.Dimension(1, 1);
		inputLayer = new ConvolutionLayer(kernel, inputDimension);
		ConvolutionLayer hiddenLayer = new ConvolutionLayer(kernel, hiddenDimension);
		Layer2D featureMap1 = new Layer2D(inputDimension);
		Layer2D featureMap2 = new Layer2D(hiddenDimension);

		int numberOfInputNeurons = 9;
		double inputNeuronValue = 0;
		List<InputNeuron> inputNeurons = CNNTestUtil.createInputNeurons(numberOfInputNeurons, inputNeuronValue);
		int numberOfHiddenNeurons = 1;
		double hiddenNeuronValue = 0;
		List<Neuron> hiddenNeurons = CNNTestUtil.createNeurons(numberOfHiddenNeurons, hiddenNeuronValue);

		CNNTestUtil.fillFeatureMap(featureMap1, inputNeurons);
		CNNTestUtil.fillFeatureMap(featureMap2, hiddenNeurons);

		inputLayer.addFeatureMap(featureMap1);
		hiddenLayer.addFeatureMap(featureMap2);

		ConvolutionUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

		Set<Weight> weights = new HashSet<Weight>();
		for (Connection connection : hiddenNeurons.get(0).getInputConnections())
			weights.add(connection.getWeight());

		for (int i = 0; i < inputLayer.getNeurons().length; i++) {
			Neuron fromNeuron = inputLayer.getNeurons()[i];
			Assert.assertEquals(fromNeuron, hiddenNeurons.get(0).getInputConnections()[i].getFromNeuron());
		}
		Assert.assertEquals(kernel.getArea(), weights.size());
	}

	@Test
	public void testSharedWeightsTwoLayersWithOneFeatureMap() {
		kernel = new Kernel(3, 3);
		Layer2D.Dimension inputDimension = new Layer2D.Dimension(4, 4);
		Layer2D.Dimension hiddenDimension = new Layer2D.Dimension(2, 2);

		inputLayer = new ConvolutionLayer(kernel, inputDimension);
		ConvolutionLayer hiddenLayer = new ConvolutionLayer(kernel, hiddenDimension);
		Layer2D featureMap1 = new Layer2D(inputDimension);
		Layer2D featureMap2 = new Layer2D(hiddenDimension);

		int numberOfInputNeurons = 16;
		double inputNeuronValue = 0;
		List<InputNeuron> inputNeurons = CNNTestUtil.createInputNeurons(numberOfInputNeurons, inputNeuronValue);
		int numberOfHiddenNeurons = 4;
		double hiddenNeuronValue = 0;
		List<Neuron> hiddenNeurons = CNNTestUtil.createNeurons(numberOfHiddenNeurons, hiddenNeuronValue);

		CNNTestUtil.fillFeatureMap(featureMap1, inputNeurons);
		CNNTestUtil.fillFeatureMap(featureMap2, hiddenNeurons);

		inputLayer.addFeatureMap(featureMap1);
		hiddenLayer.addFeatureMap(featureMap2);

		ConvolutionUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

		for (int x = 0; x < hiddenDimension.getWidth(); x++) {
			for (int y = 0; y < hiddenDimension.getHeight(); y++) {
				Neuron neuron = featureMap2.getNeuronAt(x, y);
				for (int i = 0; i < neuron.getInputConnections().length; i++) {
					int tempX = i % kernel.getWidth();
					int tempY = i / kernel.getHeight();
					Neuron fromNeuron = featureMap1.getNeuronAt(tempX + x, tempY + y);
					Assert.assertEquals(neuron.getInputConnections()[i].getFromNeuron(), fromNeuron);
				}
			}
		}

		int existingNumberOfConnections = 0;
		Set<Weight> weights = new HashSet<Weight>();
		for (Neuron neuron : hiddenNeurons) {
			for (Connection connection : neuron.getInputConnections()) {
				weights.add(connection.getWeight());
				existingNumberOfConnections++;
			}
		}

		int desiredNumberOfConnections = kernel.getArea() * numberOfHiddenNeurons;
		int desiredWeightCount = kernel.getArea();

		Assert.assertEquals(desiredNumberOfConnections, existingNumberOfConnections);
		Assert.assertEquals(desiredWeightCount, weights.size());
	}

}
