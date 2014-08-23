package org.neuroph.nnet.learning;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

/**
 * Resilient Propagation learning rule used for Multi Layer Perceptron neural networks.
 * Its one of the most efficent learning rules for this type of networks, and it does not require 
 * setting of learning rule parameter.
 * @author Borislav Markov
 * @author Zoran Sevarac
 */
public class ResilientPropagation extends BackPropagation {

    private double decreaseFactor = 0.5;
    private double increaseFactor = 1.2;
    private double initialDelta = 0.1;
    private double maxDelta = 1;
    private double minDelta = 1e-6;
    private static final double ZERO_TOLERANCE = 1e-27; // the lowest limit when something is considered to be zero

    public ResilientPropagation() {
        super();
        super.setBatchMode(true);
    }

    private int sign(final double value) {
        if (Math.abs(value) < ZERO_TOLERANCE) {
            return 0;
        } else if (value > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    protected void onStart() {
        super.onStart(); // init all stuff from superclasses

        // create ResilientWeightTrainingtData objects that will hold additional data (resilient specific) during the training 
        for (Layer layer : this.neuralNetwork.getLayers()) {
            for (Neuron neuron : layer.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    connection.getWeight().setTrainingData(new ResilientWeightTrainingtData());
                }
            }
        }
    }
    
    /**
     * Calculate and sum gradients for each neuron's weight, the actual weight update is done in batch mode
     * @see ResilientPropagation#resillientWeightUpdate(org.neuroph.core.Weight) 
     */
    @Override
    public void updateNeuronWeights(Neuron neuron) {
        for (Connection connection : neuron.getInputConnections()) {
            double input = connection.getInput();
            if (input == 0) {
                continue;
            }

            // get the error for specified neuron,
            double neuronError = neuron.getError();
            // get the current connection's weight
            Weight weight = connection.getWeight();
            // ... and get the object that stores reislient training data for that weight
            ResilientWeightTrainingtData weightData = (ResilientWeightTrainingtData) weight.getTrainingData();

            // calculate the weight gradient (and sum gradients since learning is done in batch mode)
            weightData.gradient += neuronError * input;
        }
    }

    @Override
    protected void doBatchWeightsUpdate() {
        // iterate layers from output to input
        Layer[] layers = neuralNetwork.getLayers();
        for (int i = neuralNetwork.getLayersCount() - 1; i > 0; i--) {            
            // iterate neurons at each layer
            for (Neuron neuron : layers[i].getNeurons()) {
                // iterate connections/weights for each neuron
                for (Connection connection : neuron.getInputConnections()) {
                    // for each connection weight apply following changes
                    Weight weight = connection.getWeight();
                    resillientWeightUpdate(weight);
                }
            }
        }
    }

    /**
     * Weight update by done by ResilientPropagation  learning rule
     * Executed at the end of epoch (in batch mode)
     * @param weight 
     */
    protected void resillientWeightUpdate(Weight weight) {
        // get resilient training data for the current weight
        ResilientWeightTrainingtData weightData = (ResilientWeightTrainingtData) weight.getTrainingData();

        // multiply the current and previous gradient, and take the sign. 
        // We want to see if the gradient has changed its sign.            
        int gradientSignChange = sign(weightData.previousGradient * weightData.gradient);

        double weightChange = 0; // weight change to apply (delta weight)
        double delta; //  adaptation factor

        if (gradientSignChange > 0) {
            // if the gradient has retained its sign, then we increase delta (adaptation factor) so that it will converge faster
            delta = Math.min(
                    weightData.previousDelta * increaseFactor,
                    maxDelta);
            //  weightChange = -sign(weightData.gradient) * delta; // if error is increasing (gradient is positive) then subtract delta, if error is decreasing (gradient negative) then add delta
            // note that our gradient has different sign eg. -dE_dw so we omit the minus here
            weightChange = sign(weightData.gradient) * delta;
            weightData.previousDelta = delta;
        } else if (gradientSignChange < 0) {
            // if gradientSignChange<0, then the sign has changed, and the last weight change was too big                
            delta = Math.max(
                    weightData.previousDelta * decreaseFactor,
                    minDelta);
            // weightChange = - weightData.previousDelta;// 0;// -delta  - weightData.previousDelta; // ovo je problematicno treba da bude weightChange          
            weightChange = -weightData.previousWeightChange; // if it skipped min in previous step go back
            // avoid double punishment
            weightData.gradient = 0;
            weightData.previousGradient = 0;

            //move values in the past
            weightData.previousDelta = delta;
        } else if (gradientSignChange == 0) {
            // if gradientSignChange==0 then there is no change to the delta
            delta = weightData.previousDelta;
            //delta = weightData.previousGradient; // note that encog does this
            weightChange = sign(weightData.gradient) * delta;
        }

        weight.value += weightChange;
        weightData.previousWeightChange = weightChange;
        weightData.previousGradient = weightData.gradient; // as in moveNowValuesToPreviousEpochValues
        weightData.gradient = 0;
    }

    public double getDecreaseFactor() {
        return decreaseFactor;
    }

    public void setDecreaseFactor(double decreaseFactor) {
        this.decreaseFactor = decreaseFactor;
    }

    public double getIncreaseFactor() {
        return increaseFactor;
    }

    public void setIncreaseFactor(double increaseFactor) {
        this.increaseFactor = increaseFactor;
    }

    public double getInitialDelta() {
        return initialDelta;
    }

    public void setInitialDelta(double initialDelta) {
        this.initialDelta = initialDelta;
    }

    public double getMaxDelta() {
        return maxDelta;
    }

    public void setMaxDelta(double maxDelta) {
        this.maxDelta = maxDelta;
    }

    public double getMinDelta() {
        return minDelta;
    }

    public void setMinDelta(double minDelta) {
        this.minDelta = minDelta;
    }
    
    public class ResilientWeightTrainingtData {
        public double gradient;
        public double previousGradient;
        public double previousWeightChange;
        public double previousDelta = initialDelta;
    }
}
