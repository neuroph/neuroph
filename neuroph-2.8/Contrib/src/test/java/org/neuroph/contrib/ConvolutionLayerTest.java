package org.neuroph.contrib;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.nnet.comp.layer.ConvolutionalLayer;
import org.neuroph.nnet.comp.ConvolutionalUtils;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.comp.layer.InputMapsLayer;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.nnet.comp.layer.Layer2D.Dimensions;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

public class ConvolutionLayerTest {

	FeatureMapsLayer inputLayer;
	Kernel convolutionKernel;
	Layer2D.Dimensions inputDimension;
	int inputMapIndex = 0;
	int hiddenMapIndex = 0;

	@Before
	public void setUp() {
		convolutionKernel = new Kernel(1, 1);
		inputDimension = new Layer2D.Dimensions(1, 1);
		inputLayer = new InputMapsLayer(inputDimension, 1);
	}

	@Test
	public void testConnectTwoLayersWithOneFeatureMapOne2OneNeuronWithKernel1() {
		// given
		convolutionKernel = new Kernel(1, 1);
		inputLayer = new InputMapsLayer(inputDimension, 1);
		FeatureMapsLayer hiddenLayer = new ConvolutionalLayer(inputLayer, convolutionKernel);
		Layer2D hiddenFeatureMap = new Layer2D(hiddenLayer.getMapDimensions(), ConvolutionalLayer.DEFAULT_NEURON_PROP);
		hiddenLayer.addFeatureMap(hiddenFeatureMap);

		// when
		int inputMapIndex = 0;
		int hiddenMapIndex = 0;
		ConvolutionalUtils.connectFeatureMaps(inputLayer, hiddenLayer, inputMapIndex, hiddenMapIndex);

		// then
		int xCord = 0;
		int yCord = 0;
		Neuron fromNeuron = inputLayer.getNeuronAt(xCord, yCord, inputMapIndex);
		Neuron toNeuron = hiddenLayer.getNeuronAt(xCord, yCord, hiddenMapIndex);
		Assert.assertEquals(fromNeuron.getOutConnections()[0], toNeuron.getInputConnections()[0]);
	}

	@Test
	public void testConnectTwoLayersWithOneFeatureMapFour2OneNeuronWithKernel3() {
		// given
/*		inputDimension = new Layer2D.Dimensions(3, 3);
		inputLayer = new InputMapsLayer(inputDimension, 1);
		convolutionKernel = new Kernel(3, 3);
		FeatureMapsLayer hiddenLayer = new ConvolutionalLayer(inputLayer, convolutionKernel);
		Layer2D hiddenFeatureMap = new Layer2D(hiddenLayer.getMapDimensions());

		CNNTestUtil.fillFeatureMapWithNeurons(hiddenFeatureMap);
		hiddenLayer.addFeatureMap(hiddenFeatureMap);

		// when
		ConvolutionalUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

		// then
		Neuron[] hiddenNeurons = hiddenFeatureMap.getNeurons();
		Set<Weight> weights = new HashSet<Weight>();
		for (Connection connection : hiddenNeurons[0].getInputConnections())
			weights.add(connection.getWeight());

		for (int i = 0; i < inputLayer.getNeurons().length; i++) {
			Neuron fromNeuron = inputLayer.getNeurons()[i];
			Assert.assertEquals(fromNeuron, hiddenNeurons[0].getInputConnections()[i].getFromNeuron());
		}
		Assert.assertEquals(convolutionKernel.getArea(), weights.size());
            */
	}

	@Test
	public void testSharedWeightsTwoLayersWithOneFeatureMap() {
/*
		// given
		convolutionKernel = new Kernel(3, 3);
		inputDimension = new Layer2D.Dimensions(4, 4);
		inputLayer = new InputMapsLayer(inputDimension, 1);
		FeatureMapsLayer hiddenLayer = new ConvolutionalLayer(inputLayer, convolutionKernel);
		Layer2D hiddenFeatureMap = new Layer2D(hiddenLayer.getMapDimensions());

		CNNTestUtil.fillFeatureMapWithNeurons(hiddenFeatureMap);
		hiddenLayer.addFeatureMap(hiddenFeatureMap);

		// when
		ConvolutionalUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

		// then
		Dimensions hiddenDimension = hiddenLayer.getMapDimensions();
		Neuron[] hiddenNeurons = hiddenFeatureMap.getNeurons();

		int existingNumberOfConnections = 0;
		Set<Weight> weights = new HashSet<Weight>();
		for (Neuron neuron : hiddenNeurons) {
			for (Connection connection : neuron.getInputConnections()) {
				weights.add(connection.getWeight());
				existingNumberOfConnections++;
			}
		}

		int desiredNumberOfConnections = convolutionKernel.getArea()
				* (hiddenDimension.getWidth() * hiddenDimension.getHeight());
		int desiredNumberOfWeights = convolutionKernel.getArea();

		Assert.assertEquals(desiredNumberOfConnections, existingNumberOfConnections);
		Assert.assertEquals(desiredNumberOfWeights, weights.size());

		Layer2D inputFeatureMap = inputLayer.getFeatureMap(0);
		for (int x = 0; x < hiddenDimension.getWidth(); x++) {
			for (int y = 0; y < hiddenDimension.getHeight(); y++) {
				Neuron neuron = hiddenFeatureMap.getNeuronAt(x, y);
				for (int i = 0; i < neuron.getInputConnections().length; i++) {
					int tempX = i % convolutionKernel.getWidth();
					int tempY = i / convolutionKernel.getHeight();
					Neuron fromNeuron = inputFeatureMap.getNeuronAt(tempX + x, tempY + y);
					Assert.assertEquals(neuron.getInputConnections()[i].getFromNeuron(), fromNeuron);
				}
			}
		}
*/
	}

}
