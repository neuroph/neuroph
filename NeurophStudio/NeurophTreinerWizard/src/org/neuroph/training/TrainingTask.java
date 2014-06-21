package org.neuroph.training;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;

/**
 * Traines neural network with training set. requires neuralNetwork and trainingSet vars in process.
 * input: neural network and training set
 * output: trained network
 * @author zoran
 */
public class TrainingTask extends Task implements LearningEventListener {
    NeuralNetwork neuralNetwork;
    DataSet trainingSet;
    
    String neuralNetworkVarName, trainingSetVarName;
       
    public TrainingTask(String name, String neuralNetworkVarName, String trainingSetVarName) {
        super(name);
        this.neuralNetworkVarName = neuralNetworkVarName;
        this.trainingSetVarName = trainingSetVarName;
        
    }
        
    public void execute() {
        logMessage("Training neural network \n");
        
        // get neural network and training set from process
        this.neuralNetwork = (NeuralNetwork)parentProcess.getVar(neuralNetworkVarName); 
        this.trainingSet =  (DataSet)parentProcess.getVar(trainingSetVarName); // use trainingSet here
                               
        // add learning listener and log learning events (iterations, error) - add as logger
        neuralNetwork.getLearningRule().addListener(this);
        neuralNetwork.learn(trainingSet);       
               
        // get end error and number of iterations
        double totalError = ((BackPropagation)neuralNetwork.getLearningRule()).getTotalNetworkError();
        int iterations = ((BackPropagation)neuralNetwork.getLearningRule()).getCurrentIteration();
       
        // generate training infor that will be used for generating statistics in report        
        
        // log training results
        logMessage("\n<Training summary>------------------------------------------");       
        logMessage("Total error: "+totalError + " reached in "+iterations+" iterations");       
    }

    /**
     * Log details during the training - error for each iteration
     * @param event 
     */
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        parentProcess.logMessage(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
    }

   
}