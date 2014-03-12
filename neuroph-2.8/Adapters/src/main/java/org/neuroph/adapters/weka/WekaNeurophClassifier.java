package org.neuroph.adapters.weka;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Weka classifier wrapper for Neuroph neural networks
 * Classifier based on Neuroph which can be used inside weka
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class WekaNeurophClassifier extends AbstractClassifier {

    /**
     * NeuralNetwork
     */
    private NeuralNetwork neuralNet;

    /**
     * Creates instance of NeurophWekaClassifier using specified neural network
     * @param neuralNet NeuralNetwork
     */
    public WekaNeurophClassifier(NeuralNetwork neuralNet) {
        this.neuralNet = neuralNet;
    }

    /**
     * Builds classifier using specified data set
     * (trains neural network using that data set)
     * 
     * @param data Instance weka data set
     * @throws Exception
     */
    @Override
    public void buildClassifier(Instances data) throws Exception {
        // convert weka dataset to neuroph dataset
        DataSet dataSet = WekaDataSetConverter.convertWekaToNeurophDataset(data, neuralNet.getInputsCount(), neuralNet.getOutputsCount());
        // train neural network
        neuralNet.learn(dataSet);
    }

    /**
     * Classifies instance as one of possible classes
     * @param instance Instance to classify
     * @return double classes double value
     * @throws Exception
     */
    @Override
    public double classifyInstance(Instance instance) throws Exception {
        double[] item = convertInstanceToDoubleArray(instance);

        // set neural network input
        neuralNet.setInput(item);
        // calculate neural network output
        neuralNet.calculate();

        // find neuron with highest output
        Neuron[] outputNeurons = neuralNet.getOutputNeurons();
        Neuron maxNeuron = null;
        int maxIdx = 0;
        double maxOut = Double.NEGATIVE_INFINITY;
        for (int i=0; i< outputNeurons.length; i++) {
            if (outputNeurons[i].getOutput() > maxOut) {
                maxOut = outputNeurons[i].getOutput();
                maxIdx = i;
            }
        }

        // and return its idx (class)
        return maxIdx;
    }

    /**
     * Calculates predict values for every possible class that
     * instance can be classified as that
     * @param instance Instance to calculate values for
     * @return double[] array of predict values
     * @throws Exception
     */
    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        // Convert instance to double array
        double[] item = convertInstanceToDoubleArray(instance);
        
        // set neural network input
        neuralNet.setInput(item);
        // calculate neural network output
        neuralNet.calculate();
        
        return neuralNet.getOutput();
    }
    
    public NeuralNetwork getNeuralNetwork() {
        return neuralNet;
    }
    

    private double[] convertInstanceToDoubleArray(Instance instance) {
        //initialize double array for values
        double[] item = new double[instance.numAttributes() - 1];

        //fill double array with values from instances
        for (int i = 0; i < instance.numAttributes() - 1; i++) {
            item[i] = instance.value(i);
        }

        return item;
    }
}