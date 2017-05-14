package org.neuroph.core.input;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import static org.junit.Assert.assertEquals;

public class AndTest {

    private And instance;
    private List<Connection> inputConnections;
    private List <InputNeuron> inputNeurons;    

    @Before
    public void setUp() {
        instance = new And();
        inputNeurons = new ArrayList<>(4);
        for(int i=0; i<4; i++)
            inputNeurons.add(new InputNeuron());
        
        Neuron toNeuron =  new Neuron();
        toNeuron.setInputFunction(instance);
                
        inputConnections = new ArrayList(4);
        for(int i=0; i<4; i++) {
            Connection inConn = new Connection(inputNeurons.get(i), toNeuron, 1);
            inputConnections.add(inConn);
            toNeuron.addInputConnection(inConn);
        }          
    }

    @Test
    public void testZeroUpperBoundary() {
        double[] inputs = new double[]{0.49999d, 0d, 0d, 0d};
        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }
        
        double actualOutput = instance.getOutput(inputConnections);
        assertEquals(0d, actualOutput, 0.000001d);
    }

    @Test
    public void testZeroLowerBoundary() {
        double[] inputs = new double[]{0d, 0d, 0d, 0d};
        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }

        double actualOutput = instance.getOutput(inputConnections);
        assertEquals(0d, actualOutput, 0.000001d);
    }

    @Test
    public void testOneLowerBoundary() {
        double[] inputs = new double[]{0.5d, 0.6d, 0.7d, 0.9d};
        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }
        
        double actualOutput = instance.getOutput(inputConnections);
        assertEquals(1d, actualOutput, 0.000001d);
    }

    @Test
    public void testOneUpperBoundary() {
        double[] inputs = new double[]{1d, 1d, 1d, 1d};
        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }

        double actualOutput = instance.getOutput(inputConnections);
        assertEquals(1d, actualOutput, 0.000001d);
    }

    @Test
    public void testOneForManyInputs() {
        double[] inputs = new double[]{0.52d, 0.53d, 0.54d, 0.5d, 0.6d, 0.83d};
        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }

        double actualOutput = instance.getOutput(inputConnections);
        assertEquals(1d, actualOutput, 0.000001d);
    }

    @Test
    public void testZeroForManyInputs() {
        double[] inputs = new double[]{0.52d, 0.53d, 0.33d, 0.5d, 0.6d, 0.83d};
        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }

        double actualOutput = instance.getOutput(inputConnections);
        assertEquals(0d, actualOutput, 0.000001d);
    }

    @Test
    public void testNoInputs() {
        double actualOutput = instance.getOutput(new ArrayList<Connection>());
        assertEquals(0d, actualOutput, 0.000001d);
    }
}