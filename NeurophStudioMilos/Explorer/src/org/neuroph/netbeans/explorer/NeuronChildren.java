
package org.neuroph.netbeans.explorer;

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author zoran
 */
public class NeuronChildren extends Children.Keys<Connection> {

    private Neuron neuron;

    public NeuronChildren(Neuron neuron) {
        this.neuron = neuron;
    }

    @Override
    protected Node[] createNodes(Connection connection) {
        ConnectionNode connectionNode = new ConnectionNode(connection);
        connectionNode.setDisplayName(String.valueOf(connection.getWeight().getValue()));
        return new Node[] { connectionNode };
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys( neuron.getInputConnections());
    }

}
