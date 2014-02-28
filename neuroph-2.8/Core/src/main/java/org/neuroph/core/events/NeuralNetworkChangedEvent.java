/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.core.events;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;

/**
 *
 * @author zoran
 */
public class NeuralNetworkChangedEvent extends java.util.EventObject {

    NNEventType eventType;
    
    public NeuralNetworkChangedEvent(NeuralNetwork source) {
        super(source);
    }
    
    // when layer is added or removed
    public NeuralNetworkChangedEvent(Layer source, NNEventType eventType ) {
        super(source);
    }    
    
    // when neuron is aded or removed
    public NeuralNetworkChangedEvent(Neuron source, NNEventType eventType ) {
        super(source);
    }        
    
    public static enum NNEventType {
       // layeradded, layer removed, neuron added neuron removed 
    }
    
}
