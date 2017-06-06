
package org.neuroph.core.transfer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.neuroph.util.TransferFunctionType;
import static org.junit.Assert.*;

/**
 *
 * @author Shivanth, Tijana
 */
@RunWith(value = Parameterized.class)
public class SgnTest {

	Sgn instance;
	double net, expected;

	public SgnTest(double net, double expected) {
		this.net = net;
		this.expected = expected;
	}

	@Parameters
	public static Collection<Object[]> getParamters() {
		Object[][] objects = new Object[11][2];
		int row = 0;
		int result = 0;
		for (int i = -5; i <= 5; i++) {
			if (i > 0)
				result = 1;
			else
				result = -1;
			objects[row] = new Object[] { i, result };
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
		instance = new Sgn();
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getOutput method, of class Sgn.
	 */
	@Test
	public void testGetOutput() {
		double result = instance.getOutput(net);
		assertEquals(expected, result, 0.0);
	}

	/**
	 * Test of getProperties method, of class Sgn.
	 */
	@Test
	public void testGetProperties() {
		Properties expResult = new Properties();
		expResult.setProperty("transferFunction", TransferFunctionType.SGN.toString());
		Properties result = instance.getProperties();
		assertEquals(expResult, result);

	}

}