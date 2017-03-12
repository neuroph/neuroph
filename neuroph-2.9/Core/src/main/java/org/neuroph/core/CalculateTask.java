/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.core;

import java.util.concurrent.RecursiveTask;

/**
 *
 * @author Manoj
 */
public class CalculateTask  extends RecursiveTask {
    Neuron neuron;
    public CalculateTask(Neuron neuron){
        this.neuron= neuron;
    }

    @Override
    protected Object compute() {
        neuron.calculate();
        
        return true;
   }
    
}
