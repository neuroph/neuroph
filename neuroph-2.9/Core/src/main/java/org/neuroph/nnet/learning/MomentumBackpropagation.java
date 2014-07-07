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
     * This method implements weights update procedure for the single neuron for
     * the back propagation with momentum factor
     *
     * @param neuron neuron to update weights
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

            // tanh can be used to minimise the impact of big error values, which can cause network instability
            // suggested at https://sourceforge.net/tracker/?func=detail&atid=1107579&aid=3130561&group_id=238532
            // double neuronError = Math.tanh(neuron.getError());

            Weight weight = connection.getWeight();
            MomentumWeightTrainingData weightTrainingData = (MomentumWeightTrainingData) weight.getTrainingData();

            //double currentWeightValue = weight.getValue();
            double previousWeightValue = weightTrainingData.previousValue;
            double weightChange = this.learningRate * neuronError * input
                    + momentum * (weight.value - previousWeightValue);
            // save previous weight value
            //weight.getTrainingData().set(TrainingData.PREVIOUS_WEIGHT, currentWeightValue);
            weightTrainingData.previousValue = weight.value;


            // if the learning is in batch mode apply the weight change immediately
            if (this.isInBatchMode() == false) {
                weight.weightChange = weightChange;
                weight.value += weightChange;
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

    public class MomentumWeightTrainingData {

        public double previousValue;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // create MomentumWeightTrainingData objects that will be used during the training to store previous weight value
        for (Layer layer : this.neuralNetwork.getLayers()) {
            for (Neuron neuron : layer.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    connection.getWeight().setTrainingData(new MomentumWeightTrainingData());
                }
            } // for
        } // for        
    }
}