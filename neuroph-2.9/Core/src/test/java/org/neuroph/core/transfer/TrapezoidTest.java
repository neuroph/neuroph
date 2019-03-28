
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

/**
 *
 * @author Shivanth, Tijana
 */

@RunWith(value = Parameterized.class)
public class TrapezoidTest {

	Trapezoid instance;
	double net, expected;

	public TrapezoidTest(double net, double expected) {
		this.net = net;
		this.expected = expected;
	}

	@Parameters
	public static Collection<Object[]> getParamters() {
		return Arrays.asList(new Object[][] { { 0.5, 0.75 }, { 0.9, 0 }, { 0.65, 1 } });
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		instance = new Trapezoid();
		instance.setLeftHigh(0.6);
		instance.setLeftLow(0.2);
		instance.setRightHigh(0.7);
		instance.setRightLow(0.2);
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getOutput method, of class Trapezoid.
	 */
	@Test
	public void testGetOutput() {
		double result = instance.getOutput(net);
		assertEquals(expected, result, 0.0);
	}

}
