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

package org.neuroph.nnet.comp;

import org.neuroph.nnet.comp.neuron.DelayedNeuron;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;

/**
 * Represents the connection between neurons which can delay signal.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class DelayedConnection extends Connection {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Delay factor for this conection
	 */
	private int delay = 0;

	/**
	 * Creates an instance of delayed connection to cpecified neuron and
	 * with specified weight
         * @param fromNeuron neuron to connect (source neuron)
	 * @param toNeuron neuron to connect to (destination neuron)
	 * @param weightVal weight value for the connection
	 * @param delay delay for the connection
	 */
	public DelayedConnection(Neuron fromNeuron, Neuron toNeuron,  double weightVal, int delay) {
		super(fromNeuron, toNeuron, weightVal);
		this.delay = delay;
	}

	/**
	 * Returns delay value for this connection
	 * @return delay value for this connection
	 */
	public int getDelay() {
		return this.delay;
	}

	/**
	 * Sets delay value for this connection
	 * @param delay value for this connection
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * Gets delayed input through this connection
	 * @return delayed output from connected neuron
	 */
	@Override
	public double getInput() {
		if (this.fromNeuron instanceof DelayedNeuron)
			return ((DelayedNeuron) this.fromNeuron).getOutput(delay);
		else
			return this.fromNeuron.getOutput();
	}

}
