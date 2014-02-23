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

package org.neuroph.nnet.comp.layer;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.CompetitiveNeuron;
import org.neuroph.util.NeuronProperties;

/**
 * Represents layer of competitive neurons, and provides methods for competition.
 * 
 * TODO: competitive learning 3. training dw=n(i-w)
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class CompetitiveLayer extends Layer {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;
	
	/**
	 * Max iterations for neurons to compete
	 * This is neccesery to limit, otherwise if there is no winner there will
	 * be endless loop.
	 */
	private int maxIterations = 100;
	
	/**
	 * The competition winner for this layer
	 */
	private CompetitiveNeuron winner;

	/**
	 * Create an instance of CompetitiveLayer with the specified number of
	 * neurons with neuron properties
	 * @param neuronNum neuron number in this layer
	 * @param neuronProperties properties for the nurons in this layer
	 */
	public CompetitiveLayer(int neuronNum, NeuronProperties neuronProperties) {
		super(neuronNum, neuronProperties);
	}

	/**
	 * Performs calculaton for all neurons in this layer
	 */
	@Override
	public void calculate() {
		boolean hasWinner = false;
		
		int iterationsCount = 0;
		
		while (!hasWinner) {
			int fireingNeurons = 0;
			for(Neuron neuron : this.getNeurons()) {
				neuron.calculate();
				if (neuron.getOutput() > 0)
					fireingNeurons += 1;
			} // for
			
			if (iterationsCount > this.maxIterations) break;
			
			if (fireingNeurons == 1)
				hasWinner = true;
			iterationsCount++;
			
		} // while !done

		if (hasWinner) { 
			// now set reference to winner
			double maxOutput = Double.MIN_VALUE;

			for(Neuron neuron : this.getNeurons()) {	
				CompetitiveNeuron cNeuron = (CompetitiveNeuron)neuron;
				cNeuron.setIsCompeting(false); // turn off competing mode
				if (cNeuron.getOutput() > maxOutput) {
					maxOutput = cNeuron.getOutput();
					this.winner = cNeuron;
				}
			}
		}
		
	}

	/**
	 * Returns the winning neuron for this layer
	 * @return winning neuron for this layer
	 */
	public CompetitiveNeuron getWinner() {
		return this.winner;
	}

	/**
	 * Returns the maxIterations setting for this layer
	 * @return maxIterations setting for this layer
	 */
	public int getMaxIterations() {
		return maxIterations;
	}
	
	/**
	 * Sets max iterations for neurons to compete in this layer
	 * @param maxIterations  max iterations for neurons to compete in this layer
	 */
	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}
	
	

}
