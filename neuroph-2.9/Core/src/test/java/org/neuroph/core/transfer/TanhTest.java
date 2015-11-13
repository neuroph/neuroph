

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
 * @author Shivanth
 */
@RunWith(value=Parameterized.class)
public class TanhTest {
double input,expected,expected_derivative;
Tanh instance;
@Parameters
public static Collection getparameters(){
  return Arrays.asList(new Object [][]{
      {.1,.0249947929,.940014848},
      {.2,.0499583,.786447},
      {.3,.07485969,.596585},
      {.4,.09966799,.419974},
      {.5,.124353,.280414},
      {.6,.1488850,.1807066},
      {.7,.17323515,.113812},
      {.8,.1973753,.070650},
      {.9,.22127846,.043464},
  })  ;
}
    public TanhTest(double input,double expected,double expected_derivative) {
        this.input=input;
        this.expected=expected;
        this.expected_derivative=expected_derivative;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance=new Tanh();
        instance.setSlope(.5);
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
        result=instance.getOutput(input);
        assertEquals(expected,result,.0001);
    }

    /**
     * Test of getDerivative method, of class Tanh.
     */
    @Test
    public void testGetDerivative() {
        
        instance.setSlope(5);
       // double out = instance.getOutput(input);
        double result = instance.getDerivative(input);
        assertEquals(expected_derivative, result, 0.00001);
        
        
    }

   
}
    