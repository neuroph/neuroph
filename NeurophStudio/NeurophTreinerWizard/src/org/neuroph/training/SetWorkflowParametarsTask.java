package org.neuroph.training;

import java.util.Stack;
import org.neuroph.util.Properties;

/**
 * Setting process properties for single iteration, by poping one by one from stack
 * @author Zoran Sevarac
 */
public class SetWorkflowParametarsTask extends Task {
    Stack<Variable>  processParametarStack;
    
    String inputVarName  = "parmetarsStack", // name of the var that holds DataSetProperties
           outputVarName = "workflowParametars";    

//    public SetTrainingPropertiesTask(String name) {
//        super(name);
//    }

    public SetWorkflowParametarsTask(String name, String inputVarName, String outputVarName) {
        super(name);                
        this.inputVarName = inputVarName;
        this.outputVarName = outputVarName;        
    }    
    
    @Override
    public void execute() {
        // pop from process properties stack
        processParametarStack = (Stack<Variable> )getVariable(inputVarName);
        Variable processProperties = processParametarStack.pop();
        // and set processProperties var for this iteration
        setVariable(outputVarName, processProperties);        
    }            
}
