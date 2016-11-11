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
 * Tanh neuron transfer function.
 *
 * output = ( e^(2*input)-1) / ( e^(2*input)+1 )
 * </pre>
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Tanh extends TransferFunction implements Serializable {

    /**
     * The class fingerprint that is set to indicate serialization
     * compatibility with a previous version of the class.
     */
    private static final long serialVersionUID = 2L;

    /**
     * The slope parameter of the Tanh function
     */
    private double slope = 2d;
    
    /**
     * The amplitude parameter
     */
    private double amplitude = 1.7159d;

    private double derivativeOutput;

    /**
     * Creates an instance of Tanh neuron transfer function with default
     * slope=1.
     */
    public Tanh() {
    }

    /**
     * Creates an instance of Tanh neuron transfer function with specified
     * value for slope parameter.
     *
     * @param slope the slope parameter for the Tanh function
     */
    public Tanh(double slope) {
        this.slope = slope;
    }

    /**
     * Creates an instance of Tanh neuron transfer function with the
     * specified properties.
     *
     * @param properties properties of the Tanh function
     */
    public Tanh(Properties properties) {
        try {
            this.slope = (Double) properties.getProperty("transferFunction.slope");
        } catch (NullPointerException e) {
            // if properties are not set just leave default values
        } catch (NumberFormatException e) {
            System.err.println("Invalid transfer function properties! Using default values.");
        }
    }

    @Override
    final public double getOutput(double input) {
        // conditional logic helps to avoid NaN
        if (input > 100) {
            return 1.0d;
        } else if (input < -100) {
            return -1.0d;
        }

        //a*tanh(s*x) = a*[(e^(2*s*x) - 1) / (e^(2*s*x) - 1)]
        double E_x = Math.exp(2 * this.slope * input);
        this.output = amplitude * ((E_x - 1d) / (E_x + 1d));

        return this.output;
    }


    // The derivative of a*tanh(s*x) is a*s*sech^2(s*x), or a*s*(1-tanh^2(s*x))

    /**
     *
     * @return
     */
    @Override
    final public double getDerivative(double input) {
        //output here is a*tanh^2(s*x)
        double E_x = Math.exp(2 * this.slope * input);
        double tanhsx = (E_x - 1d) / (E_x + 1d);
        derivativeOutput = amplitude * slope * (1 - tanhsx * tanhsx);
        return derivativeOutput;
    }

    /**
     * Returns the slope parameter of this function
     *
     * @return slope parameter of this function
     */
    public double getSlope() {
        return this.slope;
    }

    /**
     * Sets the slope parameter for this function
     *
     * @param slope value for the slope parameter
     */
    public void setSlope(double slope) {
        this.slope = slope;
    }

    /**
     * Returns the amplitude parameter of this function
     *
     * @return amplitude parameter of this function
     */
    public double getAmplitude() {
        return amplitude;
    }

    /**
     * Sets the slope parameter for this function
     *
     * @param amplitude value for the amplitude parameter
     */
    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }
    
    
    
    
}
