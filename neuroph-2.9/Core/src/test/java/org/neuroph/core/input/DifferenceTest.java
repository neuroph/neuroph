package org.neuroph.core.input;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 *
 * @author Tijana
 */
@RunWith(value = Parameterized.class)
public class DifferenceTest {

	Difference instance;
	Connection[] inputConnections;
	InputNeuron[] inputNeurons;
	double[] inputs;
	double expected;

	public DifferenceTest(DoubleArray inputs, double expected) {
		this.inputs = inputs.getArray();
		this.expected = expected;
	}

	@Parameters
	public static Collection<Object[]> getParamters() {
		return Arrays.asList(new Object[][] { { new DoubleArray(new double[] { 1, 1, 1, 1 }), 0 },
				{ new DoubleArray(new double[] { .5d, .25d, -0.25d, 0.1 }), 1.784656 },
				{ new DoubleArray(new double[] { 0, 0, 0, 0 }), 2 } });

	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		instance = new Difference();
		inputNeurons = new InputNeuron[4];
		for (int i = 0; i < inputNeurons.length; i++)
			inputNeurons[i] = new InputNeuron();

		Neuron toNeuron = new Neuron();

		inputConnections = new Connection[4];
		for (int i = 0; i < inputConnections.length; i++) {
			inputConnections[i] = new Connection(inputNeurons[i], toNeuron, 1);
			toNeuron.addInputConnection(inputConnections[i]);
		}
	}

	@Test
	public void testGetOutput() {
		for (int i = 0; i < inputNeurons.length; i++) {
			inputNeurons[i].setInput(inputs[i]);
			inputNeurons[i].calculate();
		}
		double result = instance.getOutput(inputConnections);
		assertEquals(expected, result, 0.000001);
	}
}
