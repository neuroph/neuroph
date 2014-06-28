package org.neuroph.netbeans.explorer;

import java.util.ArrayList;
import java.util.Arrays;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Creates children nodes for NeuralNetwork in Navigator window
 * Used by NeuralNetworkNode
 * @author Ivana Jovicic
 * @author Zoran Sevarac
 * @see http://platform.netbeans.org/tutorials/nbm-selection-2.html
 *
 */
public class ExplorerNeuralNetworkChildren extends Children.Keys<Object> {
    private NeuralNetwork neuralNet;

    public ExplorerNeuralNetworkChildren(NeuralNetwork nnet) {
        this.neuralNet = nnet;
    }

    /**
     * Creates single child node for specified object obj.
     * For each element in array or collection passed to setKeys() in addNotify, createNodes() will be called once
     * @param obj object to create Node for
     * @return nodes for specified object.
     */
    @Override
    protected Node[] createNodes(Object object) {
        if(object instanceof Layer){                
            Layer layer = (Layer) object;           
            LayerNode layerNode = new LayerNode(layer); 
            layerNode.setName("Layer " + String.valueOf(layer.getParentNetwork().indexOf(layer)+1)); 
            return new Node[] { layerNode }; 
        }else { 
            LearningRule learningRule = (LearningRule) object;
            LearningRuleNode learningRuleNode = new LearningRuleNode(learningRule);
            learningRuleNode.setName("Learning rule");
            return new Node[] {learningRuleNode}; 
        }

        //TODO: list all neuron nodes here using LayerChildren, and if there are too many neurons put text too many neurons to inspect
//        Node neuronNode = new NeuronNode((layer.getNeuronAt(0)));
//        neuronNode.setName(layer.getNeurons().size() + " neurons");
//        layerNode.getChildren().add(new Node[]{neuronNode});
    }

    /**
     *  Get all objects you want to create nodes for, and call setKeys()
     *  For each element in the array or collection you pass to setKeys(), createNodes() will be called once
     */
    @Override
    protected void addNotify() {
        super.addNotify();
        ArrayList<Object> keys = new ArrayList<Object>();
        if(neuralNet.getLearningRule()!=null){
            keys.add(neuralNet.getLearningRule());
        }
        keys.addAll(Arrays.asList(neuralNet.getLayers()));
        setKeys(keys);   
    }
}
