package org.neuroph.core.events;

/**
 *
 * @author Zoran Sevarac
 */
public enum NeuralNetworkEventType {
    CALCULATED, 
    LAYER_ADDED,
    LAYER_REMOVED,
    NEURON_ADDED,
    NEURON_REMOVED,
    CONNECTION_ADDED,
    CONNECTION_REMOVED;
}