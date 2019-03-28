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
import org.neuroph.core.data.DataSetRow;

/**
 * Supervised hebbian learning rule.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class SupervisedHebbianLearning extends LMS {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new instance of SupervisedHebbianLearning algorithm
	 */
	public SupervisedHebbianLearning() {
		super();
	}


	/**
	 * Learn method override without network error and iteration limit
	 * Implements just one pass through the training set Used for testing -
	 * debugging algorithm
	 * 
	 * public void learn(TrainingSet trainingSet) { Iterator
	 * iterator=trainingSet.iterator(); while(iterator.hasNext()) {
	 * SupervisedTrainingElement trainingElement =
	 * (SupervisedTrainingElement)iterator.next();
	 * this.learnPattern(trainingElement); } }
	 */


    //TODO --- Check if this breaks something...??????
//	/**
//	 * Trains network with the pattern from the specified training element
//	 *
//	 * @param trainingSetRow
//	 *            a single  data set row to learn which contains input and desired output patterns (arrays)
//	 */
//	@Override
//	protected void learnPattern(DataSetRow trainingSetRow) {
//                double[] input = trainingSetRow.getInput();
//                this.neuralNetwork.setInput(input); // set network input
//                this.neuralNetwork.calculate(); // calculate the network
//                double[] output = this.neuralNetwork.getOutput(); // get actual network output
//                double[] desiredOutput = trainingSetRow.getDesiredOutput();
//
//                double[] outputError = this.calculateOutputError(desiredOutput, output); // calculate error as difference between desired and actual
//                this.addToSquaredErrorSum(outputError); // add error to total MSE
//
//                this.calculateWeightChanges(desiredOutput); // apply the weights update procedure
//	}

	/**
	 * This method implements weight update procedure for the whole network for
	 * this learning rule
	 * 
	 * @param desiredOutput
	 *            desired network output
	 */
	@Override
	protected void calculateWeightChanges(double[] desiredOutput) {
		int i = 0;
		for (Neuron neuron : neuralNetwork.getOutputNeurons()) {
			this.updateNeuronWeights(neuron, desiredOutput[i]);
			i++;
		}

	}

	/**
	 * This method implements weights update procedure for the single neuron
	 * 
	 * @param neuron
	 *            neuron to update weights
	 *        desiredOutput
	 *	      desired output of the neuron
	 */
	protected void updateNeuronWeights(Neuron neuron, double desiredOutput) {
		for (Connection connection : neuron.getInputConnections()) {
			double input = connection.getInput();
			double deltaWeight = input * desiredOutput * this.learningRate;
			connection.getWeight().inc(deltaWeight);
		}
	}
}
