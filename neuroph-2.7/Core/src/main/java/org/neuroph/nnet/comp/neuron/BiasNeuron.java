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

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.exceptions.NeurophException;

/**
 * Neuron with constant high output (1), used as bias
 *
 * @see Neuron
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class BiasNeuron extends Neuron {

	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 1L;

    	/**
	 * Creates an instance of BiasedNeuron.
	 */
	public BiasNeuron() {
                super();
	}

        @Override
        public double getOutput() {
            return 1;
        }

        @Override
        public void addInputConnection(Connection connection) {
          
        }

        @Override
        public void addInputConnection(Neuron fromNeuron, double weightVal) { 
          
        }

        @Override
        public void addInputConnection(Neuron fromNeuron){ 
          
        }



}
