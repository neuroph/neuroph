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

package org.neuroph.nnet;

import org.neuroph.core.Layer;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.nnet.comp.ConvolutionalUtils;
import org.neuroph.nnet.comp.layer.*;
import org.neuroph.nnet.learning.ConvolutionalBackpropagation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.nnet.comp.Dimension2D;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Convolutional neural network with backpropagation algorithm modified for
 * convolutional networks.
 * <p/>
 * TODO: provide Hiton, LeCun, AndrewNg implementation specific features
 *
 * @author Boris Fulurija
 * @author Zoran Sevarac
 * @see ConvolutionalBackpropagation
 */
public class ConvolutionalNetwork extends NeuralNetwork<ConvolutionalBackpropagation> {

    private static final long serialVersionUID = -1393907449047650509L;


    public ConvolutionalNetwork() {

    }

    /**
     * Sets network input, to all feature maps in input layer
     *
     * @param inputVector
     * @throws VectorSizeMismatchException
     */
    @Override
    public void setInput(double... inputVector) throws VectorSizeMismatchException {
        FeatureMapsLayer inputLayer = (FeatureMapsLayer) getLayerAt(0);
        int currentNeuron = 0;
        for (int i = 0; i < inputLayer.getNumberOfMaps(); i++) {
            FeatureMapLayer map = inputLayer.getFeatureMap(i);
            for (Neuron neuron : map.getNeurons()) {
                if (!(neuron instanceof BiasNeuron))
                    neuron.setInput(inputVector[currentNeuron++]);
            }
        }
    }

    public static class Builder {

        public static final NeuronProperties DEFAULT_FULL_CONNECTED_NEURON_PROPERTIES = new NeuronProperties();
        private ConvolutionalNetwork network;

        static {
            DEFAULT_FULL_CONNECTED_NEURON_PROPERTIES.setProperty("useBias", true);
            DEFAULT_FULL_CONNECTED_NEURON_PROPERTIES.setProperty("transferFunction", TransferFunctionType.SIGMOID);
            DEFAULT_FULL_CONNECTED_NEURON_PROPERTIES.setProperty("inputFunction", WeightedSum.class);
        }

        public Builder() {
            network = new ConvolutionalNetwork();
        }
        
        public Builder withInputLayer(int width, int height, int numberOfMaps) {
            if (network.getLayersCount() > 0) throw new NeurophException("Input layer must be the first layer in network");
            
            InputMapsLayer inputLayer = new InputMapsLayer(new Dimension2D(width, height), numberOfMaps); 
            inputLayer.setLabel("Input Layer");
            network.addLayer(inputLayer); 

            return this;
        }        

        public Builder withConvolutionLayer(int kernelWidth, int kernelHeight, int numberOfMaps) {
            FeatureMapsLayer prevLayer = getLastFeatureMapLayer();
            ConvolutionalLayer convolutionLayer = new ConvolutionalLayer(prevLayer, new Dimension2D(kernelWidth, kernelHeight), numberOfMaps);

            network.addLayer(convolutionLayer);
            ConvolutionalUtils.fullConnectMapLayers(prevLayer, convolutionLayer);

            return this;
        }
        
        public Builder withConvolutionLayer(final Dimension2D kernelDimension, int numberOfMaps, Class<? extends TransferFunction> transferFunction) {
            FeatureMapsLayer prevLayer = getLastFeatureMapLayer();
            ConvolutionalLayer convolutionLayer = new ConvolutionalLayer(prevLayer, kernelDimension, numberOfMaps, transferFunction);

            network.addLayer(convolutionLayer);
            ConvolutionalUtils.fullConnectMapLayers(prevLayer, convolutionLayer);

            return this;
        }        

        public Builder withPoolingLayer(int width, int height) {
            FeatureMapsLayer lastLayer = getLastFeatureMapLayer();
            PoolingLayer poolingLayer = new PoolingLayer(lastLayer, new Dimension2D(width, height));

            network.addLayer(poolingLayer);
            ConvolutionalUtils.fullConnectMapLayers(lastLayer, poolingLayer);

            return this;
        }

        public Builder withFullConnectedLayer(int numberOfNeurons) {
            Layer lastLayer = getLastLayer();

            Layer fullConnectedLayer = new Layer(numberOfNeurons, DEFAULT_FULL_CONNECTED_NEURON_PROPERTIES);
            network.addLayer(fullConnectedLayer);

            ConnectionFactory.fullConnect(lastLayer, fullConnectedLayer);

            return this;
        }
        
        public Builder withFullConnectedLayer(Layer layer) {
            Layer lastLayer = getLastLayer();
            network.addLayer(layer);
            ConnectionFactory.fullConnect(lastLayer, layer);
            return this;
        }        

        public ConvolutionalNetwork build() {
            network.setInputNeurons(network.getLayerAt(0).getNeurons());
            network.setOutputNeurons(getLastLayer().getNeurons());
            network.setLearningRule(new ConvolutionalBackpropagation());
            return network;
        }


        private FeatureMapsLayer getLastFeatureMapLayer() {
            Layer layer = getLastLayer();
            if (layer instanceof FeatureMapsLayer)
                return (FeatureMapsLayer) layer;

            throw new RuntimeException("Unable to add next layer because previous layer is not FeatureMapLayer");
        }

        private Layer getLastLayer() {
            return network.getLayerAt(network.getLayersCount() - 1);
        }


    }


}