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

public class DifferenceTest {

    Difference instance;
    private List<Connection> inputConnections;
    private List<InputNeuron> inputNeurons;    

    @Before
    public void setUp() {
        instance = new Difference();
        inputNeurons = new ArrayList<>(4);
        for(int i=0; i<4; i++)
            inputNeurons.add(new InputNeuron());
        
        Neuron toNeuron =  new Neuron();
                
        inputConnections = new ArrayList<Connection>(4);
        for(int i=0; i<4; i++) {
            Connection conn = new Connection(inputNeurons.get(i), toNeuron, 1);
            inputConnections.add(conn);
            toNeuron.addInputConnection(conn);
        }         
    }

    @Test
    public void testEmptyArrayInput() {
        double output = instance.getOutput(new ArrayList<Connection>());
        assertEquals(0, output, .00000001);
    }

    @Test
    public void testOnRandomConnections() {
        double[] inputs = new double[]{.5d, .25d, -0.25d, 0.1};
        for(int i=0; i<inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }        
        double output = instance.getOutput(inputConnections);
        assertEquals(1.7846, output, 0.0001d);
    }
}
