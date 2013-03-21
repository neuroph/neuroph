package org.neuroph.core.input;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 *
 * @author Shivanth
 */
public class SumSqrTest {

    private SumSqr sumSqr;
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
        sumSqr = new SumSqr();
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
    public void testSumMultipleInputs() {
        double[] inputs = new double[]{.1, .4, .7, .9};

        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }

        double output = sumSqr.getOutput(inputConnections);
        assertEquals(1.47, output, .00001);
    }

    @Test
    public void testNegatives() {
        double[] inputs = new double[]{.1, -.4, .7, -.9};

        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }

        double output = sumSqr.getOutput(inputConnections);
        assertEquals(1.47, output, .00001);
    }

    @Test
    public void testNoInput() {
        double output = sumSqr.getOutput(new Connection[0]);
        assertEquals(0, output, .001);
    }
}