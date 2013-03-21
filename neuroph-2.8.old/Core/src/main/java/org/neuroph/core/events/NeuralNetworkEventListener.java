package org.neuroph.core.events;

/**
 *
 * @author zoran
 */
public interface NeuralNetworkEventListener extends  java.util.EventListener {
    
      public void handleNeuralNetworkEvent(NeuralNetworkEvent event);
}
