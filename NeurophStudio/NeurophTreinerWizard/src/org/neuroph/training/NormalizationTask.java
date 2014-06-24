package org.neuroph.training;

import org.neuroph.core.data.DataSet;
import org.neuroph.util.data.norm.Normalizer;

/**
 * 
 * @author zoran
 */
public class NormalizationTask extends Task {
    DataSet dataSet;
    Normalizer normalizer;
    
    String dataSetVarName = "dataSet";

//    public NormalizationTask(Normalizer normalizer) {
//        super("NormalizationTask");
//        this.normalizer = normalizer;
//    }    
    
    public NormalizationTask(String name, String dataSetVarName, Normalizer normalizer) {
        super(name);
        this.dataSetVarName = dataSetVarName;
        this.normalizer = normalizer;
    }

    public void execute() {
        logMessage("Normalizing data set using " + getName());
                 
        dataSet = (DataSet) getProcessVar(dataSetVarName); // get dataSet
        normalizer.normalize(dataSet); // izgleda da baca exception ako je ts prazan...
    }
}