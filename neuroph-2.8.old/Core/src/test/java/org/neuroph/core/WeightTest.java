package org.neuroph.core;

import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class WeightTest {
    
    public WeightTest() {
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
     * Test of inc method, of class Weight.
     */
    @Test
    public void testInc() {
        System.out.println("inc");
        double amount = 0.5;
        Weight instance = new Weight(0.5);
        instance.inc(amount);
        assertEquals(1.0, instance.value, 0);
    }

    /**
     * Test of dec method, of class Weight.
     */
    @Test
    public void testDec() {
        System.out.println("dec");
        double amount = 0.6;
        Weight instance = new Weight(1);
        instance.dec(amount);
        assertEquals(0.4, instance.value, 0);
    }

    /**
     * Test of setValue method, of class Weight.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        double value = 0.5;
        Weight instance = new Weight();
        instance.setValue(value);
        assertEquals(0.5, instance.value, 0);
    }

    /**
     * Test of getValue method, of class Weight.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Weight instance = new Weight(0.7);
        double expResult = 0.7;
        double result = instance.getValue();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of toString method, of class Weight.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Weight instance = new Weight(0.4);
        String expResult = "0.4";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of randomize method, of class Weight.
     */
    @Test
    public void testRandomize_0args() {
        System.out.println("randomize");
        Weight instance = new Weight(0.3);
        instance.randomize();
        if (instance.value == 0.3)
            fail("Weight is not changed after randomization");

    }

    /**
     * Test of randomize method, of class Weight.
     */
    @Test
    public void testRandomize_double_double() {
        System.out.println("randomize");
        double min = 0.3;
        double max = 0.7;
        Weight instance = new Weight();
        instance.randomize(min, max);
        if (instance.value < min || instance.value > max)
            fail("Weight is randomized out of specified range!");
    }

    /**
     * Test of getTrainingData method, of class Weight.
     */
    @Test
    public void testGetTrainingData() {
        System.out.println("getTrainingData");
        Weight instance = new Weight();
        Object trainingData = new Object();
        instance.setTrainingData(trainingData);        
        Object expResult = trainingData;
        Object result = instance.getTrainingData();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTrainingData method, of class Weight.
     */
    @Test
    public void testSetTrainingData() {
        System.out.println("setTrainingData");
        Object trainingData = new Object();
        Weight instance = new Weight();
        instance.setTrainingData(trainingData);
        Object expResult = trainingData;
        Object result = instance.getTrainingData();
        assertEquals(expResult, result);
    }
}
