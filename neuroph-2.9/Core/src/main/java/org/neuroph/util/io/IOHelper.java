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

package org.neuroph.util.io;

import org.neuroph.core.NeuralNetwork;

/**
 * <pre>
 * This class is helper for feeding neural network with data using some InputAdapter
 * and writing network output using OutputAdapter
 * </pre>
 * @see InputAdapter
 * @see OutputAdapter
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class IOHelper {
    
    /**
     * Feeds specified neural network with data from InputAdapter and writes
     * output using OutputAdapter
     * @param neuralNet neural network
     * @param in input data source
     * @param out output data target  
     */
    public static void process(NeuralNetwork neuralNet, InputAdapter in, OutputAdapter out) {
       
        double[] input;
        while( (input = in.readInput()) != null) {
            neuralNet.setInput(input);
            neuralNet.calculate();  
            double[] output = neuralNet.getOutput();
            out.writeOutput(output);
        }
        
        in.close();
        out.close();         
    }
    
}
