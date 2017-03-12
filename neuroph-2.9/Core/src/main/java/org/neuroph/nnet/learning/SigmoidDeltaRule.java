/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.nnet.learning;

import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.TransferFunction;

/**
 * Delta rule learning algorithm for perceptrons with sigmoid (or any other diferentiable continuous) functions.
 *
 * TODO: Rename to DeltaRuleContinuous (ContinuousDeltaRule) or something like that, but that will break backward compatibility,
 * posibly with backpropagation which is the most used
 *
 * @see LMS
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class SigmoidDeltaRule extends LMS {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new SigmoidDeltaRule
	 */
	public SigmoidDeltaRule() {
		super();
	}

	/**
	 * This method implements weight update procedure for the whole network for
	 * this learning rule
	 * 
	 * @param outputError
	 *            output error vector
	 */
	@Override
	protected void calculateWeightChanges(double[] outputError) {
		int i = 0;
                // for all output neurons
		for(Neuron neuron : neuralNetwork.getOutputNeurons()) {
                        // if error is zero, just set zero error and continue to next neuron
			if (outputError[i] == 0) {
				neuron.setError(0);
                                i++;
				continue;
			}
			
                        // otherwise calculate and set error/delta for the current neuron
			TransferFunction transferFunction = neuron.getTransferFunction();
			double neuronInput = neuron.getNetInput();
			double delta = outputError[i] * transferFunction.getDerivative(neuronInput); // delta = (d-y)*df(net)
			neuron.setError(delta);
                        
                        // and update weights of the current neuron
			this.updateNeuronWeights(neuron);				
			i++;
		} // for				
	}

}
