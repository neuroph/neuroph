/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual;

import java.awt.Label;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuronConnectionWidget;
import org.neuroph.netbeans.visual.widgets.NeuronWidget;
import org.neuroph.nnet.comp.layer.CompetitiveLayer;
import org.neuroph.nnet.comp.layer.InputLayer;
import org.neuroph.nnet.comp.neuron.CompetitiveNeuron;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuronFactory;
import org.neuroph.util.NeuronProperties;

/**
 *
 * @author Ana
 */
public class NeuralNetworkEditor {

    NeuralNetwork neuralNet;

    public NeuralNetworkEditor(NeuralNetwork neuralNet) {
        this.neuralNet = neuralNet;
    }

    public void addCompetitiveLayer(int numOfNeurons, NeuronProperties neuronProperties, int neuralNetWidgetChildrenSize) {
        CompetitiveLayer newCompetitiveLayer = new CompetitiveLayer(numOfNeurons, neuronProperties);
        neuralNet.addLayer(neuralNetWidgetChildrenSize, newCompetitiveLayer);

    }

    public void addCustomLayer(Class<? extends Neuron> someNeuron, Class<? extends TransferFunction> someTF, Class<? extends InputFunction> someIF, int numberOfNeurons, int layerIdx) {
        Layer newLayer = new Layer();
        for (int j = 0; j < numberOfNeurons; j++) {
            Neuron newNeuron = NeuronFactory.createNeuron(new NeuronProperties(someNeuron, someIF, someTF));
            newLayer.addNeuron(newNeuron);
        }
        neuralNet.addLayer(layerIdx, newLayer);
    }

    public void addInputLayer(int numberOfNeurons) {
        InputLayer layer = new InputLayer(numberOfNeurons);
        neuralNet.addLayer(0, layer);
        neuralNet.setInputNeurons(layer.getNeurons());
    }

    public void addEmptyLayer(int index, Layer layer) {
        neuralNet.addLayer(index, layer);
    }

    public void addCompetitiveNeuron(TransferFunction transferFunction, Layer layer) {
        CompetitiveNeuron competitiveNeuron = new CompetitiveNeuron(new WeightedSum(), transferFunction);
        layer.addNeuron(competitiveNeuron);
    }

    public void addNeuron(Layer layer, Neuron neuron, int index) {
        if (index == layer.getNeuronsCount()) {
            layer.addNeuron(neuron);
        } else {
            layer.addNeuron(index, neuron);
        }
    }

    public void setNeuronInputFunction(Neuron neuron, InputFunction inputFunction) {
        neuron.setInputFunction(inputFunction);
    }

    public void setNeuronTransferFunction(Neuron neuron, TransferFunction transferFunction) {
        neuron.setTransferFunction(transferFunction);
    }

    public void setLayerLabel(Layer layer, String label) {
        layer.setLabel(label);

    }

    public void setNeuronLabel(Neuron neuron, String label) {
        neuron.setLabel(label);
    }

    public void setLearningRule(LearningRule learningRule) {
        neuralNet.setLearningRule(learningRule);
    }

//    public void setShowConnections(NeuralNetworkScene scene) {
//        scene.setShowConnections(!scene.isShowConnections());
//    }

    public void removeNeuron(Neuron neuron) {
        Layer layer = neuron.getParentLayer();
        layer.removeNeuron(neuron);
    }

    public void removeLayer(Layer layer) {
        layer.getParentNetwork().removeLayer(layer);
    }

    public void removeAllInputConnections(Layer layer) {
        for (Neuron n : layer.getNeurons()) {
            n.removeAllInputConnections();
        }
    }

    public void removeAllOutputConnections(Layer layer) {
        for (Neuron n : layer.getNeurons()) {
            n.removeAllOutputConnections();
        }
    }

    public void removeAllInputConnections(Neuron neuron) {
        neuron.removeAllInputConnections();
    }

    public void removeAllOutputConnections(Neuron neuron) {
        neuron.removeAllOutputConnections();
    }

    public void removeConnection(Widget srcWidget, Widget trgWidget) {
        if (trgWidget == null) {
            ((NeuronConnectionWidget) srcWidget).removeConnection();
            srcWidget.removeFromParent();
        } else {
            ((NeuronWidget) trgWidget).getNeuron().removeInputConnectionFrom(((NeuronWidget) srcWidget).getNeuron());
        }
    }

    public void createFullConnection(int index) {
        Layer fromLayer = neuralNet.getLayerAt(index - 1);
        Layer toLayer = neuralNet.getLayerAt(index);
        ConnectionFactory.fullConnect(fromLayer, toLayer);
    }

    public void createDirectConnection(int index) {
        Layer fromLayer = neuralNet.getLayerAt(index - 1);
        Layer toLayer = neuralNet.getLayerAt(index);
        int number = 0;
        if (fromLayer.getNeuronsCount() > toLayer.getNeuronsCount()) {
            number = toLayer.getNeuronsCount();
        } else {
            number = fromLayer.getNeuronsCount();
        }

        for (int i = 0; i < number; i++) {
            Neuron fromNeuron = fromLayer.getNeurons()[i];
            Neuron toNeuron = toLayer.getNeurons()[i];
            ConnectionFactory.createConnection(fromNeuron, toNeuron);      
        }
    }
}
