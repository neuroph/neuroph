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

import org.neuroph.nnet.comp.Kernel;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Convolutional layer is a special type of layer, used in convolutional neural
 * networks. It contains a collection of feature maps, default neuron settings
 * for convolutional layers, and method for creating connections to feature
 * maps. The role of the convolutional layer is extaction of high level features.
 *
 * @author Boris Fulurija
 * @author Zoran Sevarac
 * @see FeatureMapsLayer
 */
public class ConvolutionalLayer extends FeatureMapsLayer {

    private static final long serialVersionUID = -4619196904153707871L;
    
    /**
     * Default neuron properties for convolutional layer
     */
    public static final NeuronProperties DEFAULT_NEURON_PROP = new NeuronProperties();

    static {
        DEFAULT_NEURON_PROP.setProperty("useBias", true);
        DEFAULT_NEURON_PROP.setProperty("transferFunction", TransferFunctionType.SIGMOID);
        DEFAULT_NEURON_PROP.setProperty("inputFunction", WeightedSum.class);
    }

    /**
     * Creates convolutional layer with specified kernel, and appropriate map
     * dimensions in regard to previous layer - fromLayer param
     *
     * @param fromLayer previous layer, which will be connected to this layer
     * @param kernel kernel for all feature maps in this layer
     */
    public ConvolutionalLayer(FeatureMapsLayer fromLayer, Kernel kernel) {
        super(kernel);

        Layer2D.Dimensions fromDimension = fromLayer.getMapDimensions();
        int mapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
        int mapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
        this.mapDimensions = new Layer2D.Dimensions(mapWidth, mapHeight);

        createFeatureMaps(1, this.mapDimensions, ConvolutionalLayer.DEFAULT_NEURON_PROP);    
    }

    /**
     * Creates convolutional layer with specified kernel, appropriate map
     * dimensions in regard to previous layer (fromLayer param) and specified
     * number of feature maps with default neuron settings for convolutional layer.
     *
     * @param fromLayer previous layer, which will be connected to this layer
     * @param kernel kernel for all feature maps
     * @param numberOfMaps number of feature maps to create in this layer
     */
    public ConvolutionalLayer(FeatureMapsLayer fromLayer, Kernel kernel, int numberOfMaps) {
        super(kernel);
        Layer2D.Dimensions fromDimension = fromLayer.getMapDimensions();

        int mapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
        int mapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
        this.mapDimensions = new Layer2D.Dimensions(mapWidth, mapHeight);

        createFeatureMaps(numberOfMaps, this.mapDimensions, ConvolutionalLayer.DEFAULT_NEURON_PROP);
    }

    /**
     * Creates convolutional layer with specified kernel, appropriate map
     * dimensions in regard to previous layer (fromLayer param) and specified
     * number of feature maps with given neuron properties.
     * 
     * @param fromLayer previous layer, which will be connected to this layer
     * @param kernel kernel for all feature maps
     * @param numberOfMaps number of feature maps to create in this layer
     * @param neuronProp settings for neurons in feature maps
     */
    public ConvolutionalLayer(FeatureMapsLayer fromLayer, Kernel kernel, int numberOfMaps, NeuronProperties neuronProp) {
        super(kernel);
        Layer2D.Dimensions fromDimension = fromLayer.getMapDimensions();

        int mapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
        int mapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
        this.mapDimensions = new Layer2D.Dimensions(mapWidth, mapHeight);

        createFeatureMaps(numberOfMaps, this.mapDimensions, neuronProp);
    }

    /**
     * Creates connections with shared weights between two feature maps
     * Assumes that toMap is from Convolutional layer.
     * 
     * Kernel is used as a sliding window, and kernel positions overlap. 
     * Kernel is shifting right by  one position at a time.
     * Neurons at the same kernel position share the same weights
     * 
     * @param fromMap source feature map
     * @param toMap destination feature map
     */
    @Override
    public void connectMaps(Layer2D fromMap, Layer2D toMap) {

        int numberOfSharedWeights = kernel.getArea();
        // create shared weights array
        Weight[] weights = new Weight[numberOfSharedWeights];
        for (int i = 0; i < numberOfSharedWeights; i++) {
            Weight weight = new Weight();
            weight.randomize(-1, 1);
            weights[i] = weight;
        }

        for (int x = 0; x < toMap.getWidth(); x++) { // iterate all neurons by widthe and height in toMap
            for (int y = 0; y < toMap.getHeight(); y++) {
                Neuron toNeuron = toMap.getNeuronAt(x, y);
                for (int dy = 0; dy < kernel.getHeight(); dy++) { // iterate all kernel positions
                    for (int dx = 0; dx < kernel.getWidth(); dx++) {
                        int fromX = x + dx; // calculate the x position of the from neuron
                        int fromY = y + dy; // calculate the y position of the from neuron
                        int currentWeightIndex = dx + dy * kernel.getHeight(); // find the idx of the shared weight
                        Neuron fromNeuron = fromMap.getNeuronAt(fromX, fromY);
                        ConnectionFactory.createConnection(fromNeuron, toNeuron, weights[currentWeightIndex]);
                    }
                }
            }
        }
    }
}
