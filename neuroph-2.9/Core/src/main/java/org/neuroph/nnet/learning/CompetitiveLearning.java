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


import java.util.List;
import org.neuroph.core.Connection;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.UnsupervisedLearning;
import org.neuroph.nnet.comp.layer.CompetitiveLayer;
import org.neuroph.nnet.comp.neuron.CompetitiveNeuron;


/**
 * Competitive learning rule.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class CompetitiveLearning extends UnsupervisedLearning {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new instance of CompetitiveLearning
	 */
	public CompetitiveLearning() {
		super();
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
		stopLearning(); // stop learning ahter one learning epoch - because we dont have any stopping criteria  for unsupervised...
	}		
	
	/**
	 * Adjusts weights for the winning neuron
	 */
	protected void updateNetworkWeights() {
		// find active neuron in output layer
		// TODO : change idx, in general case not 1
		CompetitiveNeuron winningNeuron = ((CompetitiveLayer) neuralNetwork
				.getLayerAt(1)).getWinner();

		List<Connection> inputConnections = winningNeuron
				.getConnectionsFromOtherLayers();

		for(Connection connection : inputConnections) {
			double weight = connection.getWeight().getValue();
			double input = connection.getInput();
			double deltaWeight = this.learningRate * (input - weight);
			connection.getWeight().inc(deltaWeight);			
		}
	}

}
