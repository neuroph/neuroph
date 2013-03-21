package org.neuroph.core.transfer;

import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Shivanth
 */
@RunWith(value = Parameterized.class)
public class SigmoidTest {

    Sigmoid instance;
    double input, expected, expected_derivative;

    public SigmoidTest(double input, double expected, double expected_derivative) {
        this.expected = expected;
        this.expected_derivative = expected_derivative;
        this.input = input;
    }

    @Parameters
    public static Collection getParamters() {
        return Arrays.asList(new Object[][]{
                    {.1, .524979, .3493760},
                    {.2, .549833, .347516},
                    {.3, .574442, .344458},
                    {.4, .598687, .340260},
                    {.5, .622459, .335003},
                    {.6, .645656, .328784},
                    {.7, .668187, .321712},
                    {.8, .6899744, .313909},
                    {.9, .7109495, .305500}
                });
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new Sigmoid();
        instance.setSlope(1.0);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getOutput method, of class Sigmoid.
     */
    @Test
    public void testGetOutput() {
        double result = instance.getOutput(input);
        assertEquals(expected, result, 0.0001);        
    }

    /**
     * Test of getDerivative method, of class Sigmoid.
     */
    @Test
    public void testGetDerivative() {
        double output = instance.getOutput(input);
        double result = instance.getDerivative(input);
        assertEquals(expected_derivative, result, 0.00001);
    }
}
