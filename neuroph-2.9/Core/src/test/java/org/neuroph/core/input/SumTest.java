
package org.neuroph.core.input;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 *
 * @author Shivanth
 */
public class SumTest {

    private Sum sum;
    private List<Connection> inputConnections;
    private List<InputNeuron> inputNeurons;

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
        inputNeurons = new ArrayList<>(4);
        for(int i=0; i<4; i++)
            inputNeurons.add(new InputNeuron());
        
        Neuron toNeuron =  new Neuron();
                
        inputConnections = new ArrayList<>(4);
        for(int i=0; i<4; i++) {
            inputConnections.add(new Connection(inputNeurons.get(i), toNeuron, 1));
            toNeuron.addInputConnection(inputConnections.get(i));
        }        
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSumMultipleInputs() {
        double[] inputs = new double[]{.1, .4, .7, .9};        
        for(int i=0; i<inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }
        
        double actualOutput = sum.getOutput(inputConnections);
        assertEquals(2.1, actualOutput, .0001);        
      
    }

    @Test
    public void testSumNegatives() {
        double[] inputs = new double[]{.1, -.4, .7, -.9};        
        for(int i=0; i<inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }
        
        double actualOutput = sum.getOutput(inputConnections);
        assertEquals(-0.5, actualOutput, .00001);         
    }

    @Test
    public void testNoInput() {        
        double actualOutput = sum.getOutput(new ArrayList<Connection>());        
        assertEquals(0, actualOutput, .001);
    }
}
