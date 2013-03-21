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
 * Ramp neuron transfer function.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Ramp extends TransferFunction implements Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The slope parametetar of the ramp function
	 */	
	private double slope = 1d;
	
	/**
	 * Threshold for the high output level
	 */	
	private double xHigh = 1d;
	
	/**
	 * Threshold for the low output level
	 */	
	private double xLow = 0d;
	
	/**
	 * Output value for the high output level
	 */	
	private double yHigh = 1d;
	
	/**
	 * Output value for the low output level
	 */	
	private double yLow = 0d;

	/**
	 * Creates an instance of Ramp transfer function with default settings
	 */	
	public Ramp() {
	}

	/**
	 * Creates an instance of Ramp transfer function with specified settings
	 */	
	public Ramp(double slope, double xLow, double xHigh, double yLow,
			double yHigh) {
		this.slope = slope;
		this.xLow = xLow;
		this.xHigh = xHigh;
		this.yLow = yLow;
		this.yHigh = yHigh;
	}

	/**
	 * Creates an instance of Ramp transfer function with specified properties.
	 */		
	public Ramp(Properties properties) {
		try {
			this.slope = (Double)properties.getProperty("transferFunction.slope");
			this.yHigh = (Double)properties.getProperty("transferFunction.yHigh");
			this.yLow = (Double)properties.getProperty("transferFunction.yLow");
			this.xHigh = (Double)properties.getProperty("transferFunction.xHigh");
			this.xLow = (Double)properties.getProperty("transferFunction.xLow");
		} catch (NullPointerException e) {
			// if properties are not set just leave default values
		} catch (NumberFormatException e) {
			System.err.println("Invalid transfer function properties! Using default values.");
		}
	}

        @Override
	public double getOutput(double net) {
		if (net < this.xLow)
			return this.yLow;
		else if (net > this.xHigh)
			return this.yHigh;
		else
			return (double) (slope * net);
	}

	/**
	 * Returns threshold value for the low output level 
	 * @return threshold value for the low output level 
	 */
	public double getXLow() {
		return this.xLow;
	}

	/**
	 * Sets threshold for the low output level 
	 * @param x threshold value for the low output level
	 */		
	public void setXLow(double x) {
		this.xLow = x;
	}

	/**
	 * Returns threshold value for the high output level 
	 * @return threshold value for the high output level 
	 */		
	public double getXHigh() {
		return this.xHigh;
	}

	/**
	 * Sets threshold for the high output level 
	 * @param x threshold value for the high output level
	 */	
	public void setXHigh(double x) {
		this.xHigh = x;
	}

	/**
	 * Returns output value for low output level 
	 * @return output value for low output level 
	 */		
	public double getYLow() {
		return this.yLow;
	}

	/**
	 * Sets output value for the low output level 
	 * @param y value for the low output level 
	 */		
	public void setYLow(double y) {
		this.yLow = y;
	}

	/**
	 * Returns output value for high output level 
	 * @return output value for high output level 
	 */	
	public double getYHigh() {
		return this.yHigh;
	}

	/**
	 * Sets output value for the high output level 
	 * @param y value for the high output level 
	 */	
	public void setYHigh(double y) {
		this.yHigh = y;
	}

}