package org.neuroph.contrib;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.nnet.comp.layer.ConvolutionalLayer;
import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.comp.layer.InputMapsLayer;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.PoolingLayer;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.Dimension2D;
import org.neuroph.nnet.comp.layer.FeatureMapLayer;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.util.NeuronProperties;

public class ConvolutionNeuralNetworkTest {

    ConvolutionalNetwork network;
    FeatureMapsLayer inputLayer;
    Kernel kernel;
    Dimension2D inputDimension;

    @Before
    public void setUp() {
        inputDimension = new Dimension2D(1, 1);

        network = new ConvolutionalNetwork.Builder(inputDimension, 1).build();
        kernel = new Kernel(1, 1);
        inputLayer = new InputMapsLayer(inputDimension, 1);
    }

    @Test
    public void testCreateOneLayerWitihEmptyFeatureMap() {
        FeatureMapLayer featureMap = new FeatureMapLayer(inputDimension, new NeuronProperties());
        inputLayer = new InputMapsLayer(inputDimension, 0);
        inputLayer.addFeatureMap(featureMap);
        network.addLayer(inputLayer);

        // This line of code is meaningless... It should be implicit that if we
        // add layer 0, that input neuron are inside that layer
        network.setInputNeurons(inputLayer.getNeurons());

        Assert.assertEquals(0, network.getInputsCount());
    }

    @Test
    public void testCreateOneLayerWithOneFeatureMapWithManyNeurons() {
        inputDimension = new Dimension2D(4, 4);
        FeatureMapLayer featureMap = new FeatureMapLayer(inputDimension, new NeuronProperties());

      //  CNNTestUtil.fillFeatureMapWithNeurons(featureMap); /// compile error

        inputLayer.addFeatureMap(featureMap);
        network.addLayer(inputLayer);

        // This line of code is meaningless
        network.setInputNeurons(inputLayer.getNeurons());

        Assert.assertEquals(17, network.getInputsCount());
    }

    @Test
    public void testOutputValuesOneLayerOneFeatureMap() {
        inputDimension = new Dimension2D(4, 4);
        inputLayer = new InputMapsLayer(inputDimension, 0);
        FeatureMapLayer featureMap = new FeatureMapLayer(inputDimension, new NeuronProperties());

        double inputNeuronValue = 1;
     //   CNNTestUtil.fillFeatureMapWithNeurons(featureMap, inputNeuronValue);

        inputLayer.addFeatureMap(featureMap);
        network.addLayer(inputLayer);
        network.calculate();

        // This line of code is meaningless
        network.setInputNeurons(inputLayer.getNeurons());

        for (Neuron neuron : network.getInputNeurons()) {
            Assert.assertEquals(inputNeuronValue, neuron.getOutput(), 0.001);
        }
    }

    @Test
    public void testCreateOneLayerWithManyEmptyFeatureMaps() {
        inputLayer = new InputMapsLayer(inputDimension, 0);
        FeatureMapLayer featureMap1 = new FeatureMapLayer(inputDimension, new NeuronProperties());
        FeatureMapLayer featureMap2 = new FeatureMapLayer(inputDimension, new NeuronProperties());
        inputLayer.addFeatureMap(featureMap1);
        inputLayer.addFeatureMap(featureMap2);
        network.addLayer(inputLayer);

        // This line of code is meaningless... It should be implicit that if we
        // add layer 0, that input neuron are inside that layer
        network.setInputNeurons(inputLayer.getNeurons());

        Assert.assertEquals(0, network.getInputsCount());
    }

    @Test
    public void testCreateOneLayerWithManyFeatureMapsWithManyNeurons() {
        inputDimension = new Dimension2D(3, 5);
        inputLayer = new InputMapsLayer(inputDimension, 0);
        FeatureMapLayer featureMap1 = new FeatureMapLayer(inputDimension, new NeuronProperties());
        FeatureMapLayer featureMap2 = new FeatureMapLayer(inputDimension, new NeuronProperties());
        InputNeuron inputNeuron1 = new InputNeuron();
        inputNeuron1.setInput(1);
        InputNeuron inputNeuron2 = new InputNeuron();
        inputNeuron2.setInput(2);
        InputNeuron inputNeuron3 = new InputNeuron();
        inputNeuron3.setInput(3);
        InputNeuron inputNeuron4 = new InputNeuron();
        inputNeuron4.setInput(4);
        featureMap1.addNeuron(inputNeuron1);
        featureMap1.addNeuron(inputNeuron2);
        featureMap1.addNeuron(inputNeuron3);
        featureMap2.addNeuron(inputNeuron4);
        inputLayer.addFeatureMap(featureMap1);
        inputLayer.addFeatureMap(featureMap2);
        network.addLayer(inputLayer);

        // This line of code is meaningless
        network.setInputNeurons(inputLayer.getNeurons());

        Assert.assertEquals(4, network.getInputsCount());
    }

