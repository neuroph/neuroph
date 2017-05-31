package org.neuroph.core.transfer;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neuroph.util.Properties;
import static org.junit.Assert.*;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Shivanth, Tijana
 */
@RunWith(value = Parameterized.class)
public class LinearTest {

	Linear instance;
	double input, expected;

	@Parameters
	public static Collection<Object[]> getParameters() {
		Object[][] objects = new Object[10][2];
		double no = 0.1;
		for (int i = 0; i < 10; i++) {
			objects[i] = new Object[] { no, no };
			no += 0.1;
		}
		return Arrays.asList(objects);
	}

	public LinearTest(double expected, double input) {
		this.expected = expected;
		this.input = input;
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		instance = new Linear();
		instance.setSlope(1);
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getOutput method, of class Linear.
	 */
	@Test
	public void testgetOutput() {
		double result = instance.getOutput(input);
		assertEquals(expected, result, 0.0);
	}

	/**
	 * Test of getDerivative method, of class Linear.
	 */
	@Test
	public void testgetDerivative() {
		double result = instance.getDerivative(input);
		assertEquals(1, result, 0.0);
	}

	@Test
	public void testgetOutputwithproperties() {
		Properties properties = new Properties();
		properties.setProperty("transferFunction.slope", 1.0);
		instance = new Linear(properties);// again
		double result = instance.getOutput(input);
		assertEquals(expected, result, 0.0);
	}

	@Test
	public void testgetDerivativewthproperties() {
		Properties properties = new Properties();
		properties.setProperty("transferFunction.slope", 1.0);
		instance = new Linear(properties);// and again
		double result = instance.getDerivative(input);
		assertEquals(1, result, 0.0);
	}
}
