/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.training;

/**
 *
 * @author zoran
 */
public class LoopTask extends Task {
    int counter = 0;
    int iterationsLimit = 0;
    String fromTask;
    
    
    public LoopTask(String fromTask, int iterationsLimit) {
        super("Loop");
        this.iterationsLimit = iterationsLimit;
        this.fromTask = fromTask;
    }

    
    
    @Override
    public void execute() {
        if (counter < iterationsLimit) {
            int jumpToIdx = parentProcess.getTaskIdxByName(fromTask);
            counter++;
            parentProcess.jumpToTask(jumpToIdx);            
        }
    }
    
}
