package org.neuroph.core.input;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
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

    private SumSqr instance;
    private List<Connection> inputConnections;
    private List <InputNeuron> inputNeurons;  

    @Before
    public void setUp() {
        instance = new SumSqr();
        inputNeurons = new ArrayList<>(4);
        for(int i=0; i<4; i++) {
            inputNeurons.add(new InputNeuron());
        }

        Neuron toNeuron = new Neuron();

        inputConnections = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            Connection conn =new Connection(inputNeurons.get(i), toNeuron, 1);
            inputConnections.add(conn);
            toNeuron.addInputConnection(conn);
        }
    }

    
    @Test
    public void testSumMultipleInputs() {
        double[] inputs = new double[]{.1, .4, .7, .9};

        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }

        double output = instance.getOutput(inputConnections);
        assertEquals(1.47, output, .00001);
    }

    @Test
    public void testNegatives() {
        double[] inputs = new double[]{.1, -.4, .7, -.9};

        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }

        double output = instance.getOutput(inputConnections);
        assertEquals(1.47, output, .00001);
    }

    @Test
    public void testNoInput() {
        double output = instance.getOutput(new ArrayList<Connection>());
        assertEquals(0, output, .001);
    }
}