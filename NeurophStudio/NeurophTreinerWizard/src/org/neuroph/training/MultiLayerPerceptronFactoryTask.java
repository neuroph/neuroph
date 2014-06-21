package org.neuroph.training;

import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.Properties;

/**
 * TODO: add maxError, maxIterations, momentum , learningRuleClass
 * @author zoran
 */
public class MultiLayerPerceptronFactoryTask extends Task {

    String inputVarName  = "neuralNetworkProperties", // name of the var that holds DataSetProperties
           outputVarName = "neuralNetwork";    
    
//    public MultiLayerPerceptronFactoryTask(String name) {
//        super(name);
//    }

    public MultiLayerPerceptronFactoryTask(String taskName, String inputVarName, String outputVarName) {
        super(taskName);
        
        this.inputVarName = inputVarName;
        this.outputVarName = outputVarName;
    }    
    
    @Override
    public void execute() {
        // get neural network settings from the process and use MultiLayerPerceptronFactoryTask
        logMessage("Creating neural network");
        
        Properties processProperties = (Properties)getProcessVar("trainingProperties");      
        Properties  neuralNetworkProperties = (Properties)processProperties.get(inputVarName);        
        
        int inputs = (Integer)neuralNetworkProperties.get("inputNeurons");
        int outputs = (Integer)neuralNetworkProperties.get("outputNeurons");
        int hidden = (Integer)neuralNetworkProperties.get("hiddenNeurons");
        double learningRate = (Double)neuralNetworkProperties.get("learningRate");        
                
        MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(inputs, hidden, outputs);
        neuralNetwork.getLearningRule().setLearningRate(learningRate);  
//        neuralNetwork.setLabel(name);
        

        setProcessVar(outputVarName, neuralNetwork);
        
        logMessage("Using "+inputs +" input, " + hidden + " hidden and "+outputs+" output neurons; learning rate: "+learningRate );

    }
      
     
}
