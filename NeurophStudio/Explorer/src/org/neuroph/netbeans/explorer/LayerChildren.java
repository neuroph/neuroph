package org.neuroph.netbeans.explorer;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author zoran
 */
public class LayerChildren extends Children.Keys<Neuron> {
    private Layer layer;

    public LayerChildren(Layer layer) {
        this.layer = layer;
    }

    @Override
    protected Node[] createNodes(Neuron neuron) {
        NeuronNode neuronNode = new NeuronNode(neuron);
        neuronNode.setName("Neuron " + String.valueOf(neuron.getParentLayer().indexOf(neuron)+1));

        return new Node[] { neuronNode };
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys( layer.getNeurons());
    }

}
