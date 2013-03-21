package org.neuroph.core.input;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

public class DifferenceTest {

    Difference diference;
    private Connection[] inputConnections;
    private InputNeuron[] inputNeurons;    

    @Before
    public void setUp() {
        diference = new Difference();
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

    @Test
    public void testEmptyArrayInput() {
        double output = diference.getOutput(new Connection[0]);
        assertEquals(0, output, .00000001);
    }

    @Test
    public void testOnRandomConnections() {
        double[] inputs = new double[]{.5d, .25d, -0.25d, 0.1};
        for(int i=0; i<inputNeurons.length; i++) {
            inputNeurons[i].setInput(inputs[i]);
            inputNeurons[i].calculate();
        }        
        double output = diference.getOutput(inputConnections);
        assertEquals(1.7846, output, 0.0001d);
    }
}
