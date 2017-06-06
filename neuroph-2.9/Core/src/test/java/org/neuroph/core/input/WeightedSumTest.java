package org.neuroph.core.input;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

/**
 *
 * @author Shivanth, Tijana
 */
@RunWith(value = Parameterized.class)
public class WeightedSumTest {

    WeightedSum instance;
    List<Connection> inputConnections;
    List<InputNeuron> inputNeurons;
    double[] inputs, weights;
    double expected;

    public WeightedSumTest(DoubleArray inputs, DoubleArray weights, double expected) {
        this.inputs = inputs.getArray();
        this.weights = weights.getArray();
        this.expected = expected;
    }

    @Parameters
    public static Collection<Object[]> getParamters() {
        return Arrays.asList(new Object[][]{
            {new DoubleArray(new double[]{1, 3, 5, 7}), new DoubleArray(new double[]{.2, 5, 7, 8}), 106.2},
            {new DoubleArray(new double[]{1, 3, 5, 7}),
                new DoubleArray(new double[]{0.5d, 0.25d, -0.25d, 0.1d}), 0.7}});

    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new WeightedSum();

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

    @After
    public void tearDown() {
    }

    @Test
    public void testGetOutput() {

        for (int i = 0; i < inputNeurons.size(); i++) {
            inputConnections.get(i).getWeight().setValue(weights[i]);
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }

        double result = instance.getOutput(inputConnections);
        assertEquals(expected, result, .000001);
    }

}
