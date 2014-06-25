package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.text.DecimalFormat;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.popup.NeuralLayerPopupMenuProvider;
import org.neuroph.netbeans.visual.widgets.actions.*;
import org.neuroph.util.ConnectionFactory;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Damir Kocic
 * @author Zoran Sevarac
 */
public class NeuralLayerWidget extends IconNodeWidget implements Lookup.Provider, Connectable {

    private final Lookup lookup;
    private Layer layer;
    private static final Border DEFAULT_BORDER = BorderFactory.createRoundedBorder(5, 5, Color.white, Color.BLACK);
    private static final Border HOVER_BORDER = BorderFactory.createRoundedBorder(5, 5, new Color(240, 240, 240), Color.GRAY);
    private static final Border SELECTED_BORDER = BorderFactory.createRoundedBorder(5, 5, new Color(240, 240, 250), Color.black);

    public NeuralLayerWidget(NeuralNetworkScene scene, Layer layer) {
        super(scene);
        this.layer = layer;
        lookup = Lookups.fixed(layer, this); // either move to private constructor or override getLookup method
        setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 15));
        setBorder(DEFAULT_BORDER);
        setPreferredSize(new Dimension(200, 80));

        // set layer label
        String layerLabel = layer.getLabel();
        if ((layerLabel == null) || layerLabel.isEmpty()) {
            int layerIdx = layer.getParentNetwork().indexOf(layer);
            layerLabel = "Layer " + (layerIdx + 1); // how to get idex position from here
        }
        this.setLabel(layerLabel);

        getActions().addAction(ActionFactory.createAcceptAction(new NeuralLayerWidgetAcceptProvider(this)));
        getActions().addAction(ActionFactory.createPopupMenuAction(new NeuralLayerPopupMenuProvider()));
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new LayerConnectProvider()));
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(scene.createObjectHoverAction());

        drawChildren();
    }

    public Layer getLayer() {
        return lookup.lookup(Layer.class);
    }

    public Lookup getlookup() {
        return this.lookup;
    }

    @Override
    public void createConnectionTo(Widget targetWidget) {

        Layer myLayer = getLayer();
        if (targetWidget instanceof NeuronWidget) { // Connect all Neurons from current Layer to One Pointed Neuron            
            Neuron toNeuron = ((NeuronWidget) targetWidget).getNeuron();
            // TODO: move connection logic to editor / ConnectionFactory
            for (Neuron fromNeuron : myLayer.getNeurons()) {
                ConnectionFactory.createConnection(fromNeuron, toNeuron);
            }

        } else {  // Else will be only NeuralLayer Widget
            Layer targetLayer = ((NeuralLayerWidget) targetWidget).getLayer();
            // TODO: move connection logic to editor / ConnectionFactory
            ConnectionFactory.fullConnect(myLayer, targetLayer);
        }
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        super.notifyStateChanged(previousState, state);

        if (state.isSelected()) {
            setBorder(SELECTED_BORDER);
        } else {
            if (state.isHovered()) {
                setBorder(HOVER_BORDER);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                setBorder(DEFAULT_BORDER);
            }
        }
    }
    
    public final void drawChildren() {
        if (layer.getNeuronsCount() == 0) {
            // if layer is empty write 'Empty layer'            
            drawEmptyLayer();
        } else if (layer.getNeuronsCount() > NeuralNetworkScene.TOO_MANY_NEURONS) {
            // if layer cointains too many neurons write 'Too many neurons to display'
            drawTooManyNeurons();
        } else {
            drawNeuronWidgets();
        }
    }    

    // if layer is empty writes 'Empty layer'
    private void drawEmptyLayer() {
        LabelWidget emptyLabel = new LabelWidget(this.getScene(), "Empty Layer");
        emptyLabel.setForeground(Color.LIGHT_GRAY);
        emptyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        setLayout(LayoutFactory.createAbsoluteLayout());
        emptyLabel.setPreferredLocation(new Point(80, 25));
        setPreferredSize(new Dimension(270, (int) getPreferredSize().getHeight()));
        addChild(emptyLabel);

        LabelWidget emptyLabel2 = new LabelWidget(this.getScene(), "Drag n' drop  or right click to add neurons");
        emptyLabel2.setForeground(Color.LIGHT_GRAY);
        emptyLabel2.setFont(new Font("Arial", Font.PLAIN, 11));
        emptyLabel2.setPreferredLocation(new Point(5, 40));
        addChild(emptyLabel2);
    }

    // if layer cointains too many neurons writes message 'Too many neurons to display'    
    private void drawTooManyNeurons() {
        LabelWidget tooManyLabel = new LabelWidget(this.getScene(), "Too many neurons to display " + layer.getNeuronsCount());
        tooManyLabel.setForeground(Color.LIGHT_GRAY);
        tooManyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        setLayout(LayoutFactory.createAbsoluteLayout());
        tooManyLabel.setPreferredLocation(new Point(20, 25));
        setPreferredSize(new Dimension(300, (int) getPreferredSize().getHeight()));
        addChild(tooManyLabel);
    }



    private void drawNeuronWidgets() {
        NeuralNetworkScene nnScene = (NeuralNetworkScene)this.getScene();
        ScenePreferences scenePreferences = nnScene.getScenePreferences();
        
                // otherwise add neurons to layer
                for (int n = 0; n < layer.getNeuronsCount(); n++) {
                    Neuron neuron = layer.getNeuronAt(n);
                    NeuronWidget neuronWidget = new NeuronWidget(nnScene, neuron);
                    neuronWidget.setActivationSize(scenePreferences.isShowActivationSize());
                    neuronWidget.setActivationColor(true /*scenePreferences.isShowActivationColor()*/);

                    if (nnScene.getObjects().contains(neuron)) {
                        nnScene.removeObject(neuron);
                    }
                    nnScene.addObject(neuron, neuronWidget);

                    resizeLayer(); // TODO: remove this method from here
                    //Napravio wrapper oko neuronWidget n label da bi label pisao unutar widgeta. Koristim OverlayLayout
                    
                    IconNodeWidget neuronWrapperWidget1 = new IconNodeWidget(nnScene);
                    neuronWrapperWidget1.setLayout(LayoutFactory.createVerticalFlowLayout());
                    IconNodeWidget neuronWrapperWidget = new IconNodeWidget(nnScene);
                    neuronWrapperWidget.setLayout(LayoutFactory.createOverlayLayout());
                    neuronWrapperWidget.addChild(neuronWidget);
                    double output = neuronWidget.getNeuron().getOutput();
                    DecimalFormat df = new DecimalFormat("#.###");
                    LabelWidget label = new LabelWidget(nnScene, df.format(output));
                    label.setForeground(Color.white);
                    label.setAlignment(LabelWidget.Alignment.CENTER);
                    label.setVerticalAlignment(LabelWidget.VerticalAlignment.CENTER);
                    label.setVisible(scenePreferences.isShowActivationLevels());
                    LabelWidget neuronLabel = new LabelWidget(nnScene, neuron.getLabel());
                    neuronLabel.setForeground(Color.BLACK);
                    neuronLabel.setAlignment(LabelWidget.Alignment.CENTER);
                    neuronLabel.setVerticalAlignment(LabelWidget.VerticalAlignment.BOTTOM);

                    neuronWrapperWidget.addChild(label);
                    neuronWrapperWidget1.addChild(neuronWrapperWidget);
                    neuronWrapperWidget1.addChild(neuronLabel);
                    addChild(neuronWrapperWidget1);
                    if (neuron.getLabel() == null) {
                        neuronLabel.setVisible(false);
                    } else {
                        neuronLabel.setVisible(true);
                    }
                    
                    // these two should be removed, once we find different solution 
//                    nnScene.getNeurons().add(neuron);
//                    nnScene.getNeuronsAndWidgets().put(neuron, neuronWidget);
                }
    }
    
    // why are we doing manual resize? it should be done automatically using FlowLayout
    private void resizeLayer() {
        int i = getChildren().size(); // adjust size to th enumber of neurons
        Dimension d = new Dimension(((int) (i * 65)) + 70 , (int) getPreferredSize().getHeight());
        setPreferredSize(d);
    }    

    
}