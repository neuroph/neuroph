package org.neuroph.core;

import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author Shivanth
 */
public class NeuralNetworkTest {

    private NeuralNetwork neuralNetwork;
    private Layer layer1, layer2, layer3;
    private Neuron neurons[];

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        neuralNetwork = new NeuralNetwork();
        neurons = new Neuron[6];
        layer1 = new Layer();
        layer2 = new Layer();
        layer3 = new Layer();
        for (int i = 0; i < 6; i++) {
            neurons[i] = new Neuron();
        }
        layer1.addNeuron(neurons[0]);
        layer2.addNeuron(neurons[1]);
        layer3.addNeuron(neurons[2]);
        layer1.addNeuron(neurons[3]);
        layer2.addNeuron(neurons[4]);
        layer3.addNeuron(neurons[5]);
        neurons[1].addInputConnection(neurons[0]);
        neurons[4].addInputConnection(neurons[0]);
        neurons[1].addInputConnection(neurons[3]);
        neurons[1].addInputConnection(neurons[3]);
        neuralNetwork.addLayer(layer1);
        neuralNetwork.addLayer(layer2);
        neuralNetwork.addLayer(layer3);
//        neuralNetwork.initializeWeights(2.5d);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPrelims() {
        assertEquals(3, neuralNetwork.getLayers().length);
    }

    @Test
    public void testInputNeurons() {
        neuralNetwork.setInputNeurons(layer1.getNeurons());
        assertEquals(2, neuralNetwork.getInputNeurons().length);

    }

    @Test
    public void testOutputNeuros() {
        neuralNetwork.setOutputNeurons(layer3.getNeurons());
        assertEquals(2, neuralNetwork.getOutputNeurons().length);
    }
}
