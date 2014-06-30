package org.neuroph.adapters.jml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * Provides methods to convert dataset instances from Neuroph to JML,
 * and from JML to Neuroph
 * @author Zoran Sevarac
 * @author Vladimir Markovic
 */
public class JMLDataSetConverter {

    /**
     * Converts Java-ML data set to Neuroph data set
     * @param jmlDataset Dataset Java-ML data set
     * @param numInputs int Number of inputs
     * @param numOutputs int Number of outputs
     * @return Neuroph data set
     */
    public static DataSet convertJMLToNeurophDataset(Dataset jmlDataset, int numInputs, int numOutputs) {

        if (numInputs <= 0) {
            throw new IllegalArgumentException("Number of inputs  in DataSet cannot be zero or negative!");
        }

        if (numOutputs < 0) {
            throw new IllegalArgumentException("Number of outputs  in DataSet cannot be negative!");
        }

        // get number of attributes + 1 if class counts as attribute
        int rowSize = jmlDataset.noAttributes() + 1;
        if (numOutputs + numInputs < rowSize) {
            throw new IllegalArgumentException("Number of outputs and inputs should be equal to number of attributes from data set!");
        }

        // create dataset
        DataSet neurophDataset;

        if (numOutputs == 0) {
            neurophDataset = new DataSet(rowSize);
        } else {
            neurophDataset = new DataSet(numInputs, numOutputs);
        }


        List<String> outputClasses = new ArrayList<String>();

        for (int i = 0; i < jmlDataset.size(); i++) {
            if (!outputClasses.contains(jmlDataset.get(i).classValue().toString())) {
                outputClasses.add(jmlDataset.get(i).classValue().toString());
            }
        }

        // fill neuroph dataset from jml dataset
        for (int i = 0; i < jmlDataset.size(); i++) {
            Iterator attributeIterator = jmlDataset.get(i).iterator();

            double[] values = new double[rowSize];
            int index = 0;

            while (attributeIterator.hasNext()) {
                Double attrValue = (Double) attributeIterator.next();
                values[index] = attrValue.doubleValue();
                index++;
            }

            DataSetRow row = null;
            if (numOutputs == 0) {
                row = new DataSetRow(values);
            } else {
                double[] inputs = new double[numInputs];
                double[] outputs = new double[outputClasses.size()];
                int k = 0;
                int j = 0;
                for (int v = 0; v < values.length; v++) {
                    if (v < numInputs) {
                        inputs[j] = values[v];
                        j++;
                    }
                }
                
                for(String cla:outputClasses){
                    if(cla.equals(jmlDataset.get(i).classValue().toString())){
                        outputs[k] = 1;
                    }else{
                        outputs[k] = 0;
                    }
                    k++;
                }

                row = new DataSetRow(inputs, outputs);
            }
            row.setLabel(jmlDataset.get(i).classValue().toString());
            neurophDataset.addRow(row);
        }

        return neurophDataset;
    }

    /**
     * Converts Neuroph data set to Java-ML data set
     * @param neurophDataset Dataset Neuroph data set
     * @return Dataset Java-ML data set
     */
    public static Dataset convertNeurophToJMLDataset(DataSet neurophDataset) {
        Dataset jmlDataset = new DefaultDataset();

        int numInputs = neurophDataset.getInputSize();
        int numOutputs = neurophDataset.getOutputSize();

        for (DataSetRow row : neurophDataset.getRows()) {

            if (numOutputs > 0) {
                double[] mergedIO = new double[numInputs + numOutputs];
                for (int i = 0; i < numInputs; i++) {
                    mergedIO[i] = row.getInput()[i];
                }

                for (int i = 0; i < numOutputs; i++) {
                    mergedIO[numInputs + i] = row.getDesiredOutput()[i];
                }

                Instance instance = new DenseInstance(mergedIO);
                instance.setClassValue(row.getLabel());
                jmlDataset.add(instance);
            } else {
                Instance instance = new DenseInstance(row.getInput());
                instance.setClassValue(row.getLabel());
                jmlDataset.add(instance);
            }
        }

        return jmlDataset;
    }
}