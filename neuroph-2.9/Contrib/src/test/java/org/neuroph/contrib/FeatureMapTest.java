package org.neuroph.contrib;

import org.junit.Assert;
import org.junit.Test;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.nnet.comp.neuron.InputNeuron;

public class FeatureMapTest {

	@Test
	public void testEmptyFeatureMap() {
		Layer2D.Dimensions dimension = new Layer2D.Dimensions(0, 0);
		Layer2D featureMap = new Layer2D(dimension);
		Assert.assertEquals(0, featureMap.getNeuronsCount());
		Assert.assertEquals(0, featureMap.getHeight());
		Assert.assertEquals(0, featureMap.getWidth());
	}

	@Test
	public void testFeatureMapWithOneNeuron() {
		Layer2D.Dimensions dimension = new Layer2D.Dimensions(4, 3);
		Layer2D featureMap = new Layer2D(dimension);
		InputNeuron inputNeuron = new InputNeuron();
		inputNeuron.setInput(1);
		featureMap.addNeuron(inputNeuron);

		Assert.assertEquals(1, featureMap.getNeuronsCount());
		Assert.assertEquals(dimension.getWidth(), featureMap.getWidth());
		Assert.assertEquals(dimension.getHeight(), featureMap.getHeight());
	}

	@Test
	public void testFeatureMapWithManyNeurons() {
		Layer2D.Dimensions dimension = new Layer2D.Dimensions(4, 3);
		Layer2D featureMap = new Layer2D(dimension);
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
