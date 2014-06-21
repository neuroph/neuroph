package org.neuroph.training;

import java.util.Stack;
import org.neuroph.util.Properties;

/**
 * Setting process properties for single iteration, by poping one by one from stack
 * @author Zoran Sevarac
 */
public class SetTrainingPropertiesTask extends Task {
    Stack<Properties>  processPropertiesStack;
    
    String inputVarName  = "trainingPropertiesStack", // name of the var that holds DataSetProperties
           outputVarName = "trainingProperties";    

//    public SetTrainingPropertiesTask(String name) {
//        super(name);
//    }

    public SetTrainingPropertiesTask(String name, String inputVarName, String outputVarName) {
        super(name);                
        this.inputVarName = inputVarName;
        this.outputVarName = outputVarName;        
    }    
    
    @Override
    public void execute() {
        // pop from process properties stack
        processPropertiesStack = (Stack<Properties> )getProcessVar(inputVarName);
        Properties processProperties = processPropertiesStack.pop();
        // and set processProperties var for this iteration
        setProcessVar(outputVarName, processProperties);        
    }            
}
