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
 * output = amplitude * tanh(slope * input) = amplitude * ( e^(2*slope*input)-1) / ( e^(2*slope*input)+1 )
 * </pre>
 *
 * @author Zoran Sevarac <sevarac@gmail.com>, Nyefan
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
    private double slope = 1d;
    
    /**
     * The amplitude parameter
     */
    private double amplitude = 1d;

    /**
     * The output of the getDerivative() function;
     */
    private double derivativeOutput;

    /**
     * Creates an instance of Tanh neuron transfer function with default
     * slope=amplitude=1
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
            //this.amplitude = (Double) properties.getProperty("transferFunction.amplitude");
        } catch (NullPointerException e) {
            // if properties are not set just leave default values
        } catch (NumberFormatException e) {
            System.err.println("Invalid transfer function properties! Using default values.");
        }
    }

    /**
     * Returns the value of this function at x=input
     *
     * @param input location to evaluate this function
     * @return value of this function at x=input
     */
    @Override
    final public double getOutput(double input) {
        // conditional logic helps to avoid NaN
        if (Math.abs(input) * slope > 100) { return Math.signum(input) * 1.0d; }

        //a*tanh(s*x) = a*[(e^(2*s*x) - 1) / (e^(2*s*x) - 1)]
        double E_x = Math.exp(2.0d * slope * input);
        output = amplitude * ((E_x - 1.0d) / (E_x + 1.0d));

        return output;
    }


    // The derivative of a*tanh(s*x) is a*s*sech^2(s*x), or a*s*(1-tanh^2(s*x))

    /**
     * Returns the derivative of this function evaluated at x=input
     *
     * @param input location to evaluate the derivative
     * @return derivative of this function evaluated at x=input
     */
    @Override
    final public double getDerivative(double input) {
        // conditional logic helps to avoid NaN
        if (Math.abs(input) * slope > 100) { return 0.0d; }

        //output here is a*tanh^2(s*x)
        double E_x = Math.exp(2 * slope * input);
        double tanhsx = (E_x - 1d) / (E_x + 1d);
        derivativeOutput = amplitude * slope * (1.0d - tanhsx * tanhsx);
        return derivativeOutput;
    }

    /**
     * Returns the slope parameter of this function
     *
     * @return slope parameter of this function
     */
    public double getSlope() {
        return slope;
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
