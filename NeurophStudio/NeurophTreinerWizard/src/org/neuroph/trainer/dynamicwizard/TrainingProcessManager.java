/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.trainer.dynamicwizard;

import org.neuroph.training.ProcessEvent;
import org.neuroph.training.ProcessEventListener;
import org.openide.windows.IOProvider;

/**
 *
 * @author zoran
 */
public class TrainingProcessManager implements ProcessEventListener {
    private static TrainingProcessManager instance;
    
    private TrainingProcessManager() {
        
    }
    
    public static TrainingProcessManager getDefault() {
       if (instance==null) {
           instance = new TrainingProcessManager();
       } 
       
       return instance;
       
    }

    @Override
    public void handleProcessEvent(ProcessEvent event) {
       String message = event.getMessage();
       
       IOProvider.getDefault().getIO("Training Process", false).getOut().println(message);
    }
    
    
}