    @Test
    public void testOutputValuesOneLayerManyFeatureMaps() {
        inputDimension = new Dimension2D(3, 5);
        inputLayer = new InputMapsLayer(inputDimension, 0);
        FeatureMapLayer featureMap1 = new FeatureMapLayer(inputDimension, new NeuronProperties());
        FeatureMapLayer featureMap2 = new FeatureMapLayer(inputDimension, new NeuronProperties());
        FeatureMapLayer featureMap3 = new FeatureMapLayer(inputDimension, new NeuronProperties());
        InputNeuron inputNeuron1 = new InputNeuron();
        inputNeuron1.setInput(1);
        InputNeuron inputNeuron2 = new InputNeuron();
        inputNeuron2.setInput(2);
        InputNeuron inputNeuron3 = new InputNeuron();
        inputNeuron3.setInput(3);
        InputNeuron inputNeuron4 = new InputNeuron();
        inputNeuron4.setInput(4);
        featureMap1.addNeuron(inputNeuron1);
        featureMap1.addNeuron(inputNeuron2);
        featureMap2.addNeuron(inputNeuron3);
        featureMap3.addNeuron(inputNeuron4);
        inputLayer.addFeatureMap(featureMap1);
        inputLayer.addFeatureMap(featureMap2);
        inputLayer.addFeatureMap(featureMap3);
        network.addLayer(inputLayer);
        network.calculate();

        // This line of code is meaningless
        network.setInputNeurons(inputLayer.getNeurons());

        Assert.assertEquals(1.0, network.getInputNeurons().get(0).getOutput(), 0.001);
        Assert.assertEquals(2.0, network.getInputNeurons().get(1).getOutput(), 0.001);
        Assert.assertEquals(3.0, network.getInputNeurons().get(2).getOutput(), 0.001);
        Assert.assertEquals(4.0, network.getInputNeurons().get(3).getOutput(), 0.001);
    }

    @Test
    public void testMapLayerCreationWithOneMap() {
        FeatureMapsLayer inputLayer = new InputMapsLayer(new Dimension2D(10, 10), 1);
        FeatureMapsLayer hiddenLayer1 = new ConvolutionalLayer(inputLayer, new Dimension2D(5, 5), 1);
        FeatureMapsLayer hiddenLayer2 = new PoolingLayer(hiddenLayer1, new Dimension2D(2, 2));
        FeatureMapsLayer outputLayer = new ConvolutionalLayer(hiddenLayer2, new Dimension2D(3, 3), 1);

//		ConvolutionUtils.addFeatureMap(inputLayer);
//		ConvolutionUtils.addFeatureMap(hiddenLayer1);
//		ConvolutionUtils.addFeatureMap(hiddenLayer2);
//		ConvolutionUtils.addFeatureMap(outputLayer);

        Assert.assertEquals(100, inputLayer.getNeurons().size());
        Assert.assertEquals(36, hiddenLayer1.getNeurons().size());
        Assert.assertEquals(9, hiddenLayer2.getNeurons().size());
        Assert.assertEquals(1, outputLayer.getNeurons().size());
    }

    @Test
    public void testMapLayerCreationWithManyMaps() {
        FeatureMapsLayer inputLayer = new InputMapsLayer(new Dimension2D(10, 10), 1);
        FeatureMapsLayer hiddenLayer1 = new ConvolutionalLayer(inputLayer, new Dimension2D(5, 5), 1);
        FeatureMapsLayer hiddenLayer2 = new PoolingLayer(hiddenLayer1, new Dimension2D(2, 2));
        FeatureMapsLayer outputLayer = new ConvolutionalLayer(hiddenLayer2, new Dimension2D(3, 3), 1);

//		ConvolutionUtils.addFeatureMaps(inputLayer, 3);
//		ConvolutionUtils.addFeatureMaps(hiddenLayer1, 3);
//		ConvolutionUtils.addFeatureMaps(hiddenLayer2, 3);
//		ConvolutionUtils.addFeatureMaps(outputLayer, 3);

        Assert.assertEquals(100, inputLayer.getNeurons().size());
        Assert.assertEquals(36, hiddenLayer1.getNeurons().size());
        Assert.assertEquals(9, hiddenLayer2.getNeurons().size());
        Assert.assertEquals(1, outputLayer.getNeurons().size());
    }

}
