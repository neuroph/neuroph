package org.neuroph.core;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Shivanth
 */
public class NeuronTest {

    Neuron instance1, instance2, instance3, instance4;
    Connection testConnection13, testConnection14, testConnection23, testConnection24;

    public NeuronTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance1 = new Neuron();
        instance2 = new Neuron();
        instance3 = new Neuron();
        instance4 = new Neuron();
        
        testConnection13 = new Connection(instance1, instance3, .9);
        instance1.addOutputConnection(testConnection13);
        instance3.addInputConnection(testConnection13);
                
        testConnection14 = new Connection(instance1, instance4, .6);
        instance1.addOutputConnection(testConnection14);
        instance4.addInputConnection(testConnection14);
        
        testConnection23 = new Connection(instance2, instance3, .7);
        instance2.addOutputConnection(testConnection23);        
        instance3.addInputConnection(testConnection23);

        testConnection24 = new Connection(instance2, instance4, .8);
        instance2.addOutputConnection(testConnection24);
        instance4.addInputConnection(testConnection24);        
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCalculate() {
        instance1.setOutput(.6);
        instance2.setOutput(.8);
        instance3.calculate();
        assertEquals(1.0, instance3.getOutput(), .00001);
    }

    @Test
    public void testReset(){
        instance1.setInput(.5);
        instance1.setOutput(.3);
        instance1.reset();
        assertEquals(instance1.getOutput(),0,.001);
        assertEquals(instance1.getNetInput(),0,.0001);
    }

    //setInput not tested

    //getnetinput not tested

    //getoutput not tested
   

    @Test
    public void testHasInputConnections() {
        assertTrue(instance3.hasInputConnections());
        assertTrue(instance4.hasInputConnections());
        assertFalse(instance1.hasInputConnections());
        assertFalse(instance2.hasInputConnections());
    }

    @Test
    public void testGetInputsIterator() {
        Connection[] conns = instance3.getInputConnections();
        assertEquals(testConnection13, conns[0]);
        assertEquals(testConnection23, conns[1]);
    }

    @Test
    public void testRemoveInputConnectionFrom() {
        instance3.removeInputConnectionFrom(instance1);
        List<Connection> c= Arrays.asList(instance3.getInputConnections());
        assertFalse(c.contains(testConnection13));
        assertTrue(c.contains(testConnection23));
    }

    @Test
    public void testGetConnectionFrom() {
        Connection a=instance3.getConnectionFrom(instance1);
        Connection b=instance3.getConnectionFrom(instance2);
        assertEquals(testConnection13,a);
        assertEquals(testConnection23,b);
    }

    
   
    @Test
    public void testGetWeightsVector() {
        List<Weight> weights= Arrays.asList( instance3.getWeights() );
        assertTrue(weights.contains(testConnection13.getWeight()));
        assertTrue(weights.contains(testConnection23.getWeight()));
        assertFalse(weights.contains(testConnection14.getWeight()));
    }



    @Test
    public void testInitializeWeights_double() {             
        Weight[] w=instance3.getWeights();
        assertEquals(.9,w[0].getValue(),.00001);
        assertEquals(.7,w[1].getValue(),.0001);
    }

//    @Test
//    public void testInitializeWeights_Random() {
//        Random generator = new Random();
//        instance3.initializeWeights(generator);
//        Weight[] weight2=instance3.getWeights();
//        assertTrue(weight2[1].getValue()!=.7);
//        assertTrue(weight2[1].getValue()!=.9);
//    }
//
//    @Test
//    public void testInitializeWeights_double_double() {
//        instance3.initializeWeights(.1,.5);
//        Weight[] weights=instance3.getWeights();
//        for(Weight w : weights){
//            if((w.getValue()>.5)||(w.getValue()<.1))
//                fail();
//        }
//    }
}
