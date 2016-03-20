/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.training;

import java.awt.Event;

/**
 *
 * @author zoran
 */
public class ProcessEvent extends java.util.EventObject {

   private String message;
   private NeurophWorkflow source;
    
   // we could also add Task related Events  as Task arg as a source
   
    public ProcessEvent(NeurophWorkflow source, String message) {        
        super(source);        
        this.source = source;
        this.message = message;                
    }

    public String getMessage() {
        return message;
    }
    
}
