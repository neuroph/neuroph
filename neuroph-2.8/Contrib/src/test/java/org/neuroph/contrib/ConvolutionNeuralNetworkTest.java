package org.neuroph.contrib;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.contrib.convolution.CNNFactory;
import org.neuroph.contrib.convolution.CNNFactory.Layer2DType;
import org.neuroph.contrib.convolution.ConvolutionLayer;
import org.neuroph.contrib.convolution.ConvolutionNeuralNetwork;
import org.neuroph.contrib.convolution.FeatureMap;
import org.neuroph.contrib.convolution.FeatureMapLayer;
import org.neuroph.contrib.convolution.Kernel;
import org.neuroph.contrib.convolution.MapDimension;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

public class ConvolutionNeuralNetworkTest {

	ConvolutionNeuralNetwork network;
	FeatureMapLayer inputLayer;
	Kernel kernel;
	MapDimension mapDimension;

	@Before
	public void setUp() {
		network = new ConvolutionNeuralNetwork();
		mapDimension = new MapDimension(1, 1);
		kernel = new Kernel(1, 1);
		inputLayer = new ConvolutionLayer(kernel, mapDimension);
	}

	@Test
	public void testCreateOneLayerWitihEmptyFeatureMap() {
		FeatureMap featureMap = new FeatureMap(mapDimension);

		inputLayer.addFeatureMap(featureMap);
		network.addLayer(inputLayer);

		// This line of code is meaningless... It should be implicit that if we
		// add layer 0, that input neuron are inside that layer
		network.setInputNeurons(inputLayer.getNeurons());

		Assert.assertEquals(0, network.getInputsCount());
	}

	@Test
	public void testCreateOneLayerWithOneFeatureMapWithManyNeurons() {
		mapDimension = new MapDimension(4, 4);
		FeatureMap featureMap = new FeatureMap(mapDimension);

		int numberOfInputNeurons = 4;
		double inputNeuronValue = 0;
		List<InputNeuron> inputNeurons = CNNTestUtil.createInputNeurons(numberOfInputNeurons, inputNeuronValue);
		CNNTestUtil.fillFeatureMap(featureMap, inputNeurons);

		inputLayer.addFeatureMap(featureMap);
		network.addLayer(inputLayer);

		// This line of code is meaningless
		network.setInputNeurons(inputLayer.getNeurons());

		Assert.assertEquals(4, network.getInputsCount());
	}

	@Test
	public void testOutputValuesOneLayerOneFeatureMap() {
		mapDimension = new MapDimension(4, 4);
		FeatureMap featureMap = new FeatureMap(mapDimension);

		int numberOfInputNeurons = 4;
		double inputNeuronValue = 1;
		List<InputNeuron> inputNeurons = CNNTestUtil.createInputNeurons(numberOfInputNeurons, inputNeuronValue);
		CNNTestUtil.fillFeatureMap(featureMap, inputNeurons);

		inputLayer.addFeatureMap(featureMap);
		network.addLayer(inputLayer);
		network.calculate();

		// This line of code is meaningless
		network.setInputNeurons(inputLayer.getNeurons());

		for (Neuron neuron : network.getInputNeurons()) {
			Assert.assertEquals(inputNeuronValue, neuron.getOutput(), 0.001);
		}
	}

	@Test
	public void testCreateOneLayerWithManyEmptyFeatureMaps() {
		FeatureMap featureMap1 = new FeatureMap(mapDimension);
		FeatureMap featureMap2 = new FeatureMap(mapDimension);
		inputLayer.addFeatureMap(featureMap1);
		inputLayer.addFeatureMap(featureMap2);
		network.addLayer(inputLayer);

		// This line of code is meaningless... It should be implicit that if we
		// add layer 0, that input neuron are inside that layer
		network.setInputNeurons(inputLayer.getNeurons());

		Assert.assertEquals(0, network.getInputsCount());
	}

	@Test
	public void testCreateOneLayerWithManyFeatureMapsWithManyNeurons() {
		mapDimension = new MapDimension(3, 5);
		FeatureMap featureMap1 = new FeatureMap(mapDimension);
		FeatureMap featureMap2 = new FeatureMap(mapDimension);
		InputNeuron inputNeuron1 = new InputNeuron();
		inputNeuron1.setInput(1);
		InputNeuron inputNeuron2 = new InputNeuron();
		inputNeuron2.setInput(2);
		InputNeuron inputNeuron3 = new InputNeuron();
		inputNeuron3.setInput(3);
		InputNeuron inputNeuron4 = new InputNeuron();
		inputNeuron4.setInput(4);
		featureMap1.addNeuron(inputNeuron1);
		featureMap1.addNeuron(inputNeuron2);
		featureMap1.addNeuron(inputNeuron3);
		featureMap2.addNeuron(inputNeuron4);
		inputLayer.addFeatureMap(featureMap1);
		inputLayer.addFeatureMap(featureMap2);
		network.addLayer(inputLayer);

		// This line of code is meaningless
		network.setInputNeurons(inputLayer.getNeurons());

		Assert.assertEquals(4, network.getInputsCount());
	}

