package org.neuroph.netbeans.explorer;

import java.util.Arrays;
import java.util.List;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 * Creates children nodes for NeuralNetwork in Navigator window Used by
 * NeuralNetworkNode
 *
 * @author Ivana Jovicic
 * @author Zoran Sevarac
 * @see http://platform.netbeans.org/tutorials/nbm-selection-2.html
 *
 */
public class ExplorerNeuralNetworkChildFactory extends ChildFactory<Object> {

    private final NeuralNetwork<?> neuralNet;

    public ExplorerNeuralNetworkChildFactory(NeuralNetwork<?> nnet) {
        this.neuralNet = nnet;
        // TODO add property change listener for updating
    }

    /**
     * Get all objects you want to create nodes for, and add them to toPopulate
     * For each element in the array or collection you add to toPopulate,
     * createNodeForKey(Object key) will be called once
     * @param toPopulate
     * @return 
     */
    @Override
    protected boolean createKeys(List<Object> toPopulate) {
        if (neuralNet.getLearningRule() != null) {
            toPopulate.add(neuralNet.getLearningRule());
        }
        toPopulate.addAll(Arrays.asList(neuralNet.getLayers()));
        return true;
    }

    /**
     * Creates single child node for specified object obj. For each element in
     * array or collection passed to setKeys() in addNotify, createNodes() will
     * be called once
     *
     * @param key object to create Node for
     * @return node for specified object.
     */
    @Override
    protected Node createNodeForKey(Object key) {
        if (key instanceof Layer) {
            Layer layer = (Layer) key;
            LayerNode layerNode = new LayerNode(layer);
            layerNode.setName("Layer " + String.valueOf(layer.getParentNetwork().indexOf(layer) + 1)); // TODO name updating
            return layerNode;
        } else {
            LearningRule learningRule = (LearningRule) key;
            LearningRuleNode learningRuleNode = new LearningRuleNode(learningRule); // TODO name updating
            learningRuleNode.setName("Learning rule");
            return learningRuleNode;
        }

        //TODO: list all neuron nodes here using LayerChildren, and if there are too many neurons put text too many neurons to inspect
//        Node neuronNode = new NeuronNode((layer.getNeuronAt(0)));
//        neuronNode.setName(layer.getNeurons().size() + " neurons");
//        layerNode.getChildren().add(new Node[]{neuronNode});
    }
}
