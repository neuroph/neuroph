

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
public class OrTest {
    private Or or;
    private Connection[] inputConnections;
    private InputNeuron[] inputNeurons;    


    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        or = new Or();
        
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
    public void testZeroUpperBoundary() {
        double[] inputs = new double[]{0.49999d, 0d, 0d, 0d};
        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }

        double actualOutput = or.getOutput(inputConnections);
        assertEquals(0d, actualOutput, 0.000001d);
    }

	@Test
    public void testZeroLowerBoundary() {
        double[] inputs = new double[]{0d, 0d, 0d, 0d};
        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }

        double actualOutput = or.getOutput(inputConnections);
        assertEquals(0d, actualOutput, 0.000001d);
    }

	@Test
    public void testOneLowerBoundary() {
        double[] inputs = new double[]{0.5d, 0d, 0d, 0d};
        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }

        double actualOutput = or.getOutput(inputConnections);
        assertEquals(1d, actualOutput, 0.000001d);
    }

	@Test
    public void testOneUpperBoundary() {
        double[] inputs = new double[]{1d, 0d, 0d, 0d};
        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }

        double actualOutput = or.getOutput(inputConnections);
        assertEquals(1d, actualOutput, 0.000001d);
    }

	@Test
    public void testOneForManyInputs() {
        double[] inputs = new double[]{0.52d, 0.53d, 0.54d, 0.5d, 0.6d, 0.83d};
        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }

        double actualOutput = or.getOutput(inputConnections);
        assertEquals(1d, actualOutput, 0.000001d);
    }

	@Test
    public void testZeroForManyInputs() {
        double[] inputs = new double[]{0.32d, 0.44d, 0.33d, 0.5d, 0.4d, 0.23d};
        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }

        double actualOutput = or.getOutput(inputConnections);
        assertEquals(1d, actualOutput, 0.000001d);
    }

	@Test
    public void testNoInputs() {
        double actualOutput = or.getOutput(new Connection[0]);
        assertEquals(0d, actualOutput, 0.000001d);
    }
}