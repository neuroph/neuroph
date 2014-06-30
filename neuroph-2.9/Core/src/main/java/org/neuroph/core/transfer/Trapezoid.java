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
 * Fuzzy trapezoid neuron tranfer function.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Trapezoid extends TransferFunction implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;
	
	// these are the points of trapezoid function
	double leftLow, leftHigh, rightLow, rightHigh;


	/**
	 * Creates an instance of Trapezoid transfer function
	 */	
	public Trapezoid() {
		this.leftLow = 0d;
		this.leftHigh = 1d;
		this.rightLow = 3d;
		this.rightHigh = 2d;
	}

	/**
	 * Creates an instance of Trapezoid transfer function with the specified
	 * setting.
	 */	
	public Trapezoid(double leftLow, double leftHigh, double rightLow, double rightHigh) {
		this.leftLow = leftLow;
		this.leftHigh = leftHigh;
		this.rightLow = rightLow;
		this.rightHigh = rightHigh;
	}

	/**
	 * Creates an instance of Trapezoid transfer function with the specified
	 * properties.
	 */		
	public Trapezoid(Properties properties) {
		try {
			this.leftLow = (Double)properties.getProperty("transferFunction.leftLow");
			this.leftHigh = (Double)properties.getProperty("transferFunction.leftHigh");
			this.rightLow = (Double)properties.getProperty("transferFunction.rightLow");
			this.rightHigh = (Double)properties.getProperty("transferFunction.rightHigh");
		} catch (NullPointerException e) {
			// if properties are not set just leave default values
		} catch (NumberFormatException e) {
			System.err.println("Invalid transfer function properties! Using default values.");
		}
	}

        @Override
	public double getOutput(double net) {
		if ((net >= leftHigh) && (net <= rightHigh)) {
			return 1d;
		} else if ((net > leftLow) && (net < leftHigh)) {
			return (net - leftLow) / (leftHigh - leftLow);
		} else if ((net > rightHigh) && (net < rightLow)) {
			return (rightLow - net) / (rightLow - rightHigh);
		}

		return 0d;
	}

	/**
	 * Sets left low point of trapezoid function
	 * @param leftLow left low point of trapezoid function
	 */
	public void setLeftLow(double leftLow) {
		this.leftLow = leftLow;
	}

	/**
	 * Sets left high point of trapezoid function
	 * @param leftHigh left high point of trapezoid function
	 */	
	public void setLeftHigh(double leftHigh) {
		this.leftHigh = leftHigh;
	}

	/**
	 * Sets right low point of trapezoid function
	 * @param rightLow right low point of trapezoid function
	 */	
	public void setRightLow(double rightLow) {
		this.rightLow = rightLow;
	}

	/**
	 * Sets right high point of trapezoid function
	 * @param rightHigh right high point of trapezoid function
	 */	
	public void setRightHigh(double rightHigh) {
		this.rightHigh = rightHigh;
	}

	/**
	 * Returns left low point of trapezoid function
	 * @return left low point of trapezoid function
	 */
	public double getLeftLow() {
		return leftLow;
	}

	/**
	 * Returns left high point of trapezoid function
	 * @return left high point of trapezoid function
	 */	
	public double getLeftHigh() {
		return leftHigh;
	}

	/**
	 * Returns right low point of trapezoid function
	 * @return right low point of trapezoid function
	 */	
	public double getRightLow() {
		return rightLow;
	}

	/**
	 * Returns right high point of trapezoid function
	 * @return right high point of trapezoid function
	 */		
	public double getRightHigh() {
		return rightHigh;
	}

}
