package org.neuroph.training;

/**
 * Base class for all tasks
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
abstract public class Task {
    NeurophWorkflow parentProcess;
    String name;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
            
    public NeurophWorkflow getParentProcess() {
        return parentProcess;
    }

    public void setParentProcess(NeurophWorkflow parentProcess) {
        this.parentProcess = parentProcess;
    }  
   
    protected void logMessage(String message) {
        parentProcess.logMessage(message);
    }
    
    public Object getProcessVar(String name) {
        return parentProcess.getVar(name);
    }
    
    public void setProcessVar(String name, Object value) {
        parentProcess.setVar(name, value);
    }    
       
    public abstract void execute();    
    
}