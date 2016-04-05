package org.neuroph.netbeans.explorer;

import java.util.Arrays;
import java.util.List;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author zoran
 * @author Boris Perović <borisvperovic@gmail.com>
 */
public class LayerChildFactory extends ChildFactory<Neuron> {
    private Layer layer;

    public LayerChildFactory(Layer layer) {
        this.layer = layer;
    }

    @Override
    protected Node createNodeForKey(Neuron key) {
        NeuronNode neuronNode = new NeuronNode(key);
        neuronNode.setName("Neuron " + String.valueOf(key.getParentLayer().indexOf(key)+1));
        return neuronNode;
    }

    @Override
    protected boolean createKeys(List<Neuron> toPopulate) {
        toPopulate.addAll(layer.getNeurons());
        return true;
    }
}
