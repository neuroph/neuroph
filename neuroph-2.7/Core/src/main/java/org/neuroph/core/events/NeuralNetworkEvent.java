package org.neuroph.core.events;

import org.neuroph.core.NeuralNetwork;

/**
 *
 * @author zoran
 */
public class NeuralNetworkEvent extends java.util.EventObject {
    
    public NeuralNetworkEvent(NeuralNetwork source) {
        super(source);
    }
        
}