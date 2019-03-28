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
 * @author Shivanth, Tijana
 */
@RunWith(value = Parameterized.class)
public class TanhTest {

    Tanh instance;
    double input, expected, expected_derivative;

    @Parameters
    public static Collection<Object[]> getparameters() {
        double[] inputs = new double[]{-3.0, -2.9, -2.8, -2.7, -2.6, -2.5, -2.4, -2.3, -2.2, -2.1, -2.0, -1.9, -1.8,
            -1.7, -1.6, -1.5, -1.4, -1.3, -1.2, -1.1, -1.0, -0.9, -0.8, -0.7, -0.6, -0.5, -0.4, -0.3, -0.2, -0.1,
            2.6645352591e-15, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7,
            1.8, 1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9};

        double[] expectedOutput = new double[]{-0.9950547536867306,
            -0.9939631673505832,
            -0.9926315202011281,
            -0.9910074536781177,
            -0.9890274022010991,
            -0.9866142981514304,
            -0.9836748576936802,
            -0.9800963962661914,
            -0.9757431300314515,
            -0.9704519366134539,
            -0.9640275800758168,
            -0.956237458127739,
            -0.9468060128462681,
            -0.9354090706030987,
            -0.9216685544064711,
            -0.9051482536448662,
            -0.8853516482022622,
            -0.861723159313306,
            -0.8336546070121547,
            -0.800499021760629,
            -0.7615941559557641,
            -0.7162978701990236,
            -0.6640367702678479,
            -0.6043677771171623,
            -0.5370495669980339,
            -0.46211715726000807,
            -0.3799489622552229,
            -0.2913126124515887,
            -0.1973753202249016,
            -0.09966799462495328,
            2.6645352591003686e-15,
            0.09966799462495851,
            0.19737532022490673,
            0.2913126124515936,
            0.3799489622552275,
            0.4621171572600122,
            0.5370495669980375,
            0.6043677771171656,
            0.6640367702678508,
            0.7162978701990261,
            0.7615941559557665,
            0.8004990217606308,
            0.8336546070121564,
            0.8617231593133075,
            0.8853516482022633,
            0.9051482536448671,
            0.9216685544064719,
            0.9354090706030995,
            0.9468060128462688,
            0.9562374581277394,
            0.9640275800758172,
            0.9704519366134542,
            0.9757431300314517,
            0.9800963962661915,
            0.9836748576936803,
            0.9866142981514304,
            0.9890274022010993,
            0.9910074536781177,
            0.9926315202011281,
            0.9939631673505832};

        double[] derivative = new double[]{0.00986603716543999,
            0.01203722195039647,
            0.014682665103197379,
            0.017904226754413388,
            0.0218247976953454,
            0.026592226683160303,
            0.032383774341317895,
            0.03941105402602474,
            0.04792534419642591,
            0.058223038723196896,
            0.07065082485316465,
            0.0856099236734007,
            0.10355837403815238,
            0.12500987063344704,
            0.15052707581828584,
            0.18070663892364902,
            0.2161524590255377,
            0.2574331967030946,
            0.30501999620740994,
            0.35920131616027595,
            0.41997434161402736,
            0.4869173611483427,
            0.5590551677322454,
            0.63473958998246,
            0.7115777625872244,
            0.786447732965929,
            0.8556387860811792,
            0.9151369618266305,
            0.9610429829661176,
            0.9900662908474402,
            1.0,
            0.9900662908474392,
            0.9610429829661156,
            0.9151369618266276,
            0.8556387860811757,
            0.7864477329659252,
            0.7115777625872204,
            0.634739589982456,
            0.5590551677322415,
            0.48691736114833906,
            0.4199743416140236,
            0.35920131616027307,
            0.30501999620740705,
            0.25743319670309217,
            0.21615245902553581,
            0.18070663892364736,
            0.1505270758182844,
            0.1250098706334456,
            0.10355837403815116,
            0.08560992367339981,
            0.07065082485316376,
            0.05822303872319634,
            0.04792534419642547,
            0.03941105402602452,
            0.03238377434131767,
            0.026592226683160303,
            0.021824797695344955,
            0.017904226754413388,
            0.014682665103197379,
            0.01203722195039647};
        Object[][] objects = new Object[inputs.length][3];
        for (int j = 0; j < inputs.length; j++) {
            objects[j] = new Object[]{inputs[j], expectedOutput[j], derivative[j]};
        }
        return Arrays.asList(objects);
    }

    public TanhTest(double input, double expected, double expected_derivative) {
        this.input = input;
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
        instance.setSlope(1);
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
        assertEquals(expected, result, FloatingAccuracy.ACCURACY);
    }

    /**
     * Test of getDerivative method, of class Tanh.
     */
    @Test
    public void testGetDerivative() {
        double result = instance.getDerivative(input);
        assertEquals(expected_derivative, result, FloatingAccuracy.ACCURACY);
    }

}
