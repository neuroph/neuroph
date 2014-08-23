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
import org.neuroph.nnet.comp.neuron.ThresholdNeuron;

/**
 * Perceptron learning rule for perceptron neural networks.
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class PerceptronLearning extends LMS {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Creates new PerceptronLearning instance
	 */
        public PerceptronLearning() {
            super();
        }


	/**
	 * This method implements weights update procedure for the single neuron
	 * In addition to weights change in LMS it applies change to neuron's threshold
         *
	 * @param neuron
	 *            neuron to update weights
	 */
        @Override
	public void updateNeuronWeights(Neuron neuron) {
                // adjust the input connection weights with method from superclass
                super.updateNeuronWeights(neuron);

                // and adjust the neurons threshold
                ThresholdNeuron thresholdNeuron = (ThresholdNeuron)neuron;
                // get neurons error
                double neuronError = thresholdNeuron.getError();
                // get the neurons threshold
                double thresh = thresholdNeuron.getThresh();
                // calculate new threshold value
                thresh = thresh - this.learningRate * neuronError;
                // apply the new threshold
                thresholdNeuron.setThresh(thresh);
	}

}