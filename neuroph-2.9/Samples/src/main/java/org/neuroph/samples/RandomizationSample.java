package org.neuroph.samples;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.random.DistortRandomizer;
import org.neuroph.util.random.NguyenWidrowRandomizer;

/**
 * This sample shows how to use various weight randomization techniques in Neuroph.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class RandomizationSample {

    /**
     * Runs this sample
     */
    public static void main(String[] args) {

        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(2, 3, 1);
        // neuralNet.randomizeWeights(new WeightsRandomizer());
        // neuralNet.randomizeWeights(new RangeRandomizer(0.1, 0.9));
        // neuralNet.randomizeWeights(new GaussianRandomizer(0.4, 0.3));
        neuralNet.randomizeWeights(new NguyenWidrowRandomizer(0.3, 0.7));
        printWeights(neuralNet);

        neuralNet.randomizeWeights(new DistortRandomizer(0.5));
        printWeights(neuralNet);
    }

    public static void printWeights(NeuralNetwork neuralNet) {
        for (Layer layer : neuralNet.getLayers()) {
            for (Neuron neuron : layer.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    System.out.print(connection.getWeight().value + " ");
                }
                System.out.println();
            }
        }
    }
}
