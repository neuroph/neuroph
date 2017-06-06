package org.neuroph.core.input;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 *
 * @author Tijana
 */
@RunWith(value = Parameterized.class)
public class AndTest {

    And instance;
    List<Connection> inputConnections;
    List<InputNeuron> inputNeurons;
    double[] inputs;
    double expected;

    public AndTest(DoubleArray inputs, double expected) {
        this.inputs = inputs.getArray();
        this.expected = expected;
    }

    @Parameters
    public static Collection<Object[]> getParamters() {
        return Arrays.asList(new Object[][]{{new DoubleArray(new double[]{0.49999d, 0d, 0d, 0d}), 0},
        {new DoubleArray(new double[]{0d, 0d, 0d, 0d}), 0},
        {new DoubleArray(new double[]{0.5d, 0.6d, 0.7d, 0.9d}), 1},
        {new DoubleArray(new double[]{1d, 1d, 1d, 1d}), 1},
        {new DoubleArray(new double[]{0.52d, 0.53d, 0.54d, 0.5d, 0.6d, 0.83d}), 1},
        {new DoubleArray(new double[]{0.52d, 0.53d, 0.33d, 0.5d, 0.6d, 0.83d}), 0}});

    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new And();
        inputNeurons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            inputNeurons.add(new InputNeuron());
        }
               
        Neuron toNeuron = new Neuron();

        inputConnections = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            inputConnections.add(new Connection(inputNeurons.get(i), toNeuron, 1));
            toNeuron.addInputConnection(inputConnections.get(i));
        }
    }

    @Test
    public void testGetOutput() {
        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }

        double result = instance.getOutput(inputConnections);
        assertEquals(expected, result, 0.000001);
    }

}
