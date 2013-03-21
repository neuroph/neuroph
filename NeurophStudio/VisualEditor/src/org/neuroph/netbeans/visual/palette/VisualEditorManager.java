package org.neuroph.netbeans.visual.palette;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.popup.ConnectionPopupMenuProvider;
import org.neuroph.netbeans.visual.widgets.*;

/*
 * @author hrza
 */
public class VisualEditorManager {

    private LayerWidget interractionLayer;
    private LayerWidget connectionLayer;
    private NeuralNetworkWidget neuralNetWidget;
    private int noOfHidden = 0;
    private NeuralNetworkScene scene;
    private NeuralNetwork neuralNet;

    public VisualEditorManager() {
    }

    public VisualEditorManager(NeuralNetwork neuralNet, NeuralNetworkScene scene) {
        this.neuralNet = neuralNet;
        this.scene = scene;
        createScene();
    }

    public void createScene() {
//        interractionLayer = new LayerWidget(scene);
//        neuralNetWidget = new NeuralNetworkWidget(scene);
//        neuralNetWidget.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 50));
//        neuralNetWidget.setNeuralNetwork(this.neuralNet);
//        neuralNetWidget.setPreferredLocation(new Point(100, 10));
//        neuralNetWidget.setBorder(BorderFactory.createRoundedBorder(5, 5, Color.white, Color.black));
//        connectionLayer = new LayerWidget(neuralNetWidget.getScene());
//        scene.addChild(connectionLayer);
//        scene.addChild(interractionLayer);
//        scene.addChild(neuralNetWidget);

    }

//    public void addNewNeuronWidget(Neuron neuron) {
//        NeuronWidget neuronWidget = WidgetFactory.createNeuronWidget(neuron, scene);
//        neuronWidget.getActions().addAction(ActionFactory.createExtendedConnectAction(connectionLayer, new NeuronConnectProvider(neuralNetWidget)));
//    }
//
//    public void addNewLayerWidget(Layer layer) {
//        NeuralLayerWidget neuralLayerWidget = WidgetFactory.createNeuralLayerWidget(layer, scene);
//  //      neuralLayerWidget.getActions().addAction(ActionFactory.createAlignWithMoveAction(neuralNetWidget, interractionLayer, ActionFactory.createDefaultAlignWithMoveDecorator()));
//    }
//
//    public void addNewNeuralNetworkWidget(NeuralNetwork neuralNetwork) {
//        NeuralNetworkWidget neuralNetworkWidget = WidgetFactory.createNeuralNetwork(neuralNetwork, scene);
//    }

    public void resizeLayer(Widget w) {
        int i = w.getChildren().size();
        Dimension d = new Dimension(((int) (i * 65)) + 10, (int) w.getPreferredSize().getHeight());
        w.setPreferredSize(d);
    }

    public void removeWidgets() {
        neuralNetWidget.removeChildren();
        neuralNetWidget.repaint();
        neuralNetWidget.getScene().validate();
        connectionLayer.removeChildren();
        connectionLayer.getScene().validate();
    }

    public void refresh() {
        removeWidgets();
    }

    public void rebuildScene() {
    }

    public void createConnections(ArrayList<Neuron> neurons, ArrayList<NeuronWidget> neuronWidgets) {

        for (int i = 0; i < neurons.size(); i++) { // iterate from neurons 
            for (int j = 0; j < neurons.size(); j++) { // iterate to neurons
                Connection[] inputConnections = neurons.get(j).getInputConnections(); // get all input connections for current neuron
                for (int k = 0; k < inputConnections.length; k++) { // iterate all input connections
                    if (inputConnections[k].getFromNeuron() == neurons.get(i)) {
                        NeuronConnectionWidget connWidget = new NeuronConnectionWidget(neuralNetWidget.getScene(), inputConnections[k], neuronWidgets.get(i), neuronWidgets.get(j));
                        connWidget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
                        connWidget.setSourceAnchor(AnchorFactory.createRectangularAnchor(neuronWidgets.get(i)));
                        connWidget.setTargetAnchor(AnchorFactory.createRectangularAnchor(neuronWidgets.get(j)));
                        connWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new ConnectionPopupMenuProvider()));
                        neuronWidgets.get(i).addConnection(connWidget);
                        neuronWidgets.get(j).addConnection(connWidget);
                        connectionLayer.addChild(connWidget);
                    }

                }
            }

        }
    }
}
