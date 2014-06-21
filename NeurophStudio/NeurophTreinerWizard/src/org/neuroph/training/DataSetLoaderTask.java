package org.neuroph.training;

import org.neuroph.core.data.DataSet;
import org.neuroph.util.Properties;

/**
 * Loads data set from file and sets process var 'dataSet'
 *
 * @author Zoran Sevarac
 */
public class DataSetLoaderTask extends Task {

    DataSet dataSet;
    String fileName, delimiter;
    int inputsCount, outputsCount;

    String inputVarName = "dataSetProperties", // name of the var that holds DataSetProperties
            outputVarName = "dataSet";

//    public DataSetLoaderTask(String taskName) {
//        super(taskName);
//    }    
    public DataSetLoaderTask(String taskName, String inputVarName, String outputVarName) {
        super(taskName);

        this.inputVarName = inputVarName;
        this.outputVarName = outputVarName;
    }

    public void execute() {

        Properties trainingProperties = (Properties) getProcessVar("trainingProperties");
        Properties dataSetProps = (Properties) trainingProperties.get(inputVarName);

        String oldFilename = this.fileName;
        this.fileName = (String) dataSetProps.get("fileName");
        this.inputsCount = (Integer) dataSetProps.get("inputsCount");
        this.outputsCount = (Integer) dataSetProps.get("outputsCount");
        this.delimiter = (String) dataSetProps.get("delimiter");

        if (fileName.endsWith(".tset")) {
            if (!fileName.equals(oldFilename)) {
                logMessage("Loading data set " + fileName.substring(0, fileName.indexOf(".")));
                dataSet = DataSet.load(fileName);
            } else {
                logMessage("Using data set " + fileName);
            }

            setProcessVar(outputVarName, dataSet);
        }

    }
}
