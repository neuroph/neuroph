package org.neuroph.core;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * 
 * @author Shivanth
 */
public class LayerTest {

    Neuron testneuron1, testneuron2, testneuron3, testneuron4;
    Layer testlayer1, testlayer2;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        testneuron1 = new Neuron();
        testneuron2 = new Neuron();
        testneuron3 = new Neuron();
        testneuron4 = new Neuron();
        testlayer1 = new Layer();
        testlayer2 = new Layer();
        testlayer1.addNeuron(testneuron1);
        testlayer1.addNeuron(testneuron2);
        //testlayer1.addNeuron(testneuron3);
        testlayer2.addNeuron(testneuron4);
        testneuron4.addInputConnection(testneuron1, .5);
        testneuron1.addOutputConnection(testneuron4.getConnectionFrom(testneuron1));
        testneuron4.addInputConnection(testneuron2, .5);
        testneuron2.addOutputConnection(testneuron4.getConnectionFrom(testneuron2));

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testaddNeuron() {
        Neuron[] lst = testlayer1.getNeurons();
        assertEquals(testneuron2, lst[1]);
    }

    @Test
    public void testaddNeuronWithIndex() {
        testlayer1.addNeuron(1, testneuron3);
        Neuron[] lst = testlayer1.getNeurons();
        assertEquals(testneuron3, lst[1]);
    }

    @Test
    public void testRemoveNeuron() {
        testlayer1.removeNeuron(testneuron1);
        testlayer1.removeNeuron(testneuron2);
        assertEquals(0, testlayer1.getNeuronsCount());
    }

    @Test
    public void testSetNeuron() {
        testlayer1.setNeuron(1, testneuron3);
        Neuron[] lst = testlayer1.getNeurons();
        assertEquals(testneuron3, lst[1]);
    }

    @Test
    public void testremoveNeuronAt() {
        testlayer1.removeNeuronAt(1);
        testlayer1.removeNeuronAt(0);
        assertEquals(0, testlayer1.getNeuronsCount());
    }

    @Test
    public void testgetNeuronAt() {
        assertEquals(testlayer1.getNeuronAt(0), testneuron1);
    }

    @Test
    public void testindexOf() {
        assertEquals(testlayer1.indexOf(testneuron1), 0);
    }

    @Test
    public void testgetNeuronsCount() {
        assertEquals(testlayer1.getNeuronsCount(), 2);
        assertEquals(testlayer2.getNeuronsCount(), 1);
    }

    @Test
    public void testcalculate() {
        testlayer1.calculate();
        assertEquals(0, testneuron1.getOutput(), .0001);
    }

    @Test
    public void testreset() {
        testneuron1.setInput(5d);
        testneuron1.setOutput(6d);
        assertEquals(6d, testneuron1.getOutput(), .001);
        testlayer1.reset();
        assertEquals(0d, testneuron1.getNetInput(), .0001);
        assertEquals(0d, testneuron1.getOutput(), .0001);
    }

    @Test
    public void testinitializeweights_double() {
        testlayer2.initializeWeights(0d);
        assertEquals(0d, testneuron4.getWeights()[0].getValue(), .001);
        assertEquals(0d, testneuron4.getWeights()[0].getValue(), .0001);
    }


}