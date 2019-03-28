package org.neuroph.core;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.*;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.transfer.Step;
import org.neuroph.nnet.comp.layer.InputLayer;

/**
 * 
 * @author Shivanth, Tijana
 */
public class LayerTest {

	Layer instance;
	Neuron testneuron1, testneuron2, testneuron3;

	@Before
	public void setUp() {
		testneuron1 = new Neuron();
		testneuron2 = new Neuron();
		testneuron3 = new Neuron();
		instance = new Layer();
		instance.addNeuron(testneuron1);
		instance.addNeuron(testneuron2);
	}

	@Test
	public void testAddNeuronWithIndex() {
		instance.addNeuron(1, testneuron3);
                List<Neuron> lst = instance.getNeurons();
		assertEquals(testneuron3, lst.get(1));
	}

	@Test
	public void testAddNeuron() {
		List<Neuron> lst = instance.getNeurons();
		assertEquals(testneuron2, lst.get(1));
	}

	@Test
	public void testCalculate() {
		InputLayer input = new InputLayer(2);
		testneuron1.addInputConnection(input.getNeuronAt(0), 5);
		input.getNeuronAt(0).setInput(1);
		instance.calculate();
		Step transfer = new Step();
		assertEquals(transfer.getOutput(5), testneuron1.getOutput(), 0.0);
		input.getNeuronAt(0).setInput(-1);
		instance.calculate();
		assertEquals(transfer.getOutput(-5), testneuron1.getOutput(), 0.0);
	}

	@Test
	public void testLabel() {
		assertEquals(instance.getLabel(), null);
		instance.setLabel("Test");
		assertEquals(instance.getLabel(), "Test");
	}

	@Test
	public void testGetNeuronAt() {
		assertEquals(instance.getNeuronAt(0), testneuron1);
	}

	@Test
	public void testGetNeurons() {
		assertEquals(instance.getNeurons().size(), 2);
	}

	@Test
	public void testGetNeuronsCount() {
		assertEquals(instance.getNeuronsCount(), 2);
	}

	@Test
	public void testParentNetwork() {
		assertEquals(instance.getParentNetwork(), null);
		NeuralNetwork<LearningRule> nn = new NeuralNetwork<>();
		instance.setParentNetwork(nn);
		assertEquals(instance.getParentNetwork(), nn);
	}

	@Test
	public void testIndexOf() {
		assertEquals(instance.indexOf(testneuron1), 0);
		assertEquals(instance.indexOf(testneuron2), 1);
	}

	@Test
	public void testInitializeWeights() {
		InputLayer input = new InputLayer(2);
		testneuron1.addInputConnection(input.getNeuronAt(0), 5);
		testneuron2.addInputConnection(input.getNeuronAt(1), 5);
		instance.initializeWeights(1);
		assertEquals(1, testneuron1.getWeights()[0].getValue(), .0001);
		assertEquals(1, testneuron2.getWeights()[0].getValue(), .0001);
	}

	@Test
	public void testIsEmpty() {
		assertFalse(instance.isEmpty());
		instance.removeAllNeurons();
		assertTrue(instance.isEmpty());
	}

	@Test
	public void testRemoveAllNeurons() {
		instance.removeAllNeurons();
		assertEquals(0, instance.getNeuronsCount());
	}

	@Test
	public void testRemoveNeuron() {
		instance.removeNeuron(testneuron1);
		instance.removeNeuron(testneuron2);
		assertEquals(0, instance.getNeuronsCount());
	}

	@Test
	public void testRemoveNeuronAt() {
		instance.removeNeuronAt(0);
		assertEquals(1, instance.getNeuronsCount());
	}

	@Test
	public void testReset() {
		testneuron1.setInput(5d);
		testneuron1.setOutput(6d);
		assertEquals(6d, testneuron1.getOutput(), .0001);
		instance.reset();
		assertEquals(0d, testneuron1.getNetInput(), .0001);
		assertEquals(0d, testneuron1.getOutput(), .0001);
	}

	@Test
	public void testSetNeuron() {
		instance.setNeuron(1, testneuron3);
		List<Neuron> lst = instance.getNeurons();
		assertEquals(testneuron3, lst.get(1));
	}

}