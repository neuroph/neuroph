package org.neuroph.core.transfer;

import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;

/**
 *
 * @author Shivanth
 */
@RunWith(Parameterized.class)
public class GaussianTest {

    Gaussian instance;
    double input;
    double expected;
    double expected_derivative;

    @Parameters
    public static Collection getParameters() {
        return Arrays.asList(new Object[][]{
                    {0, 1.0, 0},
                    {.1, .9950124, -.09950124},
                    {.2, .9801986733, -.1960397},
                    {.3, .955997481, -.286799244},
                    {.4, .923116946, -0.3692465},
                    {.5, 0.882496, -.44124851},
                    {.6, .83527021141, -.5011962126},
                    {.7, .782704538, -.54789317},
                    {.8, .7261490370, -.58091922},
                    {.9, .6669768108, -.60027991},});
    }

    public GaussianTest(double input, double expected, double expected_derivative) {
        this.expected = expected;
        this.input = input;
        this.expected_derivative = expected_derivative;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new Gaussian();
        instance.setSigma(1);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getOutput method, of class Gaussian.
     */
    @Test
    public void testGetOutput() {
        double result = instance.getOutput(input);
        assertEquals(expected, result, 0.0001);
    }

    /**
     * Test of getDerivative method, of class Gaussian.
     */
    @Test
    public void testGetDerivative() {
        double expResult = 0.0;
        double out = instance.getOutput(input);
        double result = instance.getDerivative(input);
        assertEquals(expected_derivative, result, 0.0001);
    }
}
