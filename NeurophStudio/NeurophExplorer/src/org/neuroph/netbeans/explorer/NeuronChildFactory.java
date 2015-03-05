package org.neuroph.netbeans.explorer;

import java.util.Arrays;
import java.util.List;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author zoran
 */
public class NeuronChildFactory extends ChildFactory<Connection> {

    private final Neuron neuron;

    public NeuronChildFactory(Neuron neuron) {
        this.neuron = neuron;
    }

    @Override
    protected Node createNodeForKey(Connection key) {
        ConnectionNode connectionNode = new ConnectionNode(key);
        connectionNode.setDisplayName(String.valueOf(key.getWeight().getValue()));
        return connectionNode;
    }

    @Override
    protected boolean createKeys(List<Connection> toPopulate) {
        toPopulate.addAll(Arrays.asList(neuron.getInputConnections()));
        return true;
    }
}
