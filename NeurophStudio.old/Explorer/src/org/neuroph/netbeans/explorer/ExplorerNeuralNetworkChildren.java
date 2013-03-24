package org.neuroph.netbeans.explorer;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
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
public class ExplorerNeuralNetworkChildren extends Children.Keys<Layer> {
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
    protected Node[] createNodes(Layer layer) {
        LayerNode layerNode = new LayerNode(layer);
        layerNode.setName("Layer " + String.valueOf(layer.getParentNetwork().indexOf(layer)+1));

        //TODO: list all neuron nodes here using LayerChildren, and if there are too many neurons put text too many neurons to inspect
//        Node neuronNode = new NeuronNode((layer.getNeuronAt(0)));
//        neuronNode.setName(layer.getNeurons().size() + " neurons");
//        layerNode.getChildren().add(new Node[]{neuronNode});

        return new Node[] { layerNode };
    }

    /**
     *  Get all objects you want to create nodes for, and call setKeys()
     *  For each element in the array or collection you pass to setKeys(), createNodes() will be called once
     */
    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys( neuralNet.getLayers());
    }
}
