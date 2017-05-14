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

package org.neuroph.core.learning.error;

/**
 * Interface for calculating total network error during the learning.
 * Custom error types  can be implemented.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public interface ErrorFunction {

    /**
     * Return total network error
     * 
     * @return total network error
     */
    public double getTotalError();

    /**
     * Calculates and adds pattern error for the given predicted and target output vector and adds it to total error.
     * @param predictedOutput actual network output
     * @param targetOutput target/desired output
     * @return returns pattern error vector
     */
    public double[] addPatternError(double[] predictedOutput, double[] targetOutput);
    
    /**
     * Sets total error and pattern count  to zero.
     */
    public void reset();    

}
