package org.neuroph.core;

import static org.junit.Assert.assertEquals;

import org.junit.*;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class ConnectionTest {
    
    Neuron fromNeuron, toNeuron;
    public ExpectedException exception = ExpectedException.none(); 
    
    public ConnectionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        fromNeuron = new Neuron();
        toNeuron = new Neuron();
    }
    
    @After
    public void tearDown() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConnectionIn(){
    	Connection conn = new Connection(fromNeuron, null);
    	 exception.expect(IllegalArgumentException.class);
    	 exception.expectMessage("To neuron in connection cant be null!");
    	 
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConnectionIn2(){
    	Connection conn = new Connection(null,toNeuron );
    	exception.expect(IllegalArgumentException.class);
   	 	exception.expectMessage("From neuron in connection cant be null !");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void ConnectionIn3(){
    	Connection conn = new Connection(null,toNeuron,12 );
    	exception.expect(IllegalArgumentException.class);
   	 	exception.expectMessage("From neuron in connection cant be null !");
    }
    @Test(expected = IllegalArgumentException.class)
    public void ConnectionIn4(){
    	Connection conn = new Connection(fromNeuron, null,0);
    	 exception.expect(IllegalArgumentException.class);
    	 exception.expectMessage("To neuron in connection cant be null!");
    	 
    }
    @Test(expected = IllegalArgumentException.class)
    public void ConnectionIn5(){
    	Connection conn = new Connection(fromNeuron,null,new Weight(4));
    	exception.expect(IllegalArgumentException.class);
   	 	exception.expectMessage("To neuron in connection cant be null!");
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void ConnectionIn6(){
    	Connection conn = new Connection(null,toNeuron,new Weight(2) );
    	exception.expect(IllegalArgumentException.class);
   	 	exception.expectMessage("From neuron in connection cant be null !");
    }
    @Test(expected = IllegalArgumentException.class)
    public void ConnectionIn7(){
    	Connection conn = new Connection(fromNeuron, toNeuron,null);
    	 exception.expect(IllegalArgumentException.class);
    	 exception.expectMessage("Connection Weight cant be null!");
    	 
    }
    @Test
    public void ConnectionIn8(){
    	Connection conn = new Connection(fromNeuron, toNeuron,new Weight(23));
    	Assert.assertNotNull(conn);     	 
    } 
    /**
     * Test of getWeight method, of class Connection.
     */
    @Test
    public void testGetWeight() {
        System.out.println("getWeight");
        Connection instance = new Connection(fromNeuron, toNeuron, 0.777);
        double expResult = 0.777;
        double result = instance.getWeight().value;
        assertEquals(expResult, result, 0d);
    }

    /**
     * Test of setWeight method, of class Connection.
     */
    @Test
    public void testSetWeight() {
        System.out.println("setWeight");
        Weight weight = new Weight(0.88);
        Connection instance = new Connection(fromNeuron, toNeuron);
        instance.setWeight(weight);
        assertEquals(weight, instance.getWeight());
    }

    /**
     * Test of setWeight method, of class Connection.
     */
    @Test(expected  = IllegalArgumentException.class)
    public void throwsExceptionWhenWeightIsSetToNull() {
        System.out.println("setWeight");
        Weight weight = null;
        Connection instance = new Connection(fromNeuron, toNeuron);
        instance.setWeight(weight);
    }

    /**
     * Test of getInput method, of class Connection.
     */
    @Test
    public void testGetInput() {
        System.out.println("getInput");
        Connection instance = new Connection(fromNeuron, toNeuron, 1.0);
        fromNeuron.setOutput(0.5);
        double expResult = 0.5;
        double result = instance.getInput();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getWeightedInput method, of class Connection.
     */
    @Test
    public void testGetWeightedInput() {
        System.out.println("getWeightedInput");
        Connection instance = new Connection(fromNeuron, toNeuron, 0.5);
        fromNeuron.setOutput(0.5);
        double expResult = 0.25;
        double result = instance.getWeightedInput();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getFromNeuron method, of class Connection.
     */
    @Test
    public void testGetFromNeuron() {
        System.out.println("getFromNeuron");
        Connection instance = new Connection(fromNeuron, toNeuron);
        Neuron expResult = fromNeuron;
        Neuron result = instance.getFromNeuron();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFromNeuron method, of class Connection.
     */
    @Test
    public void testSetFromNeuron() {
        System.out.println("setFromNeuron");
        Connection instance = new Connection(new Neuron(), toNeuron);
        instance.setFromNeuron(fromNeuron);
        assertEquals(fromNeuron, instance.getFromNeuron());
    }
    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnNullSetFromNeuron() {
        System.out.println("setFromNeuron");
        Connection instance = new Connection(new Neuron(), toNeuron);
        instance.setFromNeuron(null);
    }

    /**
     * Test of getToNeuron method, of class Connection.
     */
    @Test
    public void testGetToNeuron() {
        System.out.println("getToNeuron");
        Connection instance = new Connection(fromNeuron, toNeuron);
        Neuron expResult = toNeuron;
        Neuron result = instance.getToNeuron();
        assertEquals(expResult, result);
    }

    /**
     * Test of setToNeuron method, of class Connection.
     */
    @Test
    public void testSetToNeuron() {
        System.out.println("setToNeuron");
        Connection instance = new Connection(fromNeuron, new Neuron());
        instance.setToNeuron(toNeuron);
        assertEquals(toNeuron, instance.getToNeuron());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void throwExceptionOnNullSetToNeuron() {
        System.out.println("setToNeuron");
        Connection instance = new Connection(fromNeuron, new Neuron());
        instance.setToNeuron(null);
    }
}
