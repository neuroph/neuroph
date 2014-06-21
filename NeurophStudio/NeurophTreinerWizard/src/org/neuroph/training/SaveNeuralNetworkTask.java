package org.neuroph.training;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.project.CurrentProject;
import org.neuroph.netbeans.project.NeurophProject;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;

/**
 * Saves neural network wyhin a NeurophStudio project
 * @author zoran
 */
public class SaveNeuralNetworkTask extends Task {
    private NeuralNetwork neuralNetwork;
    int count = 1;
    
     String neuralNetworkVarName;
    
    public SaveNeuralNetworkTask(String name, String neuralNetworkVarName) {
        super(name);
        this.neuralNetworkVarName = neuralNetworkVarName;
    }

    
    
    @Override
    public void execute() {
        logMessage("Training neural network \n");
        this.neuralNetwork = (NeuralNetwork)parentProcess.getVar(neuralNetworkVarName); 
      //  this.neuralNetwork.save(neuralNetworkVarName + count +".nnet");
        
        NeurophProject np = CurrentProject.getInstance().getCurrentProject();
        NeurophProjectFilesFactory fileFactory = NeurophProjectFilesFactory.getDefault();
        neuralNetwork.setLabel(neuralNetworkVarName + count);
        fileFactory.createNeuralNetworkFile(neuralNetwork);
        
        count++;        
    }
    
}
