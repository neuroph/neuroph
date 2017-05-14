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
 * Hebbian-like learning rule for Outstar network.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class OutstarLearning extends UnsupervisedHebbianLearning {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Creates new instance of OutstarLearning algorithm
	 */
	public OutstarLearning() {
		super();
		this.setLearningRate(0.1);
	}

	
	/**
	 * This method implements weights update procedure for the single neuron
	 * 
	 * @param neuron
	 *            neuron to update weights for
	 */
	@Override
	protected void updateNeuronWeights(Neuron neuron) {
		double output = neuron.getOutput();
		for (Connection connection : neuron.getInputConnections()) {
			double input = connection.getInput();
			double weight = connection.getWeight().getValue();
			double deltaWeight = this.learningRate * input * (output-weight);
			connection.getWeight().inc(deltaWeight);
		}
	}	
	

}
