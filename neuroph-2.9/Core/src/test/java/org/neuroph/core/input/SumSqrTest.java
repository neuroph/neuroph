package org.neuroph.core.input;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 *
 * @author Shivanth, Tijana
 */
@RunWith(value = Parameterized.class)
public class SumSqrTest {

	SumSqr instance;
	Connection[] inputConnections;
	InputNeuron[] inputNeurons;
	double[] inputs;
	double expected;

	public SumSqrTest(DoubleArray inputs, double expected) {
		this.inputs = inputs.getArray();
		this.expected = expected;
	}

	@Parameters
	public static Collection<Object[]> getParamters() {
		return Arrays.asList(new Object[][] { { new DoubleArray(new double[] { .1, .4, .7, .9 }), 1.47 },
				{ new DoubleArray(new double[] { .1, -.4, .7, -.9 }), 1.47 }});

	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		instance = new SumSqr();
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
			inputNeurons[i].setInput(inputs[i]);
			inputNeurons[i].calculate();
		}

		double result = instance.getOutput(inputConnections);
		assertEquals(expected, result, .000001);
	}

}