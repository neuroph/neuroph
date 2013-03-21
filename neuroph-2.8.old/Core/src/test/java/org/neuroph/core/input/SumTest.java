
package org.neuroph.core.input;

import static org.junit.Assert.assertEquals;
import org.junit.*;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 *
 * @author Shivanth
 */
public class SumTest {

    private Sum sum;
    private Connection[] inputConnections;
    private InputNeuron[] inputNeurons;

    public SumTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        sum = new Sum();              
        inputNeurons = new InputNeuron[4];
        for(int i=0; i<inputNeurons.length; i++)
            inputNeurons[i] = new InputNeuron();
        
        Neuron toNeuron =  new Neuron();
                
        inputConnections = new Connection[4];
        for(int i=0; i<inputConnections.length; i++) {
            inputConnections[i] = new Connection(inputNeurons[i], toNeuron, 1);
            toNeuron.addInputConnection(inputConnections[i]);
        }        
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSumMultipleInputs() {
        double[] inputs = new double[]{.1, .4, .7, .9};        
        for(int i=0; i<inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }
        
        double actualOutput = sum.getOutput(inputConnections);
        assertEquals(2.1, actualOutput, .0001);        
      
    }

    @Test
    public void testSumNegatives() {
        double[] inputs = new double[]{.1, -.4, .7, -.9};        
        for(int i=0; i<inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }
        
        double actualOutput = sum.getOutput(inputConnections);
        assertEquals(-0.5, actualOutput, .00001);         
    }

    @Test
    public void testNoInput() {        
        double actualOutput = sum.getOutput(new Connection[0]);        
        assertEquals(0, actualOutput, .001);
    }
}
