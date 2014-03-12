package org.neuroph.adapters.weka;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

/**
 * Example usage of Neuroph Weka adapters
 * @author Zoran Sevarac
 */
public class WekaNeurophSample {

    public static void main(String[] args) throws Exception {

        // create weka dataset from file
        DataSource dataSource = new DataSource("datasets/iris.arff");
        Instances wekaDataset = dataSource.getDataSet();
        wekaDataset.setClassIndex(4);

        // normalize dataset
        Normalize filter = new Normalize();
        filter.setInputFormat(wekaDataset);
        wekaDataset = Filter.useFilter(wekaDataset, filter);    
        
        // convert weka dataset to neuroph dataset
        DataSet neurophDataset = WekaDataSetConverter.convertWekaToNeurophDataset(wekaDataset, 4, 3);

        // convert back neuroph dataset to weka dataset
        Instances testWekaDataset = WekaDataSetConverter.convertNeurophToWekaDataset(neurophDataset);

        // print out all to compare
        System.out.println("Weka data set from file");
        printDataSet(wekaDataset);
        
        System.out.println("Neuroph data set converted from Weka data set");
        printDataSet(neurophDataset);
        
        System.out.println("Weka data set reconverted from Neuroph data set");
        printDataSet(testWekaDataset);

        System.out.println("Testing WekaNeurophClassifier");
        testNeurophWekaClassifier(wekaDataset);
    }

    /**
     * Prints Neuroph data set
     *
     * @param neurophDataset Dataset Neuroph data set
     */
    public static void printDataSet(DataSet neurophDataset) {
        System.out.println("Neuroph dataset");
        Iterator iterator = neurophDataset.iterator();

        while (iterator.hasNext()) {
            DataSetRow row = (DataSetRow) iterator.next();
            System.out.println("inputs");
            System.out.println(Arrays.toString(row.getInput()));
            if (row.getDesiredOutput().length > 0) {
                System.out.println("outputs");
                System.out.println(Arrays.toString(row.getDesiredOutput()));
                System.out.println(row.getLabel());
            }
        }
    }

    /**
     * Prints Weka data set
     *
     * @param wekaDataset Instances Weka data set
     */
    private static void printDataSet(Instances wekaDataset) {
        System.out.println("Weka dataset");
        Enumeration en = wekaDataset.enumerateInstances();
        while (en.hasMoreElements()) {
            Instance instance = (Instance) en.nextElement();
            double[] values = instance.toDoubleArray();
            System.out.println(Arrays.toString(values));
            System.out.println(instance.stringValue(instance.classIndex()));
        }
    }

    /**
     * Test NeurophWekaClassifier
     *
     * @param wekaDataset Instances Weka data set
     */
    private static void testNeurophWekaClassifier(Instances wekaDataset) {
        try {
            MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(4, 16, 3);

            // set labels manualy
            neuralNet.getOutputNeurons()[0].setLabel("Setosa");
            neuralNet.getOutputNeurons()[1].setLabel("Versicolor");
            neuralNet.getOutputNeurons()[2].setLabel("Virginica");

            // initialize NeurophWekaClassifier
            WekaNeurophClassifier neurophWekaClassifier = new WekaNeurophClassifier(neuralNet);
            // set class index on data set
            wekaDataset.setClassIndex(4);
            
            // process data set
            neurophWekaClassifier.buildClassifier(wekaDataset);

            // test item
            //double[] item = {5.1, 3.5, 1.4, 0.2, 0.0}; // normalized item is below
            double[] item = {0.22222222222222213, 0.6249999999999999, 0.06779661016949151, 0.04166666666666667, 0};

            // create weka instance for test item
            Instance instance = new DenseInstance(1, item);

            // test classification
            System.out.println("NeurophWekaClassifier - classifyInstance for {5.1, 3.5, 1.4, 0.2}");
            System.out.println("Class idx: "+neurophWekaClassifier.classifyInstance(instance));
            System.out.println("NeurophWekaClassifier - distributionForInstance for {5.1, 3.5, 1.4, 0.2}");
            double dist[] = neurophWekaClassifier.distributionForInstance(instance);            
            for (int i=0; i<dist.length; i++ ) {
                System.out.println("Class "+i+": "+dist[i]);
            }

        } catch (Exception ex) {
            Logger.getLogger(WekaNeurophSample.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
