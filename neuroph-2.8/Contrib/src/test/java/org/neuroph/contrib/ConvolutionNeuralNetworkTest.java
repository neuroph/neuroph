package org.neuroph.contrib;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.nnet.comp.layer.ConvolutionalLayer;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.comp.layer.InputMapsLayer;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.nnet.comp.layer.PoolingLayer;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

public class ConvolutionNeuralNetworkTest {

	ConvolutionalNetwork network;
	FeatureMapsLayer inputLayer;
	Kernel kernel;
	Layer2D.Dimensions inputDimension;

	@Before
	public void setUp() {
		network = new ConvolutionalNetwork();
		inputDimension = new Layer2D.Dimensions(1, 1);
		kernel = new Kernel(1, 1);
		inputLayer = new InputMapsLayer(inputDimension, 1);
	}

	@Test
	public void testCreateOneLayerWitihEmptyFeatureMap() {
		Layer2D featureMap = new Layer2D(inputDimension);
                inputLayer = new InputMapsLayer(inputDimension, 0);
		inputLayer.addFeatureMap(featureMap);
		network.addLayer(inputLayer);

		// This line of code is meaningless... It should be implicit that if we
		// add layer 0, that input neuron are inside that layer
		network.setInputNeurons(inputLayer.getNeurons());

		Assert.assertEquals(0, network.getInputsCount());
	}

	@Test
	public void testCreateOneLayerWithOneFeatureMapWithManyNeurons() {
		inputDimension = new Layer2D.Dimensions(4, 4);
		Layer2D featureMap = new Layer2D(inputDimension);

		CNNTestUtil.fillFeatureMapWithNeurons(featureMap);

		inputLayer.addFeatureMap(featureMap);
		network.addLayer(inputLayer);

		// This line of code is meaningless
		network.setInputNeurons(inputLayer.getNeurons());

		Assert.assertEquals(17, network.getInputsCount());
	}

	@Test
	public void testOutputValuesOneLayerOneFeatureMap() {
		inputDimension = new Layer2D.Dimensions(4, 4);
                inputLayer = new InputMapsLayer(inputDimension, 0);                
		Layer2D featureMap = new Layer2D(inputDimension);

		double inputNeuronValue = 1;
		CNNTestUtil.fillFeatureMapWithNeurons(featureMap, inputNeuronValue);

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
                inputLayer = new InputMapsLayer(inputDimension, 0);            
		Layer2D featureMap1 = new Layer2D(inputDimension);
		Layer2D featureMap2 = new Layer2D(inputDimension);
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
		inputDimension = new Layer2D.Dimensions(3, 5);
                inputLayer = new InputMapsLayer(inputDimension, 0);
		Layer2D featureMap1 = new Layer2D(inputDimension);
		Layer2D featureMap2 = new Layer2D(inputDimension);
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
		inputDimension = new Layer2D.Dimensions(3, 5);
                inputLayer = new InputMapsLayer(inputDimension, 0);
		Layer2D featureMap1 = new Layer2D(inputDimension);
		Layer2D featureMap2 = new Layer2D(inputDimension);
		Layer2D featureMap3 = new Layer2D(inputDimension);
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
		FeatureMapsLayer inputLayer = new InputMapsLayer(new Layer2D.Dimensions(10, 10), 1);
		FeatureMapsLayer hiddenLayer1 = new ConvolutionalLayer(inputLayer, new Kernel(5, 5));
		FeatureMapsLayer hiddenLayer2 = new PoolingLayer(hiddenLayer1, new Kernel(2, 2));
		FeatureMapsLayer outputLayer = new ConvolutionalLayer(hiddenLayer2, new Kernel(3, 3));

//		ConvolutionUtils.addFeatureMap(inputLayer);
//		ConvolutionUtils.addFeatureMap(hiddenLayer1);
//		ConvolutionUtils.addFeatureMap(hiddenLayer2);
//		ConvolutionUtils.addFeatureMap(outputLayer);

		Assert.assertEquals(100, inputLayer.getNeurons().length);
		Assert.assertEquals(36, hiddenLayer1.getNeurons().length);
		Assert.assertEquals(9, hiddenLayer2.getNeurons().length);
		Assert.assertEquals(1, outputLayer.getNeurons().length);
	}

	@Test
	public void testMapLayerCreationWithManyMaps() {
		FeatureMapsLayer inputLayer = new InputMapsLayer(new Layer2D.Dimensions(10, 10), 1);
		FeatureMapsLayer hiddenLayer1 = new ConvolutionalLayer(inputLayer, new Kernel(5, 5));
		FeatureMapsLayer hiddenLayer2 = new PoolingLayer(hiddenLayer1, new Kernel(2, 2));
		FeatureMapsLayer outputLayer = new ConvolutionalLayer(hiddenLayer2, new Kernel(3, 3));
		
//		ConvolutionUtils.addFeatureMaps(inputLayer, 3);
//		ConvolutionUtils.addFeatureMaps(hiddenLayer1, 3);
//		ConvolutionUtils.addFeatureMaps(hiddenLayer2, 3);
//		ConvolutionUtils.addFeatureMaps(outputLayer, 3);

		Assert.assertEquals(100, inputLayer.getNeurons().length);
		Assert.assertEquals(36, hiddenLayer1.getNeurons().length);
		Assert.assertEquals(9, hiddenLayer2.getNeurons().length);
		Assert.assertEquals(1, outputLayer.getNeurons().length);
	}

}
