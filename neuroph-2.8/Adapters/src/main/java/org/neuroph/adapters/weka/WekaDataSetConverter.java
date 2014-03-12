package org.neuroph.adapters.weka;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Provides methods to convert dataset instances from Neuroph to Weka 
 * and from Weka to Neuroph.
 * 
 * @author Zoran Sevarac
 */
public class WekaDataSetConverter {

    /**
     * Converts Weka data set to Neuroph data set
     * @param wekaDataset Instances Weka data set
     * @param numInputs int Number of inputs
     * @param numOutputs int Number of outputs
     * @return Neuroph data set
     */
    public static DataSet convertWekaToNeurophDataset(Instances wekaDataset, int numInputs, int numOutputs) {
        
        if (numInputs <= 0) {
            throw new IllegalArgumentException("Number of inputs  in DataSet cannot be zero or negative!");
        }

        if (numOutputs < 0) {
            throw new IllegalArgumentException("Number of outputs  in DataSet cannot be negative!");
        }
        
        if (numOutputs + numInputs < wekaDataset.numAttributes()) {
            throw new IllegalArgumentException("Number of outputs and inputs should be equal to number of attributes from data set!");
        }
        
        // create supervised or unsupervised data set that will be returned
        DataSet neurophDataset=null;
        
        if(numOutputs > 0){
            neurophDataset = new DataSet(numInputs,numOutputs);
        }else{
            neurophDataset = new DataSet(numInputs);
        }
        
        List<Double> classValues = new ArrayList<Double>();
        
        // get all different class values (as ints) from weka dataset
        for(Instance inst: wekaDataset){
            Double classDouble = inst.classValue();
            if(!classValues.contains(classDouble)){
                classValues.add(classDouble);
            }
        }
        
        Enumeration en = wekaDataset.enumerateInstances();
        while(en.hasMoreElements()) { // iterate all instances from dataset
            Instance instance = (Instance) en.nextElement();            
            double[] values = instance.toDoubleArray(); // get all the values from current instance
            if(numOutputs == 0){ // add unsupervised row
                neurophDataset.addRow(values);
            } else {  // add supervised row
                double[] inputs = new double[numInputs];
                double[] outputs = new double[numOutputs];
                
                // set inputs 
                for(int k = 0; k < values.length; k++){
                    if(k < numInputs){
                        inputs[k] = values[k];
                    }
                }
                
                // set binary values for class outputs
                int k = 0;
                for(Double entry : classValues){                    
                    if(entry.doubleValue() == instance.classValue()){ // if the 
                        outputs[k] = 1;
                    }else{
                        outputs[k] = 0;
                    }
                    k++;
                }
                
                DataSetRow row = new DataSetRow(inputs, outputs);
                row.setLabel(instance.stringValue(instance.classIndex()));
                neurophDataset.addRow(row);
            }
        }
        
        return neurophDataset;
    }
    
    
    /**
     * Converts Neuroph data set to Weka data set
     * @param neurophDataset DataSet Neuroph data set
     * @return instances Weka data set
     */
    public static Instances convertNeurophToWekaDataset(DataSet neurophDataset) {
        
        Map<double[], String> classValues = getClassValues(neurophDataset);
        
        Instances instances = createEmptyWekaDataSet(neurophDataset.getInputSize(), neurophDataset.size(), classValues);
        
        int numInputs = neurophDataset.getInputSize();
//        int numOutputs = neurophDataset.getOutputSize();
        int numOutputs = 1; // why is this, and the above line is commented? probably because weka 
        
        instances.setClassIndex(numInputs);
        
        Iterator<DataSetRow> iterator = neurophDataset.iterator();
        while(iterator.hasNext()) { // iterate all dataset rows
            DataSetRow row = iterator.next();
            
            if (numOutputs> 0) { // if it is supervised (has outputs)
                Instance instance = new DenseInstance(numInputs + numOutputs);
                for(int i=0; i< numInputs; i++) {
                    instance.setValue(i, row.getInput()[i] );
                }
                             
                instance.setDataset(instances);     
                
                // set output attribute, as String and double value of class
                for(Map.Entry<double[], String> entry : classValues.entrySet()){
                    if(entry.getValue().equals(row.getLabel())){
                        instance.setValue(numInputs, entry.getValue());
                        double[] rowDouble = row.getDesiredOutput();
                        for(int i = 0; i < rowDouble.length; i++){
                            if(rowDouble[i] == 1){
                                instance.setValue(numInputs, i);
                            }
                            break;
                        }
                        break;
                    }
                }

                           
                instances.add(instance);
            } else { // if it is unsupervised - has only inputs
                // create new instance
                Instance instance = new DenseInstance(numInputs);
                // set all input values
                for(int i=0; i< numInputs; i++) {
                    instance.setValue(i, row.getInput()[i] );
                }
                // and add instance to weka dataset
                instance.setDataset(instances);                        
                instances.add(instance);
            }
        }
        
        return instances;
    }
    
    /**
     * Creates and returns empty weka data set
     * @param numOfAttr int Number of attributes without class attribute
     * @param capacity int Capacity of sample
     * @return empty weka data set
     */
    private static Instances createEmptyWekaDataSet(int numOfAttr, int capacity, Map<double[], String> classValues) {
        //Vector for class attribute possible values
        FastVector fvClassVal = new FastVector();
        //Map double value for every possible class value
        HashMap classVals = new HashMap<String, Double>();
        //Map class label with double key value
        HashMap classValsDoubleAsKey = new HashMap<Double, String>();
        //ind represents double value for class attribute
        int ind = 0;
        
        //loop through possible class values
        for (Map.Entry<double[], String> values : classValues.entrySet()) {

            //add value to vector
            fvClassVal.addElement(values.getValue());

            //map double value for class value
            classVals.put(values.getValue(), new Double(ind));
            //map class label for double key value
            classValsDoubleAsKey.put(new Double(ind),values.getValue());

            ind++;
        }
        //Class attribute with possible values
        Attribute classAttribute = new Attribute("theClass", fvClassVal, classValues.size());
        //Creating attribute vector for Instances class instance
        FastVector fvWekaAttributes = new FastVector(numOfAttr + 1);
        //Fill vector with simple attributes
        for (int i = 0; i < numOfAttr; i++) {
            fvWekaAttributes.addElement(new Attribute(i + "", i));
        }
        //Add class attribute to vector
        fvWekaAttributes.addElement(classAttribute);

        //newDataSet as Instances class instance
        Instances newDataSet = new Instances("newDataSet", fvWekaAttributes, capacity);
        return newDataSet;
    }

    /**
     * Returns all posisible class values
     * @param neurophDataset Neuroph data set
     * @return Map with all possible class values <classValue, className>
     */
    private static Map<double[], String> getClassValues(DataSet neurophDataset) {
        Map<double[], String> classValues = new HashMap<double[], String>();
        
        for(DataSetRow row : neurophDataset.getRows()){
            if(!classValues.containsValue(row.getLabel())){
                classValues.put(row.getDesiredOutput(), row.getLabel());
            }
        }
        
        return classValues;
    }
               
}