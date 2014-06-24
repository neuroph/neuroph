package org.neuroph.training;

import org.neuroph.core.data.DataSet;
import org.neuroph.util.Properties;
import org.neuroph.util.data.sample.Sampling;
import org.neuroph.util.data.sample.SubSampling;

/**
 * Executes sampling task for the data set in process.
 * @author Zoran Sevarac
 */
public class SamplingTask extends Task {
    DataSet dataSet;
    Sampling sampling;
    DataSet[] dataSetSample;
          
    public SamplingTask(String name) {
        super(name);
    }

//    public SamplingTask(Sampling sampling) {
//        super("SamplingTask");
//        this.sampling = sampling;
//    }    
    
     // how to choose sampling method
    public void execute() {        
        Properties trainingProperties = (Properties)getProcessVar("trainingProperties");      
        Properties crossValProps = (Properties)trainingProperties.get("crossValidationProperties");          
                
        dataSet = (DataSet)getProcessVar("dataSet");
        int trainingSetPercent =  (Integer)crossValProps.get("trainingSetPercent") ;        
            
        logMessage("Sampling data set to create training and test sets: "+trainingSetPercent+" % / "+(100-trainingSetPercent) +"%");

        this.sampling = new SubSampling(trainingSetPercent);        
        dataSetSample = dataSet.sample(sampling);
        
        parentProcess.setVar("trainingSet", dataSetSample[0]);
        parentProcess.setVar("testSet", dataSetSample[1]);
    }

    
}