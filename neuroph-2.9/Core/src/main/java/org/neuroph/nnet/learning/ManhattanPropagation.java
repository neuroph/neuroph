package org.neuroph.nnet.learning;

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

/**
 *
 * @author Zoran Sevarac <zoran.sevarac@deepnetts.com>
 */
public class ManhattanPropagation extends BackPropagation {
    // dont use gradient value, only sign and fixed step


     @Override
     public void calculateWeightChanges(Neuron neuron) {
        // get the error(delta) for specified neuron,
        double delta = neuron.getDelta();

        // iterate through all neuron's input connections
        for (Connection connection : neuron.getInputConnections()) {
            // get the input from current connection
            double input = connection.getInput();
            // calculate the weight change
            // gradient = delta * input
            double weightChange = -learningRate * Math.signum(delta * input); // da li treba - ili da bude +?

            // get the connection weight
            Weight weight = connection.getWeight();
            // if the learning is in online mode (not batch) apply the weight change immediately
            if (!this.isBatchMode()) {
                weight.weightChange = weightChange;
            } else { // otherwise its in batch mode, accumulate  weight changes and apply them later, after the current epoch (see SupervisedLearning.doLearningEpoch method)
                weight.weightChange += weightChange;
            }
        }
    }

}
