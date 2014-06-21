package org.neuroph.training;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * TODO: MSE, min error, max error SD, other stats,number of TP, TN, FP, FN, precision, recall
 * @author zoran
 */
public class TestTask extends Task {
    NeuralNetwork neuralNetwork;
    DataSet testSet;    // we should be able to have 1..M test sets
    
    String neuralNetworkVarName, testSetVarName;    

    public TestTask(String name, String neuralNetworkVarName, String testSetVarName) {
        super(name);
        
        this.neuralNetworkVarName = neuralNetworkVarName;
        this.testSetVarName = testSetVarName;        
    }
    
    public void execute() {
        // calculate mse, itd.
        
        this.neuralNetwork = (NeuralNetwork)parentProcess.getVar(neuralNetworkVarName); 
        this.testSet =  (DataSet)parentProcess.getVar(testSetVarName);        
        
        
        for(DataSetRow row : testSet.getRows()) {
            neuralNetwork.setInput(row.getInput());
            neuralNetwork.calculate();
            double[]output = neuralNetwork.getOutput();
            // now calculate error difference and calculate MSE and other error measures do something with it
            // reuse stuff from goai clasifier and noprop evaluator
        }
    }

        
}
