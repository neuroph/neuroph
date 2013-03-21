package org.neuroph.core.input;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.*;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 *
 * @author Shivanth
 */
public class WeightedSumTest {

    private WeightedSum weightedSum;
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
        weightedSum = new WeightedSum();

        inputNeurons = new InputNeuron[4];
        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i] = new InputNeuron();
        }

        Neuron toNeuron = new Neuron();

        inputConnections = new Connection[4];
        for (int i = 0; i < inputConnections.length; i++) {
            inputConnections[i] = new Connection(inputNeurons[i], toNeuron, 1);
            toNeuron.addInputConnection(inputConnections[i]);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSumWithRandomInput() {
        double[] inputs = new double[]{1, 3, 5, 7};
        double[] weights = new double[]{.2, 5, 7, 8};

        for (int i = 0; i < inputNeurons.length; i++) {
            inputConnections[i].getWeight().setValue(weights[i]);
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }

        double output = weightedSum.getOutput(inputConnections);
        assertEquals(106.2, output, .0001);
    }

    @Test
    public void testOnRandomConnections() {
        double[] inputs = new double[]{1, 3, 5, 7};
        double[] weights = {0.5d, 0.25d, -0.25d, 0.1d};
        
        for (int i = 0; i < inputNeurons.length; i++) {
            inputConnections[i].getWeight().setValue(weights[i]);   
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();            
        }

        double output = weightedSum.getOutput(inputConnections);
        assertEquals(0.7, output, .000001);
    }
}
