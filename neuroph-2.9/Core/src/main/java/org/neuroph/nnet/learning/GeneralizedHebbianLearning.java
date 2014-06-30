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

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;

/**
 * A variant of Hebbian learning called Generalized Hebbian learning.
 * Weight change is calculated using formula
 *      deltaWeight = (input - netInput) * output * learningRate
 * @author Zoran Sevarac <sevarac@gmail.com> 
 */
public class GeneralizedHebbianLearning extends UnsupervisedHebbianLearning {
    
	/**
	 * This method implements weights update procedure for the single neuron
	 * 
	 * @param neuron
	 *            neuron to update weights
	 */
	@Override
	protected void updateNeuronWeights(Neuron neuron) {
		double output = neuron.getOutput();
		for(Connection connection : neuron.getInputConnections()) {
			double input = connection.getInput();
                        double netInput = neuron.getNetInput();
			double deltaWeight = (input - netInput) * output * this.learningRate; // is it right to use netInput here?
			connection.getWeight().inc(deltaWeight);
		}
	}    
    
}
