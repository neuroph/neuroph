package org.neuroph.core.events;

import org.neuroph.core.NeuralNetwork;

/**
 * This event is generated when neural network is calculated
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class NeuralNetworkCalculatedEvent extends NeuralNetworkEvent {

    public NeuralNetworkCalculatedEvent(NeuralNetwork source) {
        super(source);
    }
    
    
    
}
