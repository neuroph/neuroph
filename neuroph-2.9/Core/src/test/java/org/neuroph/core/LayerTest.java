package org.neuroph.core;

import static org.junit.Assert.*;
import org.junit.*;
import org.neuroph.core.learning.LearningRule;

/**
 * 
 * @author Shivanth, Tijana
 */
public class LayerTest {

	Layer instance1, instance2;
	Neuron testneuron1, testneuron2, testneuron3, testneuron4;

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		testneuron1 = new Neuron();
		testneuron2 = new Neuron();
		testneuron3 = new Neuron();
		testneuron4 = new Neuron();
		instance1 = new Layer();
		instance2 = new Layer();
		instance1.addNeuron(testneuron1);
		instance1.addNeuron(testneuron2);
		instance2.addNeuron(testneuron4);
		testneuron4.addInputConnection(testneuron1, .5);
		testneuron1.addOutputConnection(testneuron4.getConnectionFrom(testneuron1));
		testneuron4.addInputConnection(testneuron2, .5);
		testneuron2.addOutputConnection(testneuron4.getConnectionFrom(testneuron2));

	}

	@After
	public void tearDown() {
	}

	@Test
	public void testAddNeuronWithIndex() {
		instance1.addNeuron(1, testneuron3);
		Neuron[] lst = instance1.getNeurons();
		assertEquals(testneuron3, lst[1]);
	}

	@Test
	public void testAddNeuron() {
		Neuron[] lst = instance1.getNeurons();
		assertEquals(testneuron2, lst[1]);
	}

	@Test
	public void testCalculate() {
		instance1.calculate();
		assertEquals(0, testneuron1.getOutput(), .0001);
	}

	@Test
	public void testLabel() {
		instance1.setLabel("Test");
		assertEquals(instance1.getLabel(), "Test");
		assertEquals(instance2.getLabel(), null);
	}

	@Test
	public void testGetNeuronAt() {
		assertEquals(instance1.getNeuronAt(0), testneuron1);
	}

	@Test
	public void testGetNeurons() {
		assertEquals(instance1.getNeurons().length, 2);
	}

	@Test
	public void testGetNeuronsCount() {
		assertEquals(instance1.getNeuronsCount(), 2);
		assertEquals(instance2.getNeuronsCount(), 1);
	}

	@Test
	public void testParentNetwork() {
		assertEquals(instance1.getParentNetwork(), null);
		NeuralNetwork<LearningRule> nn = new NeuralNetwork<>();
		instance1.setParentNetwork(nn);
		assertEquals(instance1.getParentNetwork(), nn);
	}

	@Test
	public void testIndexOf() {
		assertEquals(instance1.indexOf(testneuron1), 0);
		assertEquals(instance1.indexOf(testneuron2), 1);
	}

	@Test
	public void testInitializeWeights() {
		instance2.initializeWeights(0d);
		assertEquals(0d, testneuron4.getWeights()[0].getValue(), .0001);
		assertEquals(0d, testneuron4.getWeights()[0].getValue(), .0001);
	}

	@Test
	public void testIsEmpty() {
		assertFalse(instance1.isEmpty());
		instance1.removeAllNeurons();
		assertTrue(instance1.isEmpty());
	}

	@Test
	public void testRemoveAllNeurons() {
		instance1.removeAllNeurons();
		assertEquals(0, instance1.getNeuronsCount());
	}

	@Test
	public void testRemoveNeuron() {
		instance1.removeNeuron(testneuron1);
		instance1.removeNeuron(testneuron2);
		assertEquals(0, instance1.getNeuronsCount());
	}

	@Test
	public void testRemoveNeuronAt() {
		instance1.removeNeuronAt(0);
		assertEquals(1, instance1.getNeuronsCount());
	}

	@Test
	public void testReset() {
		testneuron1.setInput(5d);
		testneuron1.setOutput(6d);
		assertEquals(6d, testneuron1.getOutput(), .0001);
		instance1.reset();
		assertEquals(0d, testneuron1.getNetInput(), .0001);
		assertEquals(0d, testneuron1.getOutput(), .0001);
	}

	@Test
	public void testSetNeuron() {
		instance1.setNeuron(1, testneuron3);
		Neuron[] lst = instance1.getNeurons();
		assertEquals(testneuron3, lst[1]);
	}

}