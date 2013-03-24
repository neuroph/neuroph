package org.neuroph.netbeans.visual;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;

public class NetworkEditor {

    NeuralNetwork network;

    public NetworkEditor(NeuralNetwork network) {
        this.network = network;
    }

    public void setNetwork(NeuralNetwork network) {
        this.network = network;
    }

    public void addLayer(int idx, Layer layer) {
        network.addLayer(idx, layer);

    }

    public void removeLayer(Layer layer) {
        network.removeLayer(layer);
    }

    public void addNeuron(int layerIndex, Neuron neuron) {
        network.getLayerAt(layerIndex).addNeuron(neuron);
    }

    public void removeNeuron(int layerIndex, Neuron neuron) {
        network.getLayerAt(layerIndex).removeNeuron(neuron);
    }

    public void addConnection(Neuron fromNeuron, Neuron toNeuron, double weightVal) {
    //public void addConnection(int fromLayer, int fromNeuron, int toLayer, int toNeuron) {
        /*
        network.getLayerAt(toLayer).getNeuronAt(toNeuron).
                addInputConnection(network.getLayerAt(fromNeuron).getNeuronAt(fromNeuron));
         * 
         */

        network.createConnection(fromNeuron, toNeuron, weightVal);

    }

    public void removeConnection(Neuron fromNeuron, Neuron toNeuron) {
        toNeuron.removeInputConnectionFrom(fromNeuron);
    }




    public NeuralNetwork getNetwork() {
        return network;
    }

}
