package org.neuroph.contrib;

import org.junit.Assert;
import org.junit.Test;
import org.neuroph.contrib.convolution.FeatureMap;
import org.neuroph.contrib.convolution.MapDimension;
import org.neuroph.nnet.comp.neuron.InputNeuron;

public class FeatureMapTest {

	@Test
	public void testEmptyFeatureMap() {
		MapDimension dimension = new MapDimension(0, 0);
		FeatureMap featureMap = new FeatureMap(dimension);
		Assert.assertEquals(0, featureMap.getNeuronsCount());
		Assert.assertEquals(0, featureMap.getHeight());
		Assert.assertEquals(0, featureMap.getWidth());
	}

	@Test
	public void testFeatureMapWithOneNeuron() {
		MapDimension dimension = new MapDimension(4, 3);
		FeatureMap featureMap = new FeatureMap(dimension);
		InputNeuron inputNeuron = new InputNeuron();
		inputNeuron.setInput(1);
		featureMap.addNeuron(inputNeuron);

		Assert.assertEquals(1, featureMap.getNeuronsCount());
		Assert.assertEquals(dimension.getWidth(), featureMap.getWidth());
		Assert.assertEquals(dimension.getHeight(), featureMap.getHeight());
	}

	@Test
	public void testFeatureMapWithManyNeurons() {
		MapDimension dimension = new MapDimension(4, 3);
		FeatureMap featureMap = new FeatureMap(dimension);
		InputNeuron inputNeuron1 = new InputNeuron();
		inputNeuron1.setInput(1);
		InputNeuron inputNeuron2 = new InputNeuron();
		inputNeuron2.setInput(2);
		InputNeuron inputNeuron3 = new InputNeuron();
		inputNeuron3.setInput(3);
		InputNeuron inputNeuron4 = new InputNeuron();
		inputNeuron4.setInput(4);
		featureMap.addNeuron(inputNeuron1);
		featureMap.addNeuron(inputNeuron2);
		featureMap.addNeuron(inputNeuron3);
		featureMap.addNeuron(inputNeuron4);

		Assert.assertEquals(4, featureMap.getNeuronsCount());
	}

}
