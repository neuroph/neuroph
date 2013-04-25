/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.widgets;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;

/**
 *
 * @author zoran
 */
public class NeuralNetworkUtils {
    
    
    public static boolean hasInputConnections(Layer layer) {

        for(Neuron neuron : layer.getNeurons()) {
            if (neuron.hasInputConnections())
                return true;
        }
        
        return false;
    }
    
}
