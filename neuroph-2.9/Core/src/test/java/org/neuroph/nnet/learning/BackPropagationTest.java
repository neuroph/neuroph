package org.neuroph.nnet.learning;

import junit.framework.TestCase;
import org.junit.Before;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.transfer.Linear;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class BackPropagationTest extends TestCase {

    private BackPropagation backPropagation;
    private Neuron neuron;
    NeuralNetwork<BackPropagation> neuralNetwork;

    Linear transfer;

    @Before
    public void setUp() {
        neuron = new Neuron();
        transfer = new Linear();
        transfer.setSlope(0.5D);
        neuralNetwork = new NeuralNetwork<>();
        neuralNetwork.setOutputNeurons(new Neuron[]{neuron});
        backPropagation = spy(new BackPropagation());
        backPropagation.setNeuralNetwork(neuralNetwork);
    }

    public void testUpdateNetworkWeights() throws Exception {

    }

    public void testCalculateErrorAndUpdateOutputNeuronsOnZeroError() throws Exception {
        backPropagation.calculateErrorAndUpdateOutputNeurons(new double[]{0D});
        assertEquals(0D, neuron.getError());
    }

    public void testCalculateErrorAndUpdateOutputNeuronsNonZeroError() throws Exception {
        neuron.setTransferFunction(transfer);
        neuron.setInput(0.25D);
        backPropagation.calculateErrorAndUpdateOutputNeurons(new double[]{0.35D});
        assertEquals(0.175D, neuron.getError());
    }

    public void testCalculateErrorAndUpdateHiddenNeurons() throws Exception {
        doReturn(0.5D).when(backPropagation).calculateHiddenNeuronError(any(Neuron.class));
        Layer neuralLayer = new Layer();
        neuralLayer.addNeuron(neuron);
        neuralNetwork.addLayer(new Layer());
        neuralNetwork.addLayer(neuralLayer);
        neuralNetwork.addLayer(new Layer());
        backPropagation.calculateErrorAndUpdateHiddenNeurons();
        assertEquals(0.5D, neuron.getError());
    }

    public void testCalculateHiddenNeuronError() throws Exception {
        Neuron connectedNeuron = new Neuron();
        Weight weight = new Weight();
        double startValue = weight.value;
        neuron.addInputConnection(new Connection(connectedNeuron, neuron, weight));
        neuron.setError(0.5D);
        connectedNeuron.setTransferFunction(transfer);
        assertEquals(0.5D * startValue * 0.5, backPropagation.calculateHiddenNeuronError(connectedNeuron));
    }
}