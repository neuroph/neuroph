package org.neuroph.contrib;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.nnet.comp.ConvolutionalUtils;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.comp.layer.InputMapsLayer;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.PoolingLayer;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.nnet.comp.Dimension2D;
import org.neuroph.nnet.comp.layer.FeatureMapLayer;

public class PoolingLayerTest {

    private FeatureMapsLayer inputLayer;
    private Dimension2D inputDimension;
    private Dimension2D kernel;

    @Before
    public void setUp() {
        kernel = new Dimension2D(1, 1);
        inputDimension = new Dimension2D(1, 1);
    }

    @Test
    public void testConnectTwoLayersWithOneFeatureMapOne2OneNeuronWithKernel1() {
        // given
        inputLayer = new InputMapsLayer(inputDimension, 1);
        FeatureMapsLayer hiddenLayer = new PoolingLayer(inputLayer, kernel);
        FeatureMapLayer featureMap = new FeatureMapLayer(hiddenLayer.getMapDimensions(), PoolingLayer.DEFAULT_NEURON_PROP);
        hiddenLayer.addFeatureMap(featureMap);

        // when
        ConvolutionalUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

        // then
        Neuron fromNeuron = inputLayer.getNeuronAt(0, 0, 0);
        Neuron toNeuron = hiddenLayer.getNeuronAt(0, 0, 0);
        Assert.assertEquals(fromNeuron.getOutConnections().get(0), toNeuron.getInputConnections().get(0));
    }

    @Test
    public void testConnectTwoLayersWithOneFeatureMapFour2OneNeuronWithKernel3() {
        // given
        kernel = new Dimension2D(2, 2);
        inputDimension = new Dimension2D(2, 2);
        inputLayer = new InputMapsLayer(inputDimension, 1);
        FeatureMapsLayer hiddenLayer = new PoolingLayer(inputLayer, kernel);
        FeatureMapLayer featureMap = new FeatureMapLayer(hiddenLayer.getMapDimensions(), PoolingLayer.DEFAULT_NEURON_PROP);
        hiddenLayer.addFeatureMap(featureMap);

        // when
        ConvolutionalUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

        //then
        Neuron pooledNeuron = hiddenLayer.getNeurons().get(0);
        Set<Weight> weights = new HashSet<Weight>();
        for (Connection connection : pooledNeuron.getInputConnections())
            weights.add(connection.getWeight());

        for (int i = 0; i < inputLayer.getNeurons().size(); i++) {
            Neuron fromNeuron = inputLayer.getNeurons().get(i);
            Assert.assertEquals(fromNeuron, pooledNeuron.getInputConnections().get(i).getFromNeuron());
        }
        Assert.assertEquals(1, weights.size());
    }

    @Test
    public void testPoolingTwoLayersWithOneFeatureMap() {
        //given
        inputDimension = new Dimension2D(4, 4);
        int kernelWidth = 2;
        int kernelHeight = 2;
        kernel = new Dimension2D(kernelWidth, kernelHeight);

        inputLayer = new InputMapsLayer(inputDimension, 1);
        FeatureMapsLayer hiddenLayer = new PoolingLayer(inputLayer, kernel);
        FeatureMapLayer featureMap = new FeatureMapLayer(hiddenLayer.getMapDimensions(), PoolingLayer.DEFAULT_NEURON_PROP);
        hiddenLayer.addFeatureMap(featureMap);

        // when
        ConvolutionalUtils.connectFeatureMaps(inputLayer, hiddenLayer, 0, 0);

        //then
        FeatureMapLayer fromMap = inputLayer.getFeatureMap(0);
        FeatureMapLayer toMap = hiddenLayer.getFeatureMap(0);
        for (int x = 0; x < hiddenLayer.getMapDimensions().getWidth(); x++) {
            for (int y = 0; y < hiddenLayer.getMapDimensions().getHeight(); y++) {
                Neuron toNeuron = toMap.getNeuronAt(x, y);
                for (int i = 0; i < toNeuron.getInputConnections().size(); i++) {
                    int tempX = i % kernelWidth;
                    int tempY = i / kernelHeight;
                    Neuron fromNeuron = fromMap.getNeuronAt(tempX + (kernelWidth * x), tempY + (kernelHeight * y));
                    Assert.assertEquals(toNeuron.getInputConnections().get(i).getFromNeuron(), fromNeuron);
                }
            }
        }

        Set<Weight> weights = new HashSet<Weight>();
        for (Neuron neuron : hiddenLayer.getNeurons()) {
            for (Connection connection : neuron.getInputConnections()) {
                weights.add(connection.getWeight());
            }
        }

        int desiredTotalConnections = 4;
        Assert.assertEquals(desiredTotalConnections, weights.size());
        Assert.assertEquals(kernelHeight * kernelWidth, weights.size());
    }

}
