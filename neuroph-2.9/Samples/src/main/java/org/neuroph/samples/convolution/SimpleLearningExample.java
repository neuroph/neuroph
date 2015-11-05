package org.neuroph.samples.convolution;

import org.neuroph.nnet.ConvolutionalNetwork;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.samples.convolution.util.WeightVisualiser;


/**
 * This is simple sample to show that learning procedure works
 */
public class SimpleLearningExample {

    public static void testLearningOneLayer() {
        Layer2D.Dimensions inputDimension = new Layer2D.Dimensions(5, 5);

        Kernel convolutionKernel = new Kernel(3, 3);

        ConvolutionalNetwork convolutionNet = new ConvolutionalNetwork.Builder(inputDimension, 1)
                .withConvolutionLayer(convolutionKernel, 2)
                .withFullConnectedLayer(2)
                .createNetwork();
        
        convolutionNet.setLearningRule(new MomentumBackpropagation());


        // CREATE DATA SET

        DataSet dataSet = new DataSet(25, 2);
        dataSet.addRow(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new double[]{1, 0});
        dataSet.addRow(new double[]{0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
                new double[]{0, 1});

        // TRAIN NETWORK

        convolutionNet.getLearningRule().setMaxError(0.00001);
        convolutionNet.learn(dataSet);
                
        System.out.println("Done training!");
        
        Layer2D featureMap1 = ((FeatureMapsLayer)convolutionNet.getLayerAt(1)).getFeatureMap(0);
        Layer2D featureMap2 = ((FeatureMapsLayer)convolutionNet.getLayerAt(1)).getFeatureMap(1);
        
        WeightVisualiser visualiser1 = new WeightVisualiser(featureMap1, convolutionKernel);
        visualiser1.displayWeights();

        WeightVisualiser visualiser2 = new WeightVisualiser(featureMap2, convolutionKernel);
        visualiser2.displayWeights();

        
        // CREATE TEST SET

        DataSet testSet = new DataSet(25, 2);
        testSet.addRow(new double[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                new double[]{1, 0});
        testSet.addRow(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                new double[]{1, 0});
        testSet.addRow(new double[]{0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0},
                new double[]{0, 1});

    }

    public static void main(String[] args) {
        testLearningOneLayer();
        System.out.println("done!");
    }
}
