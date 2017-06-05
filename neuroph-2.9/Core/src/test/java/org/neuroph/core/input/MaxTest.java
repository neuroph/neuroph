
package org.neuroph.core.input;

import java.util.Arrays;
import java.util.Collection;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 *
 * @author Zoran, Tijana
 */
@RunWith(value = Parameterized.class)
public class MaxTest {

	Max instance;
	Connection[] inputConnections;
	InputNeuron[] inputNeurons;
	double[] inputs;
	double expected;

	public MaxTest(DoubleArray inputs, double expected) {
		this.inputs = inputs.getArray();
		this.expected = expected;
	}

	@Parameters
	public static Collection<Object[]> getParamters() {
		return Arrays.asList(new Object[][] { { new DoubleArray(new double[] { 0.1d, 0.21d, 0.8d, 0.9d }), 0.9 },
				{ new DoubleArray(new double[] { 0.1d, -0.21d, -0.8d, -0.9d }), 0.1d } });

	}

	@BeforeClass
	public static void setUpClass() throws Exception {

	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		instance = new Max();
		inputNeurons = new InputNeuron[4];
		for (int i = 0; i < 4; i++)
			inputNeurons[i] = new InputNeuron();

		Neuron toNeuron = new Neuron();
		toNeuron.setInputFunction(instance);

		inputConnections = new Connection[4];
		for (int i = 0; i < 4; i++) {
			Connection inConn = new Connection(inputNeurons[i], toNeuron, 1);
			inputConnections[i] = inConn;
			toNeuron.addInputConnection(inConn);
		}
	}

	@Test
	public void testGetOutput() {
		for (int i = 0; i < inputNeurons.length; i++) {
			inputNeurons[i].setInput(inputs[i]);
			inputNeurons[i].calculate();
		}

		double result = instance.getOutput(inputConnections);
		System.out.println(result);
		assertEquals(expected, result, 0.000001d);
	}

}