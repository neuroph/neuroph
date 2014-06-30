package org.neuroph.adapters.jml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author zoran
 */
public class JMLNeurophClassifier implements Classifier {

    /**
     * NeuralNetwork
     */
    NeuralNetwork neuralNet;

    /**
     * Creates instance of NeurophJMLClassifier 
     * @param neuralNet NeuralNetwork
     * @param dataSet DataSet
     */
    public JMLNeurophClassifier(NeuralNetwork neuralNet) {
        this.neuralNet = neuralNet;
    }

    /**
     * Neural network learns from Java-ML data set
     * @param dataSetJML Dataset Java-ML data set
     */
    @Override
    public void buildClassifier(Dataset dataSetJML) {
        DataSet dataSet = JMLDataSetConverter.convertJMLToNeurophDataset(dataSetJML, neuralNet.getInputsCount(), neuralNet.getOutputsCount());
        neuralNet.learn(dataSet);
    }

    /**
     * Classifies instance as one of possible classes
     * @param instnc Instance to classify
     * @return Object class as Object
     */
    @Override
    public Object classify(Instance instnc) {
        
        double[] item = convertInstanceToDoubleArray(instnc);

        // set neural network input
        neuralNet.setInput(item);
        // calculate neural network output
        neuralNet.calculate();

        // find neuron with highest output
        Neuron maxNeuron = null;
        double maxOut = Double.NEGATIVE_INFINITY;
        for (Neuron neuron : neuralNet.getOutputNeurons()) {
            if (neuron.getOutput() > maxOut) {
                maxNeuron = neuron;
                maxOut = neuron.getOutput();
            }
        }

        // and return its label
        return maxNeuron.getLabel();
    }

    /**
     * Calculates predict values for every possible class that
     * instance can be classified as that
     * @param instnc Instance
     * @return Map<Object, Double>
     */
    @Override
    public Map<Object, Double> classDistribution(Instance instnc) {
        
        // Convert instance to double array
        double[] item = convertInstanceToDoubleArray(instnc);
        
        // set neural network input
        neuralNet.setInput(item);
        // calculate neural network output
        neuralNet.calculate();

        // find neuron with highest output
        Map<Object, Double> possibilities = new HashMap<Object, Double>();

        for (Neuron neuron : neuralNet.getOutputNeurons()) {
            possibilities.put(neuron.getLabel(), neuron.getOutput());
        }
        
        return possibilities;
    }

    /**
     * Convert instance attribute values to double array values
     * @param instnc Instance to convert
     * @return double[]
     */
    private double[] convertInstanceToDoubleArray(Instance instnc) {
        Iterator attributeIterator = instnc.iterator();

        double[] item = new double[instnc.noAttributes()];
        int index = 0;

        while (attributeIterator.hasNext()) {
            Double attrValue = (Double) attributeIterator.next();
            item[index] = attrValue.doubleValue();
            index++;
        }
        
        return item;
    }
}
