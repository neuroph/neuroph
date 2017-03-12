package org.neuroph.contrib.learning;


import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.TransferFunction;

/**
 * Activation function which enforces that output neurons have probability distribution (sum of all outputs is one)
 */
public class SoftMax extends TransferFunction {

    private Layer layer;
    private double totalLayerInput;

    public SoftMax(final Layer layer) {
        this.layer = layer;
    }


    @Override
    public double getOutput(double netInput) {
        totalLayerInput = 0;
                // add max here for numerical stability - find max netInput for all neurons in this lyer
        double max = 0;
        
        for (Neuron neuron : layer.getNeurons()) {
            totalLayerInput += Math.exp(neuron.getNetInput()-max);
        }

        output = Math.exp(netInput-max) / totalLayerInput;
        return output;
    }

    @Override
    public double getDerivative(double net) {
        return output * (1d - output); // ovaj izvod nije dobar vec zavisi koji je neuron - ima dva izvoda
        // -yi * yj
    }
}
