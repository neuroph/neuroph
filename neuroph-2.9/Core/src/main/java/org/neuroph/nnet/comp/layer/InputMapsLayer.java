/**
 * Copyright 2013 Neuroph Project http://neuroph.sourceforge.net
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

package org.neuroph.nnet.comp.layer;

import org.neuroph.core.transfer.Linear;
import org.neuroph.nnet.comp.Dimension2D;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.NeuronProperties;

/**
 * Input layer for convolutional networks
 * @author Boris Fulurija
 * @author Zoran Sevarac
 */
public class InputMapsLayer extends FeatureMapsLayer {

    private static final long serialVersionUID = -4982081431101626706L;
    
    /**
     * Default neuron properties for InputMapsLayer is InputNeuron with Linear transfer function 
     */
    public static final  NeuronProperties DEFAULT_NEURON_PROP = new NeuronProperties();

    static {
		DEFAULT_NEURON_PROP.setProperty("neuronType", InputNeuron.class);
		DEFAULT_NEURON_PROP.setProperty("transferFunction", Linear.class);     
    }
    
    
    /**
     * Create InputMapsLayer with specified number of maps with specified dimensions
     * @param mapDimension dimensions of a single feature map
     * @param mapCount  number of feature maps
     */
    public InputMapsLayer( Dimension2D mapDimensions, int mapCount) {
        super(mapDimensions, mapCount, InputMapsLayer.DEFAULT_NEURON_PROP );
    }

    @Override
    public void connectMaps(FeatureMapLayer fromMap, FeatureMapLayer toMap) {
       // does nothing
    }
    
    
}