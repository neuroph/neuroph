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

package org.neuroph.nnet.comp.neuron;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.neuroph.core.Neuron;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;

/**
 * Provides behaviour for neurons with delayed output.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class DelayedNeuron extends Neuron {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Output history for this neuron
	 */
	protected transient List<Double> outputHistory;

	/**
	 * Creates an instance of neuron which can delay output
	 * @param inputFunction neuron input function
	 * @param transferFunction neuron transfer function
	 */
	public DelayedNeuron(InputFunction inputFunction,
			TransferFunction transferFunction) {
		super(inputFunction, transferFunction);
		outputHistory = new ArrayList<Double>(5); // default delay buffer size is 5
		outputHistory.add(new Double(0));
	}

	@Override
	public void calculate() {
		super.calculate();
		outputHistory.add(0, new Double(this.output));
		if (outputHistory.size() > 5)
			outputHistory.remove(5);
	}

	/**
	 * Returns neuron output with the specified delay
	 * @param delay output delay
	 * @return neuron output at (t-delay) moment
	 */
	public double getOutput(int delay) {
		return outputHistory.get(delay).doubleValue();
	}
        
        
        private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            outputHistory = new ArrayList<>(5);
        }        

}
