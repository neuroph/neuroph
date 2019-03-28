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

/**
 *<pre>
 * Neuron's input function. It has two subcomponents:
 * 
 * weightsFunction - which performs operation with input and weight vector
 * summingFunction - which performs operation with the resulting vector from weightsFunction
 * 
 * InputFunction implements the following behaviour:
 * output = summingFunction(weightsFunction(inputs))
 * 
 * Different neuron input functions can be created by setting different weights and summing functions.
 *</pre>
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see org.neuroph.core.Neuron
 */
abstract public class InputFunction implements Serializable {   // this should be functional interface!!!!!!!!
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 2L;
	

	/**
	 * Returns ouput value of this input function for the given neuron inputs
	 * 
	 * @param inputConnections
	 *            neuron's input connections
	 * @return input total net input
	 */
	abstract public double getOutput(List<Connection> inputConnections);

}