	@Test
	public void testOutputValuesOneLayerManyFeatureMaps() {
		mapDimension = new MapDimension(3, 5);
		FeatureMap featureMap1 = new FeatureMap(mapDimension);
		FeatureMap featureMap2 = new FeatureMap(mapDimension);
		FeatureMap featureMap3 = new FeatureMap(mapDimension);
		InputNeuron inputNeuron1 = new InputNeuron();
		inputNeuron1.setInput(1);
		InputNeuron inputNeuron2 = new InputNeuron();
		inputNeuron2.setInput(2);
		InputNeuron inputNeuron3 = new InputNeuron();
		inputNeuron3.setInput(3);
		InputNeuron inputNeuron4 = new InputNeuron();
		inputNeuron4.setInput(4);
		featureMap1.addNeuron(inputNeuron1);
		featureMap1.addNeuron(inputNeuron2);
		featureMap2.addNeuron(inputNeuron3);
		featureMap3.addNeuron(inputNeuron4);
		inputLayer.addFeatureMap(featureMap1);
		inputLayer.addFeatureMap(featureMap2);
		inputLayer.addFeatureMap(featureMap3);
		network.addLayer(inputLayer);
		network.calculate();

		// This line of code is meaningless
		network.setInputNeurons(inputLayer.getNeurons());

		Assert.assertEquals(1.0, network.getInputNeurons()[0].getOutput(), 0.001);
		Assert.assertEquals(2.0, network.getInputNeurons()[1].getOutput(), 0.001);
		Assert.assertEquals(3.0, network.getInputNeurons()[2].getOutput(), 0.001);
		Assert.assertEquals(4.0, network.getInputNeurons()[3].getOutput(), 0.001);
	}

	@Test
	public void testMapLayerCreationWithOneMap() {
		FeatureMapLayer inputLayer = CNNFactory.creteInputLayer(new MapDimension(10, 10));
		FeatureMapLayer hiddenLayer1 = CNNFactory.createNextLayer(inputLayer, new Kernel(5, 5), Layer2DType.CONVOLUTION);
		FeatureMapLayer hiddenLayer2 = CNNFactory.createNextLayer(hiddenLayer1, new Kernel(2, 2), Layer2DType.POOLING);
		FeatureMapLayer outputLayer = CNNFactory.createNextLayer(hiddenLayer2, new Kernel(3, 3), Layer2DType.CONVOLUTION);

		CNNFactory.addFeatureMap(inputLayer);
		CNNFactory.addFeatureMap(hiddenLayer1);
		CNNFactory.addFeatureMap(hiddenLayer2);
		CNNFactory.addFeatureMap(outputLayer);

		Assert.assertEquals(100, inputLayer.getNeurons().length);
		Assert.assertEquals(36, hiddenLayer1.getNeurons().length);
		Assert.assertEquals(9, hiddenLayer2.getNeurons().length);
		Assert.assertEquals(1, outputLayer.getNeurons().length);
	}

	@Test
	public void testMapLayerCreationWithManyMaps() {
		FeatureMapLayer inputLayer = CNNFactory.creteInputLayer(new MapDimension(10, 10));
		FeatureMapLayer hiddenLayer1 = CNNFactory.createNextLayer(inputLayer, new Kernel(5, 5), Layer2DType.CONVOLUTION);
		FeatureMapLayer hiddenLayer2 = CNNFactory.createNextLayer(hiddenLayer1, new Kernel(2, 2), Layer2DType.POOLING);
		FeatureMapLayer outputLayer = CNNFactory.createNextLayer(hiddenLayer2, new Kernel(3, 3), Layer2DType.CONVOLUTION);

		CNNFactory.addFeatureMaps(inputLayer, 3);
		CNNFactory.addFeatureMaps(hiddenLayer1, 3);
		CNNFactory.addFeatureMaps(hiddenLayer2, 3);
		CNNFactory.addFeatureMaps(outputLayer, 3);

		Assert.assertEquals(300, inputLayer.getNeurons().length);
		Assert.assertEquals(108, hiddenLayer1.getNeurons().length);
		Assert.assertEquals(27, hiddenLayer2.getNeurons().length);
		Assert.assertEquals(3, outputLayer.getNeurons().length);
	}

}
