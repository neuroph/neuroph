

package org.neuroph.core.transfer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.neuroph.util.Properties;

/**
 *
 * @author Shivanth
 */
public class StepTest {

    public StepTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getOutput method, of class Step.
     */
    @Test
    public void testGetOutput() {
        double net = 1.0,ylow=5,yhigh=6;
        Step instance = new Step();
        instance.setYHigh(yhigh);
        instance.setYLow(ylow);
        double expResult = 6;
        double result = instance.getOutput(net);
        assertEquals(expResult, result, 0.0);
        expResult=5;
        net=-3;
        result=instance.getOutput(net);
        assertEquals(expResult,result,0.0);
        
    }

    
  
    /**
     * Test of getProperties method, of class Step.
     */
    @Test
    public void testGetProperties() {
        double ylow=6,yhigh=7;
        Step instance = new Step();
        instance.setYHigh(yhigh);
        instance.setYLow(ylow);
        Properties expResult = new Properties();
        expResult.setProperty("transferFunction.yHigh", String.valueOf(yhigh));
        expResult.setProperty("transferFunction.yLow", String.valueOf(ylow));
        Properties result = instance.getProperties();
        assertEquals(expResult, result);
        }

}