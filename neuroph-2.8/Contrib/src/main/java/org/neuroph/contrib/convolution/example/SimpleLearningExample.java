package org.neuroph.contrib.convolution.example;

import org.neuroph.contrib.convolution.CNNFactory;
import org.neuroph.contrib.convolution.CNNFactory.Layer2DType;
import org.neuroph.contrib.convolution.ConvolutionNeuralNetwork;
import org.neuroph.contrib.convolution.FeatureMapLayer;
import org.neuroph.contrib.convolution.Kernel;
import org.neuroph.contrib.convolution.MapDimension;
import org.neuroph.contrib.convolution.util.ModelMetric;
import org.neuroph.contrib.convolution.util.WeightVisualiser;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.NeuralNetworkFactory;

public class SimpleLearningExample {

	public static void testLearningOneLayer() {
		ConvolutionNeuralNetwork cnn = new ConvolutionNeuralNetwork();
		cnn.setLearningRule(new BackPropagation());

		MapDimension mapSize = new MapDimension(5, 5);
		Kernel convolutionKernel = new Kernel(5, 5);

		FeatureMapLayer inputLayer = CNNFactory.creteInputLayer(mapSize);
		FeatureMapLayer convolutionLayer = CNNFactory.createNextLayer(inputLayer, convolutionKernel, Layer2DType.CONVOLUTION);

		CNNFactory.addFeatureMap(inputLayer);
		CNNFactory.addFeatureMap(convolutionLayer);
		CNNFactory.addFeatureMap(convolutionLayer);
		cnn.addLayer(inputLayer);
		cnn.addLayer(convolutionLayer);

		NeuralNetworkFactory.setDefaultIO(cnn);

		CNNFactory.connect(inputLayer, convolutionLayer, 0, 0);
		CNNFactory.connect(inputLayer, convolutionLayer, 0, 1);

		DataSet dataSet = new DataSet(25, 2);
		dataSet.addRow(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, new double[] { 1, 0 });
		dataSet.addRow(new double[] { 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 }, new double[] { 0, 1 });

		cnn.getLearningRule().setMaxError(0.00001);
		cnn.learn(dataSet);

		WeightVisualiser visualiser1 = new WeightVisualiser(convolutionLayer.getFeatureMap(0), convolutionKernel);
		visualiser1.displayWeights();

		WeightVisualiser visualiser2 = new WeightVisualiser(convolutionLayer.getFeatureMap(1), convolutionKernel);
		visualiser2.displayWeights();

		DataSet testSet = new DataSet(25, 2);
		testSet.addRow(new double[] { 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, new double[] { 1, 0 });
		testSet.addRow(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 }, new double[] { 1, 0 });
		testSet.addRow(new double[] { 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0 }, new double[] { 0, 1 });

		ModelMetric.calculateModelMetric(cnn, testSet);

	}

	public static void main(String[] args) {
		testLearningOneLayer();
	}
}
