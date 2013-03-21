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

/**
 * <pre>
 * Sin neuron transfer function.
 * 
 * output = sin(input)
 * </pre>
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Sin extends TransferFunction {

    @Override
    public double getOutput(double net) {
        return Math.sin(net);
    }
    
    @Override
    public double getDerivative(double net) {
	return Math.cos(net);
    }    
         
}
