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

import java.util.concurrent.Callable;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.Dimension2D;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.util.NeuronFactory;
import org.neuroph.util.NeuronProperties;

/**
 * FeatureMapLayer Layer provides 2D layout of the neurons in layer. All the neurons
 * are actually stored in one dimensional array in superclass.
 * This type of layer is used as feature map for convolutional networks
 *
 * @author Boris Fulurija
 * @author Zoran Sevarac
 */
public class FeatureMapLayer extends Layer /*implements Callable<Void>*/ {

    private static final long serialVersionUID = 2498669699995172395L;

    /**
     * Dimensions of this layer (width and height)
     */
    private Dimension2D dimensions;
    
    /**
     * Kernel of this feature map
     */
    private Kernel kernel;
    
    
   
    /**
     * Creates an empty 2D layer with specified dimensions
     *
     * @param dimensions layer dimensions (width and weight)
     */    
    public FeatureMapLayer(Dimension2D dimensions, NeuronProperties neuronProperties) {
        this.dimensions = dimensions;
        
        for (int i = 0; i < dimensions.getHeight() * dimensions.getWidth(); i++) {
            Neuron neuron = NeuronFactory.createNeuron(neuronProperties);
            addNeuron(neuron);
        }        
    }    
    

    /**
     * Creates an empty 2D layer with specified dimensions and kernel
     *
     * @param dimensions layer dimensions (width and weight)
     */
    public FeatureMapLayer(Dimension2D dimensions, Dimension2D kernelDimension) {
        this.dimensions = dimensions;
        this.kernel = new Kernel(kernelDimension);
    }

    /**
     * Creates 2D layer with specified dimensions, filled with neurons with
     * specified properties
     *
     * @param dimensions       layer dimensions
     * @param neuronProperties neuron properties
     */
    public FeatureMapLayer(Dimension2D dimensions, NeuronProperties neuronProperties, Dimension2D kernelDimension) {
        this(dimensions, kernelDimension);

        for (int i = 0; i < dimensions.getHeight() * dimensions.getWidth(); i++) {
            Neuron neuron = NeuronFactory.createNeuron(neuronProperties);
            addNeuron(neuron);
        }
    }

    /**
     * Returns width of this layer
     *
     * @return width of this layer
     */
    public int getWidth() {
        return dimensions.getWidth();
    }

    /**
     * Returns height of this layer
     *
     * @return height of this layer
     */
    public int getHeight() {
        return dimensions.getHeight();
    }


    /**
     * Returns dimensions of this layer
     *
     * @return dimensions of this layer
     */
    public Dimension2D getDimensions() {
        return dimensions;
    }


    /**
     * Returns neuron at specified position in this layer
     *
     * @param x neuron's x position
     * @param y neuron's y position
     * @return neuron at specified position in this layer
     */
    public Neuron getNeuronAt(int x, int y) {
        return getNeuronAt(x + y * (dimensions.getWidth()));
    }

    public Kernel getKernel() {
        return kernel;
    }

 
//    @Override
//    public Void call() throws Exception {
//        calculate();
//        return null;
//    }


  


}
