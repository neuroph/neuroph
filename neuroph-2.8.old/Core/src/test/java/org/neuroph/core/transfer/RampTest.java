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
import org.junit.runner.Runner;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Shivanth
 */
@RunWith(value = Parameterized.class)
public class RampTest {

    double expected,input,expected_derivative;
    Ramp instance;

    public RampTest(double input,double expected,double expected_derivative ) {
        this.input=input;
        this.expected=expected;
        this.expected_derivative=expected_derivative;
    }
@Parameters
public static Collection getparameters(){
    return Arrays.asList(new Object [][]{
        {.1,.3,1.0},
        {.2,.3,1.0},
        {.3,.3,1.0},
        {.4,.3,1.0},
        {.5,.3,1.0},
        {.6,.6,1.0},
        {.7,.7,1.0},
        {.8,.8,1.0},
        {.9,.9,1.0}

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
        instance = new Ramp();
            }

    @After
    public void tearDown() {
    }

    @Test
    public void testgetOutput() {
        instance.setXHigh(.8);
        instance.setXLow(.6);
        instance.setYHigh(.9);
        instance.setYLow(.3);
        double result = instance.getOutput(input);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testgetOutputwthProperties() {
        Properties properties = new Properties();
        properties.setProperty("transferFunction.slope", 1d);
        properties.setProperty("transferFunction.yHigh", .9d);
        properties.setProperty("transferFunction.yLow", .3d);
        properties.setProperty("transferFunction.xHigh", .8d);
        properties.setProperty("transferFunction.xLow", .6d);
        instance = new Ramp(properties);
        double result = instance.getOutput(input);
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testgetDerivativewthProperties() {
        Properties properties = new Properties();
        //properties.setProperty("transferFunction.slope", .5d);
        properties.setProperty("transferFunction.yHigh", .9d);
        properties.setProperty("transferFunction.yLow", .3d);
        properties.setProperty("transferFunction.xHigh", .8d);
        properties.setProperty("transferFunction.xLow", .6d);
        Ramp instance = new Ramp(properties);
        double result = instance.getDerivative(input);
        assertEquals(expected_derivative, result, 0.0);
    }

    @Test
    public void testgetDerivative() {
        instance.setXHigh(5);
        instance.setXLow(1);
        instance.setYHigh(5);
        instance.setYLow(2);
        double result = instance.getDerivative(input);
        assertEquals(expected_derivative, result, 0.0);
    }
}
