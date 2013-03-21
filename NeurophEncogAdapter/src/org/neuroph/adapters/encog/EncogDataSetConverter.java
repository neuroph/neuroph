package org.neuroph.adapters.encog;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author Zoran Sevarac
 */
public class EncogDataSetConverter {
        public static DataSet covertToNeurophDataset(BasicMLDataSet encogDataset) {
            int numInputs = encogDataset.getInputSize();
            int numOutputs = encogDataset.getIdealSize();
            DataSet neurophDataset = null;
            
            if (numOutputs > 0) {
                neurophDataset = new DataSet( numInputs, numOutputs);
            } else {
                neurophDataset = new DataSet( numInputs);
            }
                        
            for (MLDataPair dataPair :  encogDataset.getData()) {
                if (numOutputs > 0) {
                    DataSetRow row = new DataSetRow(dataPair.getInputArray(), dataPair.getIdealArray());
                    neurophDataset.addRow(row);                    
                } else {
                    DataSetRow row = new DataSetRow(dataPair.getInputArray());
                    neurophDataset.addRow(row);
                }                
            }
                        
            return neurophDataset;
        }
        
    public static BasicMLDataSet covertToJMLDataset(DataSet neurophDataset) {
        BasicMLDataSet encogDataSet = new BasicMLDataSet();
        int numOutputs = neurophDataset.getOutputSize();
              
        for(DataSetRow row : neurophDataset.getRows()) {
            
            if (numOutputs > 0) {            
                encogDataSet.add(new BasicMLData(row.getInput()), new BasicMLData(row.getDesiredOutput()) );
            } else {
                encogDataSet.add(new BasicMLData(row.getInput()));
            }                                             
        }
    
        return encogDataSet;
    }
        
 
}
