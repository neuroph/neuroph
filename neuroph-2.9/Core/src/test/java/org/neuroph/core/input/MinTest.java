/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.core.input;

import java.util.ArrayList;
import java.util.List;
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
 * @author zoran
 */
public class MinTest {
    
    private Min instance;
    private List<Connection> inputConnections;
    private List <InputNeuron> inputNeurons;        
     
    
    @Before
    public void setUp() {
        instance = new Min();
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
    public void testGetOutputPositive() {
        double[] inputs = new double[]{0.1d, 0.21d, 0.8d, 0.9d};
        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }

        double actualOutput = instance.getOutput(inputConnections);
        assertEquals(0.1d, actualOutput, 0.000001d);
    }
    
    @Test
    public void testGetOutputNegative() {
        double[] inputs = new double[]{0.1d, 0.21d, -0.8d, 0.9d};
        for (int i = 0; i < inputNeurons.size(); i++) {
            inputNeurons.get(i).setInput(inputs[i]);
            inputNeurons.get(i).calculate();
        }

        double actualOutput = instance.getOutput(inputConnections);
        assertEquals(-0.8d, actualOutput, 0.000001d);
    }    
    
    @Test
    public void testNoInputs() {
        double actualOutput = instance.getOutput(new ArrayList<Connection>());
        assertEquals(0d, actualOutput, 0.000001d);
    }    
    
}
