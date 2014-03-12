package org.neuroph.adapters.jml;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.filter.normalize.NormalizeMidrange;
import net.sf.javaml.tools.data.FileHandler;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;

/**
 * Example how to use adapters for Java ML http://java-ml.sourceforge.net/
 * 
 * @author Zoran Sevarac
 */
public class JMLNeurophSample {

    public static void main(String[] args) {
        try {
            //create jml dataset
            Dataset jmlDataset = FileHandler.loadDataset(new File("datasets/iris.data"), 4, ",");

            // normalize dataset
            NormalizeMidrange nmr=new NormalizeMidrange(0,1);
            nmr.build(jmlDataset);         
            nmr.filter(jmlDataset);
            
            //print data as read from file
            System.out.println(jmlDataset);

            //convert jml dataset to neuroph
            DataSet neurophDataset = JMLDataSetConverter.convertJMLToNeurophDataset(jmlDataset, 4, 3);
            
            //convert neuroph dataset to jml
            Dataset jml = JMLDataSetConverter.convertNeurophToJMLDataset(neurophDataset);

            //print out both to compare them
            System.out.println("Java-ML data set read from file");
            printDataset(jmlDataset);
            System.out.println("Neuroph data set converted from Java-ML data set");
            printDataset(neurophDataset);
            System.out.println("Java-ML data set reconverted from Neuroph data set");
            printDataset(jml);

            System.out.println("JMLNeuroph classifier test");
            //test NeurophJMLClassifier
            testJMLNeurophClassifier(jmlDataset);

        } catch (Exception ex) {
            Logger.getLogger(JMLNeurophSample.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Prints Java-ML data set
     *
     * @param jmlDataset Dataset Java-ML data set
     */
    public static void printDataset(Dataset jmlDataset) {
        System.out.println("JML dataset");
        Iterator iterator = jmlDataset.iterator();

        while (iterator.hasNext()) {
            Instance instance = (Instance) iterator.next();
            System.out.println("inputs");
            System.out.println(instance.values());
            System.out.println(instance.classValue());
        }
    }

    /**
     * Prints Neuroph data set
     *
     * @param neurophDataset Dataset Neuroph data set
     */
    public static void printDataset(DataSet neurophDataset) {
        System.out.println("Neuroph dataset");
        Iterator iterator = neurophDataset.iterator();

        while (iterator.hasNext()) {
            DataSetRow row = (DataSetRow) iterator.next();
            System.out.println("inputs");
            System.out.println(Arrays.toString(row.getInput()));
            if (row.getDesiredOutput().length > 0) {
                System.out.println("outputs");
                System.out.println(Arrays.toString(row.getDesiredOutput()));
            }
        }
    }


    /**
     * Converts Java-ML data set to Map
     *
     * @param jmlDataset Dataset Java-ML data set
     * @return Map converted from Java-ML data set
     */
    private static Map<double[], String> convertJMLDatasetToMap(Dataset jmlDataset) {

        //number of attributes without class attribute
        int numOfAttributes = jmlDataset.noAttributes();

        //initialize map
        Map<double[], String> itemClassMap = new HashMap<double[], String>();

        //iterate through jml dataset
        for (Instance dataRow : jmlDataset) {

            //initialize double array for values from dataset
            double[] values = new double[numOfAttributes];
            int ind = 0;

            //iterate through values in dataset instance an adding them in double array
            for (Double val : dataRow) {
                values[ind] = val;
                ind++;
            }

            //put attribute values and class value in map
            itemClassMap.put(values, dataRow.classValue().toString());
        }
        return itemClassMap;
    }

    /**
     * Test JMLNeurophClassifier
     *
     * @param jmlDataset Dataset Java-ML data set
     */
    private static void testJMLNeurophClassifier(Dataset jmlDataset) {
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(4, 16, 3);
        
        // set labels for output neurons
        neuralNet.getOutputNeurons()[0].setLabel("Setosa");
        neuralNet.getOutputNeurons()[1].setLabel("Versicolor");
        neuralNet.getOutputNeurons()[2].setLabel("Virginica");

        // initialize NeurophJMLClassifier
        JMLNeurophClassifier jmlnClassifier = new JMLNeurophClassifier(neuralNet);

        // Process Java-ML data set
        jmlnClassifier.buildClassifier(jmlDataset);

        // test item
        //double[] item = {5.1, 3.5, 1.4, 0.2}; // normalized item is below
        double[] item = {-0.27777777777777773, 0.1249999999999999, -0.4322033898305085, -0.45833333333333337};

        // Java-ML instance out of test item
        Instance instance = new DenseInstance(item);

        // why are these not normalised?
        System.out.println("NeurophJMLClassifier - classify of {0.22222222222222213, 0.6249999999999999, 0.06779661016949151, 0.04166666666666667}");
        System.out.println(jmlnClassifier.classify(instance));
        System.out.println("NeurophJMLClassifier - classDistribution of {0.22222222222222213, 0.6249999999999999, 0.06779661016949151, 0.04166666666666667}");
        System.out.println(jmlnClassifier.classDistribution(instance));
    }
}
