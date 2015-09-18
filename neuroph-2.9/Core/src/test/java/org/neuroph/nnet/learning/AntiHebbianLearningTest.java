package org.neuroph.nnet.learning;

import junit.framework.TestCase;
import org.junit.Before;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

public class AntiHebbianLearningTest extends TestCase {

    private Neuron testNeuron;
    private AntiHebbianLearning antiHebbianLearning = new AntiHebbianLearning();
    private Weight weight = new Weight();
    private double startValue = weight.value;

    @Before
    public void setUp() {
        this.testNeuron = new Neuron();
        this.testNeuron.setOutput(1D);
        Neuron neuron = new Neuron();
        neuron.setOutput(0.5D);
        testNeuron.addInputConnection(new Connection(neuron, testNeuron, weight));
    }

    public void testUpdateNeuronWeights() throws Exception {
        antiHebbianLearning.updateNeuronWeights(testNeuron);
        assertEquals(startValue - 0.5D * 1D * antiHebbianLearning.getLearningRate(),
                testNeuron.getWeights()[0].getValue());
    }
}