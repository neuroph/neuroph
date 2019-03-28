
package org.neuroph.core.transfer;

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
import java.util.Random;

import org.neuroph.util.Properties;

/**
 *
 * @author Shivanth, Tijana
 */

@RunWith(value = Parameterized.class)
public class StepTest {

	Step instance;
	double net, expected, ylow, yhigh;

	public StepTest(double net, double expected, double ylow, double yhigh) {
		this.net = net;
		this.expected = expected;
		this.ylow = ylow;
		this.yhigh = yhigh;
	}

	@Parameters
	public static Collection<Object[]>  getParamters() {
		Object[][] objects = new Object[11][4];
		Random r = new Random();
		int row = 0;
		int ylow = 0;
		int yhigh = 0;
		int result = 0;
		for (int i = -5; i <= 5; i++) {
			ylow = r.nextInt(10);
			yhigh = ylow + 2;
			if (i > 0)
				result = yhigh;
			else
				result = ylow;
			objects[row] = new Object[] { i, result, ylow, yhigh };
			row++;
		}
		return Arrays.asList(objects);
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		instance = new Step();
		instance.setYHigh(yhigh);
		instance.setYLow(ylow);
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getOutput method, of class Sigmoid.
	 */
	@Test
	public void testGetOutput() {
		double result = instance.getOutput(net);
		assertEquals(expected, result, 0.0);

	}

	/**
	 * Test of getProperties method, of class Step.
	 */
	@Test
	public void testGetProperties() {
		Properties expResult = new Properties();
		expResult.setProperty("transferFunction.yHigh", String.valueOf(yhigh));
		expResult.setProperty("transferFunction.yLow", String.valueOf(ylow));
		Properties result = instance.getProperties();
		assertEquals(expResult, result);
	}

}