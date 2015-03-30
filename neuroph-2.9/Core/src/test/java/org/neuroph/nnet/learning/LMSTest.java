package org.neuroph.nnet.learning;

import junit.framework.TestCase;
import org.junit.Before;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

public class LMSTest extends TestCase {
    private LMS lms;
    private Neuron neuron = new Neuron();
    private Weight weight;
    private double startValue;
    private double expectedWeightChange;

    @Before
    public void setUp() {
        lms = new LMS();
        neuron = new Neuron();
        Neuron connectedNeuron = new Neuron();
        weight = new Weight();
        startValue = weight.value;
        expectedWeightChange = lms.getLearningRate() * 1D * 0.5D;
        neuron.setError(1D);
        connectedNeuron.setOutput(0.5D);
        neuron.addInputConnection(new Connection(connectedNeuron, neuron, weight));
    }

    public void testUpdateNeuronWeightsInBatchMode() {
        lms.setBatchMode(true);
        lms.updateNeuronWeights(neuron);
        assertEquals(expectedWeightChange, weight.weightChange);
        assertEquals(startValue, weight.getValue());
    }

    public void testUpdateNeuronWeightsInOnlineMode() {
        lms.setBatchMode(false);
        lms.updateNeuronWeights(neuron);
        assertEquals(expectedWeightChange, weight.weightChange);
        assertEquals(startValue + expectedWeightChange, weight.getValue());
    }

    public void testUpdateNetworkWeight() {
        NeuralNetwork<LMS> neuralNetwork = new NeuralNetwork<>();
        neuralNetwork.setOutputNeurons(new Neuron[]{neuron});
        lms.setNeuralNetwork(neuralNetwork);
        lms.updateNetworkWeights(new double[]{1D});
        assertEquals(expectedWeightChange, weight.weightChange);
        assertEquals(startValue + expectedWeightChange, weight.getValue());
    }

}