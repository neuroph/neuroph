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

package  org.neuroph.adapters.encog;

import org.neuroph.core.Weight;

/**
 * A subclass of the Weight class.  Allows weights to be stored as elements in a double type array.
 * This allows weights to very quickly be modified, affecting the neural network.
 * 
 * @author Jeff Heaton (http://www.jeffheaton.com)
 * @see FlatNetworkPlugin
 */
public class FlatWeight extends Weight {

	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 1L;
		
	/**
	 * Previous weight value (used by some learning rules like momentum for backpropagation)
	 */
	private transient double previousValue;
	
	private double[] flatWeights;
	
	private int weightIndex;

	/**
	 * Creates an instance of connection weight with random weight value in range [0..1]
	 */
	public FlatWeight(double[] flatWeights, int weightIndex) {
		this.flatWeights = flatWeights;
		this.weightIndex = weightIndex;
		this.setValue( Math.random() - 0.5d );
		this.previousValue = this.getValue();
	}

	/**
	 * Creates an instance of connection weight with the specified weight value
	 * 
	 * @param value
	 *            weight value
	 */
	public FlatWeight(double value) {
		this.setValue( value );
		this.previousValue = this.getValue();
	}

	/**
	 * Increases the weight for the specified amount
	 * 
	 * @param amount
	 *            amount to add to current weight value
	 */
	public void inc(double amount) {
		this.flatWeights[this.weightIndex] += amount;
	}

	/**
	 * Decreases the weight for specified amount
	 * 
	 * @param amount
	 *            amount to subtract from the current weight value
	 */
	public void dec(double amount) {
		this.flatWeights[this.weightIndex] -= amount;
	}

	/**
	 * Sets the weight value
	 * 
	 * @param value
	 *            weight value to set
	 */
	public void setValue(double value) {
		this.flatWeights[this.weightIndex] = value;
	}

	/**
	 * Returns weight value
	 * 
	 * @return value of this weight
	 */
	public double getValue() {
		return this.flatWeights[this.weightIndex];
	}
	
	
	/**
	 * Sets the previous weight value
	 * 
	 * @param previousValue
	 *            weight value to set
	 */
	public void setPreviousValue(double previousValue) {
		this.previousValue = previousValue;
	}
	
	/**
	 * Returns previous weight value
	 * 
	 * @return value of this weight
	 */
	public double getPreviousValue() {
		return this.previousValue;
	}	
	
	

	/**
	 * Returns weight value as String
	 */
	@Override
	public String toString() {
		return Double.valueOf(getValue()).toString();
	}

	/**
	 * Sets random weight value
	 */
	public void randomize() {
		this.flatWeights[this.weightIndex] = Math.random() - 0.5d;
                this.previousValue = this.getValue();
	}

	/**
	 * Sets random weight value within specified interval
	 */
	public void randomize(double min, double max) {
		this.flatWeights[this.weightIndex] = min + Math.random() * (max - min);
                this.previousValue = this.getValue();
	}
}
