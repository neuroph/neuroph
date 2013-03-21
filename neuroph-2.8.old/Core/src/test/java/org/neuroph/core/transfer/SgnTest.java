

package org.neuroph.core.transfer;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neuroph.util.TransferFunctionType;
import static org.junit.Assert.*;

/**
 *
 * @author Shivanth
 */
public class SgnTest {
double net;
Sgn instance;
    public SgnTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance=new Sgn();
        net=3.0;
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getOutput method, of class Sgn.
     */
    @Test
    public void testGetOutput() {
        
        double expResult = 1.0;
        double result = instance.getOutput(net);
        assertEquals(expResult, result, 0.0);
        net=-.5;
        expResult=-1;
        assertEquals(expResult,instance.getOutput(net),.001);
        
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