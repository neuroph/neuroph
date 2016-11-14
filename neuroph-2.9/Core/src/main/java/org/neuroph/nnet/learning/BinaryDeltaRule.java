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
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.ThresholdNeuron;

/**
 * Delta rule learning algorithm for perceptrons with step functions.
 *
 * The difference to Perceptronlearning is that Delta Rule calculates error
 * before the non-lnear step transfer function
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class BinaryDeltaRule extends PerceptronLearning {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The errorCorrection parametar of this learning algorithm
	 */
	private double errorCorrection = 0.1;

	/**
	 * Creates new BinaryDeltaRule learning
	 */
	public BinaryDeltaRule() {
		super();
	}


	/**
	 * This method implements weight update procedure for the whole network for
	 * this learning rule
	 *
	 * @param patternError
	 *            single pattern error vector
         *
         * if the output is 0 and required value is 1, increase rthe weights
         * if the output is 1 and required value is 0, decrease the weights
         * otherwice leave weights unchanged
         *
	 */
	@Override
	protected void calculateWeightChanges(double[] patternError) {
		int i = 0;
                List<Neuron> outputNeurons = neuralNetwork.getOutputNeurons();
                for (Neuron outputNeuron : outputNeurons) {
			ThresholdNeuron neuron = (ThresholdNeuron)outputNeuron;
			double outErr = patternError[i];
			double thresh = neuron.getThresh();
			double netInput = neuron.getNetInput();
			double threshError =  thresh - netInput; // distance from zero
                        // use output error to decide weathet to inrease, decrase or leave unchanged weights
                        // add errorCorrection to threshError to move above or below zero
                        double neuronError = outErr * (Math.abs(threshError) + errorCorrection);

                        // use same adjustment principle as PerceptronLearning,
                        // just with different neuronError
                        neuron.setError(neuronError);
			updateNeuronWeights(neuron);

			i++;
		} // for
	}

        /**
	 * Gets the errorCorrection parametar
	 *
	 * @return errorCorrection parametar
	 */
	public double getErrorCorrection() {
		return this.errorCorrection;
	}

	/**
	 * Sets the errorCorrection parametar
	 *
	 * @param errorCorrection
	 *            the value for errorCorrection parametar
	 */
	public void setErrorCorrection(double errorCorrection) {
		this.errorCorrection = errorCorrection;
	}

}