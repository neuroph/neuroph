package org.neuroph.contrib;

import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.neuroph.nnet.comp.Dimension2D;
import org.neuroph.nnet.comp.layer.FeatureMapLayer;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.NeuronProperties;

public class FeatureMapTest {
	@Rule
	public JUnitSoftAssertions softly = new JUnitSoftAssertions();

	@Test
	public void testEmptyFeatureMap() {
		Dimension2D dimension = new Dimension2D(0, 0);
		FeatureMapLayer featureMap = new FeatureMapLayer(dimension, new NeuronProperties());

		assertFeatureMap(featureMap, dimension);
	}

	@Test
	public void testFeatureMapWithOneNeuron() {
		Dimension2D dimension = new Dimension2D(4, 3);
		FeatureMapLayer featureMap = new FeatureMapLayer(dimension, new NeuronProperties());
		InputNeuron inputNeuron = new InputNeuron();
		inputNeuron.setInput(1);
		featureMap.addNeuron(inputNeuron);

		assertFeatureMap(featureMap, dimension, inputNeuron);
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

		assertFeatureMap(featureMap, dimension, inputNeuron1, inputNeuron2, inputNeuron3, inputNeuron4);
	}

	private void assertFeatureMap(FeatureMapLayer featureMap, Dimension2D dimension, InputNeuron... inputNeurons) {
		softly.assertThat(featureMap.getNeuronsCount())
		      .isEqualTo(dimension.getWidth() * dimension.getHeight() + inputNeurons.length);
		softly.assertThat(featureMap.getWidth()).isEqualTo(dimension.getWidth());
		softly.assertThat(featureMap.getHeight()).isEqualTo(dimension.getHeight());
	}
}
