

package org.neuroph.core.transfer;

import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Shivanth, Nyefan
 */
@RunWith(value=Parameterized.class)
public class TanhTest {
    private double input, slope, amplitude, expected, expected_derivative;
    private Tanh instance;
    @Parameters
    public static Collection getparameters(){
        return Arrays.asList(new Object [][]{
                {.1, 0.5, 1.5, .0749376d, .748128d},
                {.2, 0.5, 1.5, .149502d,  .74255d },
                {.3, 0.5, 1.5, .223328d,  .733375d},
                {.4, 0.5, 1.5, .296063d,  .720782d},
                {.5, 0.5, 1.5, .367378d,  .705011d},
                {.6, 0.5, 1.5, .436969d,  .686353d},
                {.7, 0.5, 1.5, .504563d,  .665139d},
                {.8, 0.5, 1.5, .569923d,  .641729d},
                {.9, 0.5, 1.5, .632849d,  .616501d},
        });
    }
        public TanhTest(double input, double slope, double amplitude, double expected, double expected_derivative) {
            this.input = input;
            this.slope = slope;
            this.amplitude = amplitude;
            this.expected = expected;
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
            instance = new Tanh();
            instance.setSlope(slope);
            instance.setAmplitude(amplitude);
        }

        @After
        public void tearDown() {
        }

        /**
         * Test of getOutput method, of class Tanh.
         */
        @Test
        public void testGetOutput() {
            double result = instance.getOutput(input);
            assertEquals(expected, result, 0.00001);
            result = instance.getOutput(input);
            assertEquals(expected,result,.0001);
        }

        /**
         * Test of getDerivative method, of class Tanh.
         */
        @Test
        public void testGetDerivative() {
            // this must be called before getDerivative is called;
            double out = instance.getOutput(input);
            double result = instance.getDerivative(input);
            assertEquals(expected_derivative, result, 0.00001);


        }

   
}
    