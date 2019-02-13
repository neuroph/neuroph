/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.neuroph.nnet.learning;

import java.util.List;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

/**
 * Backpropagation learning rule with momentum.
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MomentumBackpropagation extends BackPropagation {

    /**
     * The class fingerprint that is set to indicate serialization compatibility
     * with a previous version of the class.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Momentum factor
     */
    protected double momentum = 0.25d;

    /**
     * Creates new instance of MomentumBackpropagation learning
     */
    public MomentumBackpropagation() {
        super();
    }

    /**
     * This method implements weights adjustment for the hidden layers
     * Uses parallel processing on each layer with 100 or more neurons and a regular loop if less.
     */
    protected void calculateErrorAndUpdateHiddenNeurons() {
        List<Layer> layers = neuralNetwork.getLayers();
        for (int layerIdx = layers.size() - 2; layerIdx > 0; layerIdx--) {
            List<Neuron> layerNeurons = layers.get(layerIdx).getNeurons();
            if(layerNeurons.size() >= 100) {
				layerNeurons.parallelStream().forEach(neuron -> {
	                // calculate the neuron's error (delta)
	                double delta = calculateHiddenNeuronError(neuron);
	                neuron.setDelta(delta);
	                calculateWeightChanges(neuron);
	            });
            } else {
            	for(Neuron neuron : layerNeurons) {
            		// calculate the neuron's error (delta)
            		double delta = calculateHiddenNeuronError(neuron);
            		neuron.setDelta(delta);
            		calculateWeightChanges(neuron);
            	}
            }
        } // for
    }

    /**
     * This method implements weights update procedure for the single neuron for
     * the back propagation with momentum factor
     *
     * @param neuron neuron to update weights
     */
    @Override
    public void calculateWeightChanges(Neuron neuron) {
        for (Connection connection : neuron.getInputConnections()) {
            double input = connection.getInput();
            if (input == 0) {
                continue;
            }

            // get the error for specified neuron,
            double neuronDelta = neuron.getDelta();

            // tanh can be used to minimise the impact of big error values, which can cause network instability
            // suggested at https://sourceforge.net/tracker/?func=detail&atid=1107579&aid=3130561&group_id=238532
            // double neuronError = Math.tanh(neuron.getError());

            Weight<MomentumTrainingData> weight = connection.getWeight();
            MomentumTrainingData weightTrainingData = weight.getTrainingData();

            //double currentWeightValue = weight.getValue();
            double weightChange = -learningRate * neuronDelta * input + momentum * weightTrainingData.previousWeightChange;
            weightTrainingData.previousWeightChange = weight.weightChange;


            // if the learning is in batch mode apply the weight change immediately
            if (isBatchMode() == false) {
                weight.weightChange = weightChange;
            } else { // otherwise, sum the weight changes and apply them after at the end of epoch
                weight.weightChange += weightChange;
            }
        }
    }

    /**
     * Returns the momentum factor
     *
     * @return momentum factor
     */
    public double getMomentum() {
        return momentum;
    }

    /**
     * Sets the momentum factor
     *
     * @param momentum momentum factor
     */
    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public static class MomentumTrainingData {
        public double previousWeightChange;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // create MomentumTrainingData objects that will be used during the training to store previous weight value
        for (Layer layer : neuralNetwork.getLayers()) {
            for (Neuron neuron : layer.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    connection.getWeight().setTrainingData(new MomentumTrainingData());
                }
            } // for
        } // for
    }
}
