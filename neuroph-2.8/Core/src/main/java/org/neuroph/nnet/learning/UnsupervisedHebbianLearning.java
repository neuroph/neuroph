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
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.UnsupervisedLearning;

/**
 * Unsupervised hebbian learning rule.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class UnsupervisedHebbianLearning extends UnsupervisedLearning {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new instance of UnsupervisedHebbianLearning algorithm
	 */
	public UnsupervisedHebbianLearning() {
		super();
		this.setLearningRate(0.1d);
	}


	/**
	 * This method does one learning epoch for the unsupervised learning rules.
	 * It iterates through the training set and trains network weights for each
	 * element. Stops learning after one epoch.
	 * 
	 * @param trainingSet
	 *            training set for training network
	 */
	@Override
	public void doLearningEpoch(DataSet trainingSet) {
		super.doLearningEpoch(trainingSet);
		stopLearning(); // stop learning ahter one learning epoch -- why ? - because we dont have any other stopping criteria for this - must limit the iterations
	}

	/**
	 * Adjusts weights for the output neurons
	 */
        @Override
	protected void updateNetworkWeights() {
		for (Neuron neuron : neuralNetwork.getOutputNeurons()) {
			this.updateNeuronWeights(neuron);
		}
	}

	/**
	 * This method implements weights update procedure for the single neuron
	 * 
	 * @param neuron
	 *            neuron to update weights
	 */
	protected void updateNeuronWeights(Neuron neuron) {
		double output = neuron.getOutput();

		for (Connection connection : neuron.getInputConnections()) {
			double input = connection.getInput();
			double deltaWeight = input * output * this.learningRate;
			connection.getWeight().inc(deltaWeight);
		}
	}
}
