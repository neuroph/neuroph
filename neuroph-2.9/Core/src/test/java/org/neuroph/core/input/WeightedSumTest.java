package org.neuroph.core.input;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 *
 * @author Shivanth, Tijana
 */
@RunWith(value = Parameterized.class)
public class WeightedSumTest {

	WeightedSum instance;
	Connection[] inputConnections;
	InputNeuron[] inputNeurons;
	double[] inputs, weights;
	double expected;

	public WeightedSumTest(DoubleArray inputs, DoubleArray weights, double expected) {
		this.inputs = inputs.getArray();
		this.weights = weights.getArray();
		this.expected = expected;
	}

	@Parameters
	public static Collection<Object[]> getParamters() {
		return Arrays.asList(new Object[][] {
				{ new DoubleArray(new double[] { 1, 3, 5, 7 }), new DoubleArray(new double[] { .2, 5, 7, 8 }), 106.2 },
				{ new DoubleArray(new double[] { 1, 3, 5, 7 }),
						new DoubleArray(new double[] { 0.5d, 0.25d, -0.25d, 0.1d }), 0.7 } });

	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		instance = new WeightedSum();

		inputNeurons = new InputNeuron[4];
		for (int i = 0; i < inputNeurons.length; i++) {
			inputNeurons[i] = new InputNeuron();
		}

		Neuron toNeuron = new Neuron();

		inputConnections = new Connection[4];
		for (int i = 0; i < inputConnections.length; i++) {
			inputConnections[i] = new Connection(inputNeurons[i], toNeuron, 1);
			toNeuron.addInputConnection(inputConnections[i]);
		}
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGetOutput() {

		for (int i = 0; i < inputNeurons.length; i++) {
			inputConnections[i].getWeight().setValue(weights[i]);
			inputNeurons[i].setInput(inputs[i]);
			inputNeurons[i].calculate();
		}

		double result = instance.getOutput(inputConnections);
		assertEquals(expected, result, .000001);
	}

}
