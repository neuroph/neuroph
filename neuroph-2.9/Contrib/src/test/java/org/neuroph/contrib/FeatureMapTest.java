package org.neuroph.contrib;

import org.junit.Assert;
import org.junit.Test;
import org.neuroph.nnet.comp.Dimension2D;
import org.neuroph.nnet.comp.layer.FeatureMapLayer;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.NeuronProperties;

public class FeatureMapTest {

	@Test
	public void testEmptyFeatureMap() {
		Dimension2D dimension = new Dimension2D(0, 0);
		FeatureMapLayer featureMap = new FeatureMapLayer(dimension, new NeuronProperties());
		Assert.assertEquals(0, featureMap.getNeuronsCount());
		Assert.assertEquals(0, featureMap.getHeight());
		Assert.assertEquals(0, featureMap.getWidth());
	}

	@Test
	public void testFeatureMapWithOneNeuron() {
		Dimension2D dimension = new Dimension2D(4, 3);
		FeatureMapLayer featureMap = new FeatureMapLayer(dimension, new NeuronProperties());
		InputNeuron inputNeuron = new InputNeuron();
		inputNeuron.setInput(1);
		featureMap.addNeuron(inputNeuron);

		Assert.assertEquals(1, featureMap.getNeuronsCount());
		Assert.assertEquals(dimension.getWidth(), featureMap.getWidth());
		Assert.assertEquals(dimension.getHeight(), featureMap.getHeight());
	}

	@Test
	public void testFeatureMapWithManyNeurons() {
		Dimension2D dimension = new Dimension2D(4, 3);
		FeatureMapLayer featureMap = new FeatureMapLayer(dimension, new NeuronProperties());
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
