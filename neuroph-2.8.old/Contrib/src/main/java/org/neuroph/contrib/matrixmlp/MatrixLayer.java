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

package org.neuroph.contrib.matrixmlp;

/**
 * Base interface for all matrix based layers.
 */
public interface MatrixLayer {

    /**
     * Sets layer input
     * @param inputs
     */
    public void setInputs(double[] inputs);

    /**
     * Returns layer input     *
     * @return layer input vector
     */
    public double[] getInputs();

    /**
     * Sets layer outputs
     * @param inputs
     */
    public void setOutputs(double[] inputs);

    /**
     * Returns layer outputs
     * @return layer outputs
     */
    public double[] getOutputs();

    /**
     * Calculate layer
     */
    public void calculate();

}
