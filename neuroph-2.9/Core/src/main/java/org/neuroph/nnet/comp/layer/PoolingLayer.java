/**
 * Copyright 2013 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.neuroph.nnet.comp.layer;

import org.neuroph.nnet.comp.Kernel;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.input.Max;
import org.neuroph.core.transfer.Ramp;
import org.neuroph.core.transfer.Tanh;
import org.neuroph.nnet.comp.Dimension2D;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuronProperties;

/**
 * Pooling layer is a special type of feature maps layer (FeatureMapsLayer)
 * which is used in convolutional networks. It contains neurons with max input
 * function and method for creating pooling layer specific conectivity patterns.
 * The role of pooling layer is dimensionality and complexity reduction,
 * while it keeps essential information.
 *
 * @author Boris Fulurija
 * @author Zoran Sevarac
 * @see FeatureMapsLayer
 */
public class PoolingLayer extends FeatureMapsLayer {

    private static final long serialVersionUID = -6771501759374920878L;
    
    private Kernel kernel;

    /**
     * Default neuron properties for pooling layer
     */
    public static final NeuronProperties DEFAULT_NEURON_PROP = new NeuronProperties();

    static {
        DEFAULT_NEURON_PROP.setProperty("useBias", true);
        DEFAULT_NEURON_PROP.setProperty("transferFunction", Tanh.class);
    //    DEFAULT_NEURON_PROP.setProperty("transferFunction", Ramp.class);
        DEFAULT_NEURON_PROP.setProperty("inputFunction", Max.class);
    }

    /**
     * Creates pooling layer with specified kernel, appropriate map
     * dimensions in regard to previous layer (fromLayer param) and specified
     * number of feature maps with default neuron settings for pooling layer.
     * Number of maps in pooling layer must be the same as number of maps in previous
     * layer.
     *
     * @param fromLayer previous layer, which will be connected to this layer
     * @param kernel    kernel for all feature maps
     */
    public PoolingLayer(FeatureMapsLayer fromLayer, Dimension2D kernelDim) {
        this.kernel = new Kernel(kernelDim);
        int numberOfMaps = fromLayer.getNumberOfMaps();
        Dimension2D fromDimension = fromLayer.getMapDimensions();

        int mapWidth = fromDimension.getWidth() / kernel.getWidth();
        int mapHeight = fromDimension.getHeight() / kernel.getHeight();
        this.mapDimensions = new Dimension2D(mapWidth, mapHeight);

        createFeatureMaps(numberOfMaps, mapDimensions, kernelDim, DEFAULT_NEURON_PROP);
    }

    /**
     * Creates pooling layer with specified kernel, appropriate map
     * dimensions in regard to previous layer (fromLayer param) and specified
     * number of feature maps with given neuron properties.
     *
     * @param fromLayer    previous layer, which will be connected to this layer
     * @param kernel       kernel for all feature maps
     * @param numberOfMaps number of feature maps to create in this layer
     * @param neuronProp   settings for neurons in feature maps
     */
    public PoolingLayer(FeatureMapsLayer fromLayer, Dimension2D kernelDim, int numberOfMaps, NeuronProperties neuronProp) {
        this.kernel = kernel;
        Dimension2D fromDimension = fromLayer.getMapDimensions();

        int mapWidth = fromDimension.getWidth() / kernel.getWidth();
        int mapHeight = fromDimension.getHeight() / kernel.getHeight();
        this.mapDimensions = new Dimension2D(mapWidth, mapHeight);

        createFeatureMaps(numberOfMaps, mapDimensions, kernelDim, neuronProp);
    }

    /**
     * Creates connections with shared weights between two feature maps
     * Assumes that toMap is from Pooling layer.
     * <p/>
     * In this implementation, there is no overlapping between kernel positions.
     *
     * @param fromMap source feature map
     * @param toMap   destination feature map
     */
    @Override
    public void connectMaps(FeatureMapLayer fromMap, FeatureMapLayer toMap) {
        int kernelWidth = kernel.getWidth();
        int kernelHeight = kernel.getHeight();
        Weight weight = new Weight(1);
        for (int x = 0; x < fromMap.getWidth() - kernelWidth + 1; x += kernelWidth) { // < da li step treba da je kernel
            for (int y = 0; y < fromMap.getHeight() - kernelHeight + 1; y += kernelHeight) {

                Neuron toNeuron = toMap.getNeuronAt(x / kernelWidth, y / kernelHeight);
                for (int dy = 0; dy < kernelHeight; dy++) {
                    for (int dx = 0; dx < kernelWidth; dx++) {
                        int fromX = x + dx;
                        int fromY = y + dy;
                        Neuron fromNeuron = fromMap.getNeuronAt(fromX, fromY);
                        ConnectionFactory.createConnection(fromNeuron, toNeuron, weight);
                    }
                }
            }
        }
    }
}
