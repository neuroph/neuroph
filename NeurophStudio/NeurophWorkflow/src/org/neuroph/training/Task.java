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
    
    public <T>T getVariable(String name, Class<T> type) {
        return   (T) ((Variable<T>)parentProcess.getVariable(name).getValue());
    }
    // todo izbaciti
    public Object getVariable(String name) {
        return parentProcess.getVariable(name);
    }
    
    public void setVariable(String name, Object value) {
        parentProcess.setVariable(new Variable(name, value) );
    }    
    

    public abstract void execute();    
    
}