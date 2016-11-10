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

package org.neuroph.core.input;

import java.io.Serializable;
import java.util.List;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

/**
 * Performs the vector difference operation on input and
 * weight vector.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Difference extends InputFunction implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID =21L;
	

    @Override
	public double getOutput(List<Connection> inputConnections) {
		double output = 0d;

                double sum = 0d;
		for(Connection connection : inputConnections) {
			Neuron neuron = connection.getFromNeuron();
			Weight weight = connection.getWeight();
			double diff = neuron.getOutput() - weight.getValue();
                        sum += diff * diff;
		}

                output = Math.sqrt(sum);
                
		return output;
	}

	public double[] getOutput(double[] inputs, double[] weights) {
		double[] output = new double[inputs.length];

		for(int i = 0; i < inputs.length; i++) {
			output[i] = inputs[i] - weights[i];
		}

		return output;
	}

}
