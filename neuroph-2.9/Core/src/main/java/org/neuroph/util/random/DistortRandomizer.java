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

package org.neuroph.util.random;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;

/**
 * This class provides distort randomization technique, which distorts existing 
 * weight values using specified distortion factor.
 * Weights are distorted using following formula:
 * newWeightValue = currentWeightValue + (distortionFactor - (random * distortionFactor * 2))
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class DistortRandomizer extends WeightsRandomizer {
    
    /**
     * Distrotion factor which determines the amount to distort existing weight values
     */
    double distortionFactor;

    /**
     * Create a new instance of DistortRandomizer with specified distortion factor
     * @param distortionFactor amount to distort existing weights
     */
    public DistortRandomizer(double distortionFactor) {
        this.distortionFactor = distortionFactor;
    }
    
//    /**
//     * Iterate all layers, neurons and connection weight and apply distort randomization 
//     * @param neuralNetwork 
//     */
//    @Override
//    public void randomize(NeuralNetwork neuralNetwork) {
//        for (Layer layer : neuralNetwork.getLayers()) {
//            for (Neuron neuron : layer.getNeurons()) {
//                for (Connection connection : neuron.getInputConnections()) {
//                    double weight = connection.getWeight().getValue();
//                    connection.getWeight().setValue(distort(weight));
//                }
//            }
//        }
//
//    }

    /**
     * Iterate all layers, neurons and connection weight and apply distort randomization
     * @param neuron
     */
    @Override
    public void randomize(Neuron neuron) {
            for (Connection connection : neuron.getInputConnections()) {
                    double weight = connection.getWeight().getValue();
                    connection.getWeight().setValue(distort(weight));
            }
    }       
    
    
    /**
     * Returns distorted weight value
     * @param weight current weight value
     * @return distorted weight value
     */
    private double distort(double weight) {        
        return  weight + (this.distortionFactor - (randomGenerator.nextDouble() * this.distortionFactor * 2)); 
    }
       
}
