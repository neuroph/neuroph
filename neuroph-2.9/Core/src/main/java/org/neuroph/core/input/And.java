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
 * Performs logic AND operation on input vector.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class And extends InputFunction implements Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param inputVector Input values >= 0.5d are considered true, otherwise false.
	 */
	public double getOutput(List<Connection> inputConnections) {

                if (inputConnections.size() == 0) return 0d;

		boolean output = true;
		
		for (Connection connection : inputConnections) {
			output = output && (connection.getInput() >= 0.5d);
		}

		return output ? 1d : 0d;
	}

}
