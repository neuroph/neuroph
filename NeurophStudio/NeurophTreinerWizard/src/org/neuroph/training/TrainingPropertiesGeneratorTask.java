package org.neuroph.training;

import java.util.Stack;
import org.neuroph.core.data.DataSet;
import org.neuroph.training.SampleTraining;
import org.neuroph.util.Properties;

/**
 * Generates and sets properties for the whole process note: load all settings
 * from the xml file
 *
 * @author zoran
 */
public class TrainingPropertiesGeneratorTask extends Task {

    // neural network settings
    double[] learningRates = new double[]{0.2, 0.3};
    int[] hiddenNeurons = new int[]{12, 16};
    // settings for training/test sets
    int[] trainingSetPercents = new int[]{60, 70};
    int crossValRepeatCount = 1;
    DataSet dataSet;
    // generated properties
    Stack<Properties> properties;
    String outputVarName = "trainingPropertiesStack";

    // max iterations max error
    // da imamo varijantu da mogu da se navod ekonkretne vrednosti
    // ali da se pretrazuje ceo prostor: min max vrednost i step
    public TrainingPropertiesGeneratorTask(String taskName, String outputVarName) {
        super(taskName);
        properties = new Stack<Properties>();

        this.outputVarName = outputVarName;
    }

    // every data set file should have different settings for neural network and other stuff
    @Override
    public void execute() {
        // generate next properties and set in process var
        parentProcess.logMessage("Generating neural network and training settings");

        // iterate number of hidden neurons and learning rate - do thi snn generator   
        for (int l = 0; l < learningRates.length; l++) {
            for (int h = 0; h < hiddenNeurons.length; h++) {
                for (int r = 0; r < crossValRepeatCount; r++) {
                    for (int tp = 0; tp < trainingSetPercents.length; tp++) {
                        Properties processProperties = new Properties();
                        Properties dataSetProperties = new Properties();
                        if (dataSet == null) {
                            dataSetProperties.setProperty("fileName", SampleTraining.class.getResource("iris.data.txt").getFile());
                            dataSetProperties.setProperty("inputsCount", 4);
                            dataSetProperties.setProperty("outputsCount", 3);
                            dataSetProperties.setProperty("delimiter", ",");
                        } else {
                            dataSetProperties.setProperty("fileName", dataSet.getFilePath());
                            dataSetProperties.setProperty("inputsCount", dataSet.getInputSize());
                            dataSetProperties.setProperty("outputsCount", dataSet.getOutputSize());
                            dataSetProperties.setProperty("delimiter", ",");
                        }

                        processProperties.setProperty("dataSetProperties", dataSetProperties);

                        Properties neuralNetworkProperties = new Properties();
                        neuralNetworkProperties.setProperty("learningRate", learningRates[l]);
                        neuralNetworkProperties.setProperty("hiddenNeurons", hiddenNeurons[h]);
                        neuralNetworkProperties.setProperty("inputNeurons", dataSet.getInputSize());
                        neuralNetworkProperties.setProperty("outputNeurons", dataSet.getOutputSize());
                        processProperties.setProperty("neuralNetworkProperties", neuralNetworkProperties);

                        Properties crossValidationProperties = new Properties();
                        crossValidationProperties.setProperty("trainingSetPercent", trainingSetPercents[tp]);
                        processProperties.setProperty("crossValidationProperties", crossValidationProperties);

                        properties.push(processProperties);
                    }
                }
            }
        }

        parentProcess.setVar(outputVarName, properties);

    }

    public void setLearningRates(double[] learningRates) {
        this.learningRates = learningRates;
    }

    public void setHiddenNeurons(int[] hiddenNeurons) {
        this.hiddenNeurons = hiddenNeurons;
    }

    public void setTrainingSetPercents(int[] trainingSetPercents) {
        this.trainingSetPercents = trainingSetPercents;
    }

    public void setCrossValRepeatCount(int crossValRepeatCount) {
        this.crossValRepeatCount = crossValRepeatCount;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }
    

}
