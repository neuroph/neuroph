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
    
    public static int countConnections(Layer layer) {
        int count = 0;
        for (Neuron neuron : layer.getNeurons()) {
            count += neuron.getInputConnections().length;
        }
        return count;
    }    
    
}
