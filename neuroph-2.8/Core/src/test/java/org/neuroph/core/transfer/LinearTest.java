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
 * @author Shivanth
 */
@RunWith(value = Parameterized.class)
public class LinearTest {

    double net;
    Linear instance;
    double input;
    double expected;

    @Parameters
    public static Collection getParameters() {
        return Arrays.asList(new Object[][]{
                    {.1, .1},
                    {.2, .2},
                    {.3, .3},
                    {.4, .4},
                    {.5, .5},
                    {.6, .6},
                    {.7, .7},
                    {.8, .8},
                    {.9, .9},
                    {1, 1}
                });
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
        net = 5.0;
        instance = new Linear();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getOutput method, of class Linear.
     */
    @Test
    public void testgetOutput() {
        instance.setSlope(1);
        assertEquals(expected, instance.getOutput(input), 0.0);
    }

    /**
     * Test of getDerivative method, of class Linear.
     */
    @Test
    public void testgetDerivative() {

        instance = new Linear();// may be i shouldn't be instantiating instance in setUp
        instance.setSlope(1.0);
        double result = instance.getDerivative(input);
        assertEquals(1, result, 0.0);
    }

    @Test
    public void testgetOutputwithproperties() {
        Properties properties = new Properties();
        properties.setProperty("transferFunction.slope", 1.0);
        instance = new Linear(properties);//again
        double result = instance.getOutput(input);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testgetDerivativewthproperties() {
        Properties properties = new Properties();
        properties.setProperty("transferFunction.slope", 1.0);
        instance = new Linear(properties);//and again
        double result = instance.getDerivative(input);
        assertEquals(1.0, result, 0.0);
    }
}
