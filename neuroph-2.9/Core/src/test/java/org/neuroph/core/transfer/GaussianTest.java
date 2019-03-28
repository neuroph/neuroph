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
 * @author Shivanth, Tijana
 */
@RunWith(Parameterized.class)
public class GaussianTest {

    Gaussian instance;
    double input, expected, expected_derivative;

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{{0, 1.0, 0},
        {.1, .995012479192682, -.099501247919268},
        {.2, .980198673306755, -.196039734661351},
        {.3, .9559974818331, -.28679924454993},
        {.4, .923116346386635, -.369246538554654},
        {.5, .882496902584595, -.441248451292297},
        {.6, .835270211411272, -.501162126846763},
        {.7, .782704538241868, -.547893176769307},
        {.8, .726149037073690, -.580919229658952},
        {.9, .666976810858474, -.600279129772627},});
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
        assertEquals(expected, result, FloatingAccuracy.ACCURACY);
    }

    /**
     * Test of getDerivative method, of class Gaussian.
     */
    @Test
    public void testGetDerivative() {
        instance.getOutput(input);
        double result = instance.getDerivative(input);
        assertEquals(expected_derivative, result, FloatingAccuracy.ACCURACY);
    }
}
