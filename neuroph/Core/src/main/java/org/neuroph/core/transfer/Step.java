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

package org.neuroph.core.transfer;

import java.io.Serializable;
import org.neuroph.util.Properties;

/**
 * Step neuron transfer function.
 * y = yHigh, x > 0
 * y = yLow, x <= 0
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Step extends TransferFunction implements Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Output value for high output level
	 */
	private double yHigh = 1d;
	
	/**
	 * Output value for low output level
	 */
	private double yLow = 0d;

	/**
	 * Creates an instance of Step transfer function
	 */
	public Step() {
	}

	/**
	 * Creates an instance of Step transfer function with specified properties
	 */	
	public Step(Properties properties) {
		try {
			this.yHigh = (Double)properties.getProperty("transferFunction.yHigh");
			this.yLow = (Double)properties.getProperty("transferFunction.yLow");
		} catch (NullPointerException e) {
			// if properties are not set just leave default values
		} catch (NumberFormatException e) {
			System.err.println("Invalid transfer function properties! Using default values.");
		}
	}

        @Override
	public double getOutput(double net) {
		if (net > 0d)
			return yHigh;
		else
			return yLow;
	}

	/**
	 * Returns output value for high output level 
	 * @return output value for high output level 
	 */
	public double getYHigh() {
		return this.yHigh;
	}
	
	/**
	 * Set output value for the high output level 
	 * @param yHigh value for the high output level 
	 */
	public void setYHigh(double yHigh) {
		this.yHigh = yHigh;
	}

	/**
	 * Returns output value for low output level 
	 * @return output value for low output level 
	 */	
	public double getYLow() {
		return this.yLow;
	}

	/**
	 * Set output value for the low output level 
	 * @param yLow value for the low output level
	 */	
	public void setYLow(double yLow) {
		this.yLow = yLow;
	}

	/**
	 * Returns the properties of this function
	 * @return properties of this function
	 */
	public Properties getProperties() {
		Properties properties = new Properties();
		properties.setProperty("transferFunction.yHigh", String.valueOf(yHigh));
		properties.setProperty("transferFunction.yLow", String.valueOf(yLow));
		return properties;
	}

}
