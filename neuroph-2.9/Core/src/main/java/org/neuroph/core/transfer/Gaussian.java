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
 * <pre>
 * Gaussian neuron transfer function.
 *             -(x^2) / (2 * sigma^2)
 *  f(x) =    e
 * </pre>
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Gaussian extends TransferFunction implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;
	
	/**
	 * The sigma parametetar of the gaussian function
	 */	
	private double sigma = 0.5d;

	/**
	 * Creates an instance of Gaussian neuron transfer
	 */	
	public Gaussian() {
	}

	/**
	 * Creates an instance of Gaussian neuron transfer function with the
	 * specified properties.
	 * @param properties properties of the Gaussian function
	 */	
	public Gaussian(Properties properties) {
		try {
			this.sigma = (Double)properties.getProperty("transferFunction.sigma");
		} catch (NullPointerException e) {
			// if properties are not set just leave default values
		} catch (NumberFormatException e) {
			System.err.println("Invalid transfer function properties! Using default values.");
		}
	}

        @Override
	public double getOutput(double totalInput) {
            output = Math.exp(-Math.pow(totalInput, 2) / (2*Math.pow(sigma, 2)));
              //  output = Math.exp(-0.5d * Math.pow(net, 2));
            return output;
	}
	
	@Override
	public double getDerivative(double net) {
		// TODO: check if this is correct
		double derivative = output * ( -net / (sigma*sigma) );
		return derivative;
	}	

	/**
	 * Returns the sigma parametar of this function
	 * @return  sigma parametar of this function 
	 */	
	public double getSigma() {
		return this.sigma;
	}

	/**
	 * Sets the sigma parametar for this function
	 * @param sigma value for the slope parametar
	 */	
	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

}
