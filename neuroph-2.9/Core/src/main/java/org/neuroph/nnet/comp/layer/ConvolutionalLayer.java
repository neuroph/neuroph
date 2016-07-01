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

import org.neuroph.core.transfer.RectifiedLinear;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.core.transfer.Tanh;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.nnet.comp.Dimension2D;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuronProperties;

/**
 * Convolutional layer is a special type of layer, used in convolutional neural
 * networks. It contains a collection of feature maps, default neuron settings
 * for convolutional layers, and method for creating connections to feature
 * maps. The role of the convolutional layer is extaction of high level
 * features.
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
        DEFAULT_NEURON_PROP.setProperty("inputFunction", WeightedSum.class);
        DEFAULT_NEURON_PROP.setProperty("transferFunction", Tanh.class); // <<<--- use Sigmoid, Tanh?        RectifiedLinear
        DEFAULT_NEURON_PROP.setProperty("useBias", true);
    }

    /**
     * Creates convolutional layer with specified kernel, and appropriate map
     * dimensions in regard to previous layer - fromLayer param
     *
     * @param fromLayer previous layer, which will be connected to this layer
     * @param kernel kernel for all feature maps in this layer
     */
//    public ConvolutionalLayer(FeatureMapsLayer fromLayer, Kernel kernel) {
//        Dimension2D fromDimension = fromLayer.getMapDimensions();
//        int mapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
//        int mapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
//        this.mapDimensions = new Dimension2D(mapWidth, mapHeight);
//
//        createFeatureMaps(1, this.mapDimensions, ConvolutionalLayer.DEFAULT_NEURON_PROP);
//    }

    /**
     * Creates convolutional layer with specified kernel, appropriate map
     * dimensions in regard to previous layer (fromLayer param) and specified
     * number of feature maps with default neuron settings for convolutional
     * layer.
     *
     * @param fromLayer previous layer, which will be connected to this layer
     * @param kernel kernel for all feature maps
     * @param numberOfMaps number of feature maps to create in this layer
     */
    public ConvolutionalLayer(FeatureMapsLayer fromLayer, Dimension2D kernelDimension, int numberOfMaps) {
        Dimension2D fromDimension = fromLayer.getMapDimensions();

        int mapWidth = fromDimension.getWidth() - kernelDimension.getWidth() + 1;
        int mapHeight = fromDimension.getHeight() - kernelDimension.getHeight() + 1;
        this.mapDimensions = new Dimension2D(mapWidth, mapHeight);

        createFeatureMaps(numberOfMaps, this.mapDimensions, kernelDimension, ConvolutionalLayer.DEFAULT_NEURON_PROP);
    }
    
     /**
     * Creates convolutional layer with specified kernel, appropriate map
     * dimensions in regard to previous layer (fromLayer param) and specified
     * number of feature maps with default neuron settings for convolutional
     * layer.
     *
     * @param fromLayer previous layer, which will be connected to this layer
     * @param kernel kernel for all feature maps
     * @param numberOfMaps number of feature maps to create in this layer
     * @param transferFunction neuron's transfer function to use
     */
    public ConvolutionalLayer(FeatureMapsLayer fromLayer, Dimension2D kernelDimension, int numberOfMaps, Class <? extends TransferFunction> transferFunction) {
        Dimension2D fromDimension = fromLayer.getMapDimensions();

        int mapWidth = fromDimension.getWidth() - kernelDimension.getWidth() + 1;
        int mapHeight = fromDimension.getHeight() - kernelDimension.getHeight() + 1;
        this.mapDimensions = new Dimension2D(mapWidth, mapHeight);
        
        NeuronProperties neuronProp = new NeuronProperties(Neuron.class, transferFunction);

        createFeatureMaps(numberOfMaps, this.mapDimensions, kernelDimension, neuronProp);
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
    public ConvolutionalLayer(FeatureMapsLayer fromLayer, Dimension2D kernelDimension, int numberOfMaps, NeuronProperties neuronProp) {
        Dimension2D fromDimension = fromLayer.getMapDimensions();

        int mapWidth = fromDimension.getWidth() - kernelDimension.getWidth() + 1;
        int mapHeight = fromDimension.getHeight() - kernelDimension.getHeight() + 1;
        this.mapDimensions = new Dimension2D(mapWidth, mapHeight);

        createFeatureMaps(numberOfMaps, this.mapDimensions, kernelDimension, neuronProp);
    }

    /**
     * Creates connections with shared weights between two feature maps Assumes
     * that toMap is from Convolutional layer.
     * <p/>
     * Kernel is used as a sliding window, and kernel positions overlap. Kernel
     * is shifting right by one position at a time. Neurons at the same kernel
     * position share the same weights
     *
     * @param fromMap source feature map
     * @param toMap destination feature map
     */
    @Override
    public void connectMaps(FeatureMapLayer fromMap, FeatureMapLayer toMap) {

        Kernel kernel = toMap.getKernel();
        kernel.initWeights(-0.15, 0.15); // zasto ove vrednosti ???
      //  int numberOfSharedWeights = kernel.getArea();
//        Weight[][] weights = new Weight[kernel.getHeight()][kernel.getWidth()];
//        //double coefficient = getWeightCoeficient(toMap);
//        // initialize kernel with random weights
//        // ovo prebaciti u kernel
//        for (int i = 0; i < kernel.getHeight(); i++) {
//            for (int j = 0; j < kernel.getWidth(); j++) {
//                Weight weight = new Weight();
//                weight.randomize(-0.15, 0.15); // zasto ove vrednosti?
//                weights[i][j] = weight;
//            }
//        }
//        kernel.setWeights(weights); // na kraju svi kerneli od svih feature mapa imaju iste tezine jer gadjaju istu instancu kernela od nadklase!!!!
//                                    // kernel prebaciti u Layer2D preimenovati ga u FeatureMapLayer i dodati mu kernel...
//                                    // pored kernela dodati mu i BiasNeuron...
        BiasNeuron biasNeuron = new BiasNeuron();
        fromMap.addNeuron(biasNeuron);
                                    
                                    
        // ovo se koristi samo za povezivanje dva konvoluciona sloja !!! 
        // dodati step za from - ne mora da bude samo 1
        // ostaje pitanje kako se primenjuje na ivici - trebalo bi od centra - dodati onaj okvir sa strane!!!!
        for (int y = 0; y < toMap.getHeight(); y++) { // iterate all neurons by height in toMap  -- verovatno bi i ovde trebalo zameniti redosled x i y!!!
            for (int x = 0; x < toMap.getWidth(); x++) { // iterate all neurons by width in toMap
                Neuron toNeuron = toMap.getNeuronAt(x, y); // get neuron at specified position in toMap
                for (int ky = 0; ky < kernel.getHeight(); ky++) { // iterate kernel positions by y
                    for (int kx = 0; kx < kernel.getWidth(); kx++) { // iterate kernel positions by x
                        int fromX = x + kx; // calculate the x position of from neuron
                        int fromY = y + ky; // calculate the y position of from neuron
                        //int currentWeightIndex = kx + ky * kernel.getHeight(); // find the idx of the shared weight
                        Weight[][] concreteKernel = kernel.getWeights();
                        Neuron fromNeuron = fromMap.getNeuronAt(fromX, fromY);
                        ConnectionFactory.createConnection(fromNeuron, toNeuron, concreteKernel[kx][ky]);  // - da li je ovo dobro ???
                        // also create connection from bias
                        ConnectionFactory.createConnection(biasNeuron, toNeuron);
                    }
                }
            }
        }
    }

    // ova metoda se izgleda koristila ranije za odredjivanje koeficijenta tezina u kernelu u zavisnosti od broja konekcija
    // koriscena je u gornjoj metodi connectMaps
    private double getWeightCoeficient(FeatureMapLayer toMap) {
        int numberOfInputConnections = toMap.getNeuronAt(0, 0).getInputConnections().size();
        double coefficient = 1d / Math.sqrt(numberOfInputConnections);
        coefficient = !Double.isInfinite(coefficient) || !Double.isNaN(coefficient) || coefficient == 0 ? 1 : coefficient;
        return coefficient;
    }
}
