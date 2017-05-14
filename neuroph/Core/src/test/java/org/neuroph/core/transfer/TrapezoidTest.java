
package org.neuroph.core.transfer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shivanth
 */
public class TrapezoidTest {

    double net;
    Trapezoid instance;

    public TrapezoidTest() {
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
        net = .5;
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getOutput method, of class Trapezoid.
     */
    @Test
    public void testGetOutput() {
        double lefthigh = .6, leftlow = .2, righthigh = .7, rightlow = .2;
        instance.setLeftHigh(lefthigh);
        instance.setLeftLow(leftlow);
        instance.setRightHigh(righthigh);
        instance.setRightLow(rightlow);
        double expResult = 0.75;
        double result = instance.getOutput(net);
        assertEquals(expResult, result, 0.0);
        net = .9;
        expResult = 0;
        result = instance.getOutput(net);
        assertEquals(expResult, result, .0001);
        net = .65;
        expResult = 1.0;
        result = instance.getOutput(net);
        assertEquals(expResult, result, .0001);

    }
    /**
     * Test of setLeftLow method, of class Trapezoid.
     */
}
