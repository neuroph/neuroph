package org.neuroph.netbeans.visual.widgets;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.geom.AffineTransform;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.explorer.ConnectionNode;
import org.neuroph.netbeans.explorer.ExplorerNeuralNetworkNode;
import org.neuroph.netbeans.explorer.LayerNode;
import org.neuroph.netbeans.explorer.LearningRuleNode;
import org.neuroph.netbeans.explorer.NeuronNode;
import org.neuroph.netbeans.visual.VisualEditorTopComponent;
import org.neuroph.netbeans.visual.NeuralNetworkEditor;
import org.neuroph.netbeans.visual.popup.MainPopupMenuProvider;
import org.neuroph.netbeans.visual.widgets.actions.KeyboardDeleteAction;
import org.openide.explorer.ExplorerManager;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * This class represents scene specialised for displaying neural networks
 *
 * TODO: delegirati iscrtavanje widgeta u svaki widget ponaosob: NeuronWidget -
 * da sam crta sve sto je vezano zanjega NeuralLayerWidget - da sam crta neurone
 * NeuralNetworkWidget da crta layere i konekcije
 *
 * kad se doda neuron da se osvezi samo layer kad se doda lejer da ga docrta
 *
 * svaka komponenta da ima flag koji pokazuje da li je treba iscrtati ponovo
 *
 *
 * @author Zoran Sevarac
 */
public class NeuralNetworkScene extends ObjectScene implements LookupListener {
//http://bits.netbeans.org/dev/javadoc/org-netbeans-api-visual/org/netbeans/api/visual/widget/doc-files/documentation.html

    private LayerWidget mainLayer;          // this layer contains widgets
    private LayerWidget interractionLayer;  // this layer is for drawing connections while connectiong
    private LayerWidget connectionLayer;    // this layer contains connections

    private NeuralNetwork<?> neuralNetwork;
    private NeuralNetworkWidget neuralNetworkWidget;
    //private ImageWidget componentsWidgets = null;   // hold learningRuleWidget - should be moved to nn widget    

    private DataSet dataSet;
    private ImageWidget dataSetWidget = null;
    private LabelWidget dataSetLabel;
    private ImageWidget inputsContainerWidget = null;
    private ImageWidget outputsContainerWidget = null;

    private boolean waitingClick = false;
    private boolean waitingLayerClick = false;

    private InstanceContent content = new InstanceContent();
    private AbstractLookup aLookup = new AbstractLookup(content);

    //  private ArrayList<Neuron> neurons = new ArrayList<>();
    private ArrayList<Layer> layers = new ArrayList<>();

    public static final int TOO_MANY_NEURONS = 100;
    public static final int TOO_MANY_CONNECTIONS = 250;

    private NeuralNetworkEditor networkEditor;
    private VisualEditorTopComponent topComponent;
    private ScenePreferences scenePreferences;
    private ArrayList<NeuronConnectionWidget> connectionsToRoute = new ArrayList<>();

    private final Lookup.Result<ExplorerNeuralNetworkNode> focusedExplorerNeuralNetwork;
    private final Lookup.Result<LearningRuleNode> focusedExplorerLearningRule;
    private final Lookup.Result<LayerNode> focusedExplorerLayer;
    private final Lookup.Result<NeuronNode> focusedExplorerNeuron;
    private final Lookup.Result<ConnectionNode> focusedExplorerConnection;

    public NeuralNetworkScene(NeuralNetwork<?> neuralNet, VisualEditorTopComponent nnetTopComponent) {
        this.neuralNetwork = neuralNet;
        this.neuralNetworkWidget = new NeuralNetworkWidget(this, neuralNetwork);
        addObject(neuralNetwork, neuralNetworkWidget);
//        content.add(neuralNetwork);
        this.topComponent = nnetTopComponent;

        this.networkEditor = new NeuralNetworkEditor(neuralNetwork);
        this.scenePreferences = new ScenePreferences();

        setLayout(LayoutFactory.createOverlayLayout()); // layout for layer widgets

        mainLayer = new LayerWidget(this);         // holds widget 
        mainLayer.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 20));
        // Main Layer children
        dataSetWidget = new ImageWidget(this);
        dataSetLabel = new LabelWidget(this, "DataSet: none (drag n drop to set)");
        dataSetLabel.setForeground(Color.GRAY);
        dataSetLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dataSetWidget.setBorder(BorderFactory.createRoundedBorder(5, 5, Color.white, Color.LIGHT_GRAY));
        dataSetWidget.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 4));
        dataSetWidget.addChild(dataSetLabel);

        inputsContainerWidget = new ImageWidget(this);
        dataSetWidget.addChild(inputsContainerWidget);
        mainLayer.addChild(dataSetWidget);

        mainLayer.addChild(neuralNetworkWidget);

        outputsContainerWidget = new ImageWidget(this);
        outputsContainerWidget.setBorder(BorderFactory.createRoundedBorder(5, 5, Color.white, Color.LIGHT_GRAY));
        mainLayer.addChild(outputsContainerWidget);
        // END Main Layer children
        addChild(mainLayer);
        connectionLayer = new LayerWidget(this);    // draw connections
        addChild(connectionLayer);
        interractionLayer = new LayerWidget(this); // draw connections while creating them
        addChild(interractionLayer);

        // middle-click + drag  Scene.getInputBindings().getPanActionButton()
        getActions().addAction(ActionFactory.createPanAction());
        // ctrl + scroll        Scene.getInputBindings().getZoomActionModifiers()
        getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.1));
        // To support right-click on the scene
        getActions().addAction(ActionFactory.createPopupMenuAction(new MainPopupMenuProvider()));
        // To support selecting background scene (deselecting all widgets)
        getActions().addAction(ActionFactory.createSelectAction(new SceneSelectProvider(), false));
        // Delete action on keyboard
        getActions().addAction(new KeyboardDeleteAction(this));
        // To support drag-and-drop from the palette
        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {
            @Override
            public ConnectorState isAcceptable(final Widget widget, final Point point, final Transferable t) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
//                        DataFlavor flavor = t.getTransferDataFlavors()[3];
//                Class droppedClass = flavor.getRepresentationClass();
                        Image dragImage = getImageFromTransferable(t);
                        JComponent view = widget.getScene().getView();

                        // Graphics2D graphics = (Graphics2D) view.getGraphics(); 
                        Graphics2D graphics = widget.getScene().getGraphics();

                        //Rectangle visRect = view.getVisibleRect();
                        //view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
//                Image dragImage = null;
//                try {
//                    dragImage = (Image) t.getTransferData(DataFlavor.imageFlavor);
//                } catch (UnsupportedFlavorException ex) {
//                    Exceptions.printStackTrace(ex);
//                } catch (IOException ex) {
//                    Exceptions.printStackTrace(ex);
//                } 
                        graphics.drawImage(dragImage,
                                AffineTransform.getTranslateInstance(point.getLocation().getX(),
                                        point.getLocation().getY()),
                                null);
                        // widget.getScene().paint(graphics);
                    }
                });

                DataFlavor dataSetflavor = t.getTransferDataFlavors()[1];
                try {
                    DataObject dsdo = (DataObject) t.getTransferData(dataSetflavor);
                    DataSet ds = dsdo.getLookup().lookup(DataSet.class);

                    if (ds != null) {
                        return ConnectorState.ACCEPT;
                    }

                } catch (UnsupportedFlavorException | IOException ex) {
                    //  Exceptions.printStackTrace(ex);
                }

                return ConnectorState.REJECT;
            }

            @Override
            public void accept(Widget widget, Point point, Transferable t) {
                DataFlavor dataSetflavor = t.getTransferDataFlavors()[1];
                try {
                    DataObject dsdo = (DataObject) t.getTransferData(dataSetflavor);
                    DataSet ds = dsdo.getLookup().lookup(DataSet.class);

                    if (ds.getInputSize() != NeuralNetworkScene.this.neuralNetwork.getInputsCount()) {
                        JOptionPane.showMessageDialog(topComponent, "Number of inputs of data set and neural network must be equal!");
                        return;
                    }
                    dataSet = ds;
                    dataSetLabel.setLabel("DataSet: " + ds.getLabel());
                    topComponent.requestActive();
                } catch (UnsupportedFlavorException | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }));

        addObjectSceneListener(new NeuralNetworkObjectSceneListener(),
                ObjectSceneEventType.OBJECT_FOCUS_CHANGED);

        // Listen to node lookup from the Explorer
        focusedExplorerNeuralNetwork = Utilities.actionsGlobalContext().lookupResult(ExplorerNeuralNetworkNode.class);
        focusedExplorerLearningRule = Utilities.actionsGlobalContext().lookupResult(LearningRuleNode.class);
        focusedExplorerLayer = Utilities.actionsGlobalContext().lookupResult(LayerNode.class);
        focusedExplorerNeuron = Utilities.actionsGlobalContext().lookupResult(NeuronNode.class);
        focusedExplorerConnection = Utilities.actionsGlobalContext().lookupResult(ConnectionNode.class);
        addLookupListeners();
    }

    public NeuralNetwork<?> getNeuralNetwork() {
        return neuralNetwork;
    }

    public NeuralNetworkWidget getNeuralNetworkWidget() {
        return neuralNetworkWidget;
    }

    public NeuralNetworkEditor getNeuralNetworkEditor() {
        return networkEditor;
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public LayerWidget getInterractionLayer() {
        return interractionLayer;
    }

    public LayerWidget getConnectionLayer() {
        return connectionLayer;
    }

    private ExplorerManager getExplorerManager() {
        return topComponent.getExplorerManager();
    }

    public boolean isWaitingLayerClick() {
        return waitingLayerClick;
    }

    public void setWaitingLayerClick(boolean waitingLayerClick) {
        this.waitingLayerClick = waitingLayerClick;
    }

    public boolean isWaitingClick() {
        return waitingClick;
    }

    public void setWaitingClick(boolean waitingClick) {
        this.waitingClick = waitingClick;
    }

    public ScenePreferences getScenePreferences() {
        return scenePreferences;
    }

    public void setTopComponent(VisualEditorTopComponent topComponent) {
        this.topComponent = topComponent;
    }

    // Used to enable selection detection from scene
    private void addLookupListeners() {
        focusedExplorerNeuralNetwork.addLookupListener(this);
        focusedExplorerLearningRule.addLookupListener(this);
        focusedExplorerLayer.addLookupListener(this);
        focusedExplorerNeuron.addLookupListener(this);
        focusedExplorerConnection.addLookupListener(this);
    }

    // Used to disable selection detection from scene, while changing selection in explorer (avoid loop selections)
    private void removeLookupListeners() {
        focusedExplorerNeuralNetwork.removeLookupListener(this);
        focusedExplorerLearningRule.removeLookupListener(this);
        focusedExplorerLayer.removeLookupListener(this);
        focusedExplorerNeuron.removeLookupListener(this);
        focusedExplorerConnection.removeLookupListener(this);
    }

    @Override
    public Lookup getLookup() {
        return aLookup;
    }

    private Image getImageFromTransferable(Transferable transferable) {
        Object o = null;
        try {
            o = transferable.getTransferData(DataFlavor.imageFlavor);
        } catch (IOException | UnsupportedFlavorException | InvalidDnDOperationException ex) {
            // InvalidDnDOperationException thwown by com.jme3.gde.core.scene.SceneApplication
        }

        return o instanceof Image ? (Image) o : ImageUtilities.loadImage("org/netbeans/shapesample/palette/shape1.png");
    }

    // http://bits.netbeans.org/dev/javadoc/org-netbeans-api-visual/org/netbeans/api/visual/widget/doc-files/documentation.html#ValidationProcess
    public void refresh() {
        visualizeNetwork();
        // change focus to visual editor top component
        topComponent.requestActive();
    }

    public void visualizeNetwork() {

        // clear all connections
        connectionLayer.removeChildren();

        redrawNeuralNetworkWidget();

        // create inputs and outputs widgets and connections to inputs and outputs
        redrawInputsWidgets();
        redrawOutputsWidgets();

        if (scenePreferences.isShowConnections()) {
            createConnections();
        }
        this.validate(); // only one call to validate since they are using same scene instance   

        // ako je ovo pre validate gore u if-u, router baca null pointer exception...
        if (scenePreferences.isShowConnections())
            routeConnectionsInSameLayer();
    }

    // Creates layer and neuron widgets
    private void redrawNeuralNetworkWidget() {
        //  neurons = new ArrayList<>();
        layers = new ArrayList<>();

        neuralNetworkWidget.redrawChildWidgets();

        // this shoul dalso be moved to neural network widget
//        LearningRule learningRule = neuralNetwork.getLearningRule();
//        String learningRuleName = "none";        
//        
//        if (learningRule != null) { // if learning rule is set
//            LearningRuleWidget learningRuleWidget = new LearningRuleWidget(this, learningRule);
//            learningRuleName = learningRule.getClass().toString();
//            learningRuleName = learningRuleName.substring(learningRuleName.lastIndexOf(".") + 1);
//
//            learningRuleWidget.setLabel(learningRuleName);
//
//            componentsWidgets = new ImageWidget(this);
//            componentsWidgets.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 4));
//            componentsWidgets.addChild(learningRuleWidget);
//            
//         //   removeObject(learningRule); // fix for affertion exception bellow
// //           addObject(learningRule, learningRuleWidget);
//
//  //          neuralNetworkWidget.addChild(componentsWidgets);
//   //         componentsWidgets.setPreferredSize(new Dimension(500, 40));
//        }
    }

    private void redrawInputsWidgets() {
        inputsContainerWidget.removeChildren();
        inputsContainerWidget.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 5));
        if (neuralNetwork.getInputNeurons() != null && neuralNetwork.getInputNeurons().length < TOO_MANY_NEURONS) {

            for (int i = 0; i < neuralNetwork.getInputNeurons().length; i++) {
                LabelWidget inputLabel = new LabelWidget(this);
                inputLabel.setLabel("In " + (i + 1));
                inputLabel.setBorder(org.netbeans.api.visual.border.BorderFactory.createRoundedBorder(5, 5, Color.white, Color.LIGHT_GRAY));
                inputsContainerWidget.addChild(inputLabel);

                NeuronWidget targetWidget = (NeuronWidget) findWidget(neuralNetwork.getInputNeurons()[i]);

                if (scenePreferences.isShowConnections()) {
                    ConnectionWidget connWidget = new ConnectionWidget(this);

                    connWidget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
                    connWidget.setSourceAnchor(AnchorFactory.createRectangularAnchor(inputLabel));
                    connWidget.setTargetAnchor(AnchorFactory.createRectangularAnchor(targetWidget));
                    connectionLayer.addChild(connWidget);
                }
            }

            // if first layer has more than 100 neurons, create one  input label and connect it with the first layer
        } else if (neuralNetwork.getInputNeurons() != null) {

            LabelWidget inputLabel = new LabelWidget(this);
            inputLabel.setLabel(neuralNetwork.getInputNeurons().length + " Inputs");
            inputLabel.setBorder(org.netbeans.api.visual.border.BorderFactory.createRoundedBorder(5, 5, Color.white, Color.LIGHT_GRAY));
            inputsContainerWidget.addChild(inputLabel);

            Layer sourceLayer = layers.get(0); // verovatno ne mora da bude samo prvi layer ...

            NeuralLayerWidget sourceWidget = (NeuralLayerWidget) findWidget(sourceLayer);
            if (scenePreferences.isShowConnections()) {
                ConnectionWidget connWidget = new ConnectionWidget(this);
                // create connection between input widget and first neural layer widget
                connWidget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
                connWidget.setSourceAnchor(AnchorFactory.createRectangularAnchor(inputLabel));
                connWidget.setTargetAnchor(AnchorFactory.createRectangularAnchor(sourceWidget));
                connectionLayer.addChild(connWidget);
            }

            // neuralNetworkWidget.addChild(0, inputsContainerWidget);
        }
    }

    private void redrawOutputsWidgets() {
        outputsContainerWidget.removeChildren();
        outputsContainerWidget.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 5));
        LabelWidget outputsLabel = new LabelWidget(this, "Outputs:");
        outputsLabel.setForeground(Color.GRAY);
        outputsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        outputsContainerWidget.addChild(outputsLabel);

        if (neuralNetwork.getOutputNeurons() != null && neuralNetwork.getOutputNeurons().length < TOO_MANY_NEURONS) {
            for (int i = 0; i < neuralNetwork.getOutputNeurons().length; i++) {
                LabelWidget outputLabel = new LabelWidget(this);
                outputLabel.setLabel("Out " + (i + 1));
                outputLabel.setBorder(org.netbeans.api.visual.border.BorderFactory.createRoundedBorder(5, 5, Color.white, Color.LIGHT_GRAY));
                outputsContainerWidget.addChild(outputLabel);

                NeuronWidget sourceWidget = (NeuronWidget) findWidget(neuralNetwork.getOutputNeurons()[i]);
                if (scenePreferences.isShowConnections()) {
                    ConnectionWidget connWidget = new ConnectionWidget(this);

                    connWidget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
                    connWidget.setSourceAnchor(AnchorFactory.createRectangularAnchor(sourceWidget));
                    connWidget.setTargetAnchor(AnchorFactory.createRectangularAnchor(outputLabel));
                    connectionLayer.addChild(connWidget);
                }
            }
            //if last neural layer  has more than 100 neurons connect that neural layer widget with one output label
        } else if (neuralNetwork.getOutputNeurons() != null) {
            LabelWidget outputLabel = new LabelWidget(this);
            outputLabel.setLabel("Output " + neuralNetwork.getOutputNeurons().length);
            outputLabel.setBorder(org.netbeans.api.visual.border.BorderFactory.createRoundedBorder(5, 5, Color.white, Color.LIGHT_GRAY));
            outputsContainerWidget.addChild(outputLabel);
            Layer targetlayer = layers.get(layers.size() - 1);

            //connect that neural layer widget with the previous one
            NeuralLayerWidget sourceWidget = (NeuralLayerWidget) findWidget(targetlayer);
            if (scenePreferences.isShowConnections()) {
                ConnectionWidget connWidget = new ConnectionWidget(this);

                connWidget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
                connWidget.setSourceAnchor(AnchorFactory.createRectangularAnchor(sourceWidget));
                connWidget.setTargetAnchor(AnchorFactory.createRectangularAnchor(outputLabel));
                connectionLayer.addChild(connWidget);

            }
        } else {
            LabelWidget notSetLabel = new LabelWidget(this, "Not set. Right click neuron or layer to set");
            notSetLabel.setForeground(Color.GRAY);
            notSetLabel.setFont(new Font("Arial", Font.PLAIN, 9));
            outputsContainerWidget.addChild(notSetLabel);
        }
    }

    /**
     * Creates single connecting line betweeen two layers Used when there is too
     * many connections between two layers
     */
    private void createNeuralLayersConnection(NeuralLayerWidget sourceLayerWidget, NeuralLayerWidget targetLayerWidget) {
        // create single connection line between two layers
        ConnectionWidget connWidget = new ConnectionWidget(this);
        LabelWidget label = new LabelWidget(this);
        int numOfConnections = sourceLayerWidget.getLayer().getNeuronsCount() * targetLayerWidget.getLayer().getNeuronsCount();
        String numOfConnectionsStr = String.valueOf(numOfConnections);
        label.setLabel(numOfConnectionsStr + " connections");
        connWidget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        connWidget.setSourceAnchor(AnchorFactory.createRectangularAnchor(sourceLayerWidget));
        connWidget.setTargetAnchor(AnchorFactory.createRectangularAnchor(targetLayerWidget));
        connWidget.setStroke(new BasicStroke(1, 1, 1, 1, new float[]{5}, 1));

        connWidget.addChild(label);
        connWidget.setConstraint(label, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER, 0.5f);
        connectionLayer.addChild(connWidget);
    }

    // creates connections 
    private void createConnections() {
        connectionsToRoute.clear();

        for (int currentLayerIdx = 0; currentLayerIdx < layers.size(); currentLayerIdx++) {
            Layer currentLayer = layers.get(currentLayerIdx);
            if (currentLayer.getNeuronsCount() > TOO_MANY_NEURONS) {

                if (currentLayerIdx == 0) { // first/input layer
                    NeuralLayerWidget sourceLayerWidget = (NeuralLayerWidget) findWidget(currentLayer); //layersAndWidgets.get(currentLayer);
                    NeuralLayerWidget targetLayerWidget = (NeuralLayerWidget) findWidget(layers.get(currentLayerIdx + 1));
                    createNeuralLayersConnection(sourceLayerWidget, targetLayerWidget);
                    // sledeci layer bi trebalo da crta svoj einput konekcije ovo je mozda nepotrebno...

                } else if (currentLayerIdx == layers.size() - 1) { // last, output layer 
                    //(ne mora da znaci da je iz prethodnog lejera
                    NeuralLayerWidget sourceLayerWidget = (NeuralLayerWidget) findWidget(layers.get(currentLayerIdx - 1));
                    NeuralLayerWidget targetLayerWidget = (NeuralLayerWidget) findWidget(layers.get(currentLayerIdx));
                    createNeuralLayersConnection(sourceLayerWidget, targetLayerWidget);

                } else { // hidden layers                      
                    if (!NeuralNetworkUtils.hasInputConnections(currentLayer)) {
                        continue;
                    }

                    NeuralLayerWidget prevLayerWidget = (NeuralLayerWidget) findWidget(layers.get(currentLayerIdx - 1));
                    NeuralLayerWidget currentLayerWidget = (NeuralLayerWidget) findWidget(currentLayer);
                    createNeuralLayersConnection(prevLayerWidget, currentLayerWidget);
                }

            } else {
                // if layer has less than 100 neurons then draw all connections     

                if (currentLayerIdx > 0) { // if not input layer
                    Layer prevLayer = layers.get(currentLayerIdx - 1);
                    if (prevLayer.getNeuronsCount() > TOO_MANY_NEURONS) {
                        if (!NeuralNetworkUtils.hasInputConnections(currentLayer)) {
                            continue;
                        }

                        // draw connection between two layers    
                        NeuralLayerWidget prevLayerWidget = (NeuralLayerWidget) findWidget(layers.get(currentLayerIdx - 1));
                        NeuralLayerWidget currentLayerWidget = (NeuralLayerWidget) findWidget(currentLayer);
                        createNeuralLayersConnection(prevLayerWidget, currentLayerWidget);
                    }
                }
                int numOfConnections = NeuralNetworkUtils.countConnections(layers.get(currentLayerIdx));

                if (numOfConnections <= TOO_MANY_CONNECTIONS) {

                    for (int j = 0; j < layers.get(currentLayerIdx).getNeuronsCount(); j++) {
                        Neuron targetNeuron = layers.get(currentLayerIdx).getNeuronAt(j);
                        Connection[] inputConnections = targetNeuron.getInputConnections();

                        for (int c = 0; c < inputConnections.length; c++) {
                            NeuronWidget targetWidget = (NeuronWidget) findWidget(targetNeuron);
                            NeuronWidget sourceWidget = (NeuronWidget) findWidget(inputConnections[c].getFromNeuron());
                            if (sourceWidget == null) { // hack when layer is deleted
                                continue;
                            }

                            NeuronConnectionWidget connWidget = createConnectionWidget(inputConnections[c], sourceWidget, targetWidget);
                            connectionLayer.addChild(connWidget);

                            if (getObjects().contains(inputConnections[c])) {
                                removeObject(inputConnections[c]);
                            }
                            addObject(inputConnections[c], connWidget);
                        }
                    }
                } else {
                    NeuralLayerWidget previousLayer = (NeuralLayerWidget) findWidget(layers.get(currentLayerIdx - 1));
                    NeuralLayerWidget nextLayer = (NeuralLayerWidget) findWidget(layers.get(currentLayerIdx));
                    createNeuralLayersConnection(previousLayer, nextLayer);
                }
            }
        }
    }

    private void routeConnectionsInSameLayer() {
        for (NeuronConnectionWidget connWidget : connectionsToRoute) {
            connWidget.setRouter(new RouterConnection(connWidget.getSrc(), connWidget.getTrg(), connWidget.getFirstControlPoint(), connWidget.getLastControlPoint()));
        }
    }

    private NeuronConnectionWidget createConnectionWidget(Connection connection, NeuronWidget sourceWidget, NeuronWidget targetWidget) {
        NeuronConnectionWidget connWidget = new NeuronConnectionWidget(this, connection, sourceWidget, targetWidget);

        connWidget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        connWidget.setSourceAnchor(AnchorFactory.createRectangularAnchor(sourceWidget));
        connWidget.setTargetAnchor(AnchorFactory.createRectangularAnchor(targetWidget));
        double connWeight = connection.getWeight().value;
        if (scenePreferences.isShowConnectionWeights()) {
            LabelWidget label = new LabelWidget(this);
            DecimalFormat fourDForm = new DecimalFormat("#.###");
            label.setLabel(fourDForm.format(connWeight));
            connWidget.addChild(label);
            connWidget.setConstraint(label, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER, 0.5f);
        }
        if (scenePreferences.isWeightHighlighting()) {
            if (connWeight < -0.5) {
                connWidget.setStroke(new BasicStroke((float) 0.5));
            } else if (connWeight >= -0.5 && connWeight < 0.5) {
                connWidget.setStroke(new BasicStroke((float) 1.5));
            } else {
                connWidget.setStroke(new BasicStroke(3));
            }
        } else {
            connWidget.setStroke(new BasicStroke(1));
        }

        sourceWidget.addConnection(connWidget);
        targetWidget.addConnection(connWidget);

        // if this is a connection in same layer it need routing which is don eafter validation. ow jus add to routing list
        if (sourceWidget.getNeuron().getParentLayer() == targetWidget.getNeuron().getParentLayer()) {
            connectionsToRoute.add(connWidget);
        }

        return connWidget;
    }

    public void showActivationSize(boolean show) {
        if (scenePreferences.isShowActivationSize() != show) {
            scenePreferences.setShowActivationSize(show);
            refresh();
        }
    }

    public void showActivationColor(boolean show) {
        // instead of redrawing everything, iterate neuronwidgets and set properties
        if (scenePreferences.isShowActivationColor() != show) {
            scenePreferences.setShowActivationColor(show);
            refresh();
        }
    }

    public void showConnections(boolean show) {
        if (scenePreferences.isShowConnections() != show) {
            scenePreferences.setShowConnections(show);
            refresh();
        }
    }

    public void showActivationLevel(boolean show) {
        if (scenePreferences.isShowActivationLevels() != show) {
            scenePreferences.setShowActivationLevels(show);
            refresh();
        }
    }

    public void showConnectionWeights(boolean show) {
        if (scenePreferences.isShowConnectionWeights() != show) {
            scenePreferences.setShowConnectionWeights(show);
            refresh();
        }
    }

    public void weightHighlighting(boolean highlight) {
        if (scenePreferences.isWeightHighlighting() != highlight) {
            scenePreferences.setWeightHighlighting(highlight);
            refresh();
        }
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result source = (Lookup.Result) ev.getSource();
        Collection instances = source.allInstances();
        if (!instances.isEmpty()) {
            for (Object instance : instances) {
                // Set focused object based on the selection of a Node in Explorer
                if (instance instanceof ExplorerNeuralNetworkNode) {
                    NeuralNetwork neuralNet = ((ExplorerNeuralNetworkNode) instance).getNeuralNet();
                    if (isObject(neuralNet)) {
                        setFocusedObjectAndToggleListeners(neuralNet);
                    }
                } else if (instance instanceof LearningRuleNode) {
                    // TODO deselect everything for now, because there are no LearningRuleWidgets in the scene
                    setFocusedObjectAndToggleListeners(null);
//                    LearningRule learningRule = ((LearningRuleNode) instance).getLearningRule();
//                    if (isObject(learningRule)) {
//                        setSelectedFocusedObjectAndToggleListeners(learningRule);
//                    }
                } else if (instance instanceof LayerNode) {
                    Layer layer = ((LayerNode) instance).getLayer();
                    if (isObject(layer)) {
                        setFocusedObjectAndToggleListeners(layer);
                    }
                } else if (instance instanceof NeuronNode) {
                    Neuron neuron = ((NeuronNode) instance).getNeuron();
                    if (isObject(neuron)) {
                        setFocusedObjectAndToggleListeners(neuron);
                    }
                } else if (instance instanceof ConnectionNode) {
                    Connection connection = ((ConnectionNode) instance).getConnection();
                    if (isObject(connection)) {
                        setFocusedObjectAndToggleListeners(connection);
                    }
                }
                // Process only the first of the instances (selection limited do 1)
                break;
            }
        } else {
            // No component or member selected in explorer, or some other TopComponent is focused, clear selection
            // this doesn't work, because sometimes there is empty instances event, when we have actually just selected some node
            // ExplorerTopComponent
            if (!topComponent.isActivatedLinkedTC()) {
                selectScene();
            }
        }
        validate();
        repaint();
    }

    public void deselectAll() {
        setFocusedObjectAndToggleListeners(null);
    }

    // Selection and content.add need to be here, because on the first opening of the scene
    // oldFocus and newFocus are null, so the event doesn't trigger
    public void selectScene() {
        // Select classDiagram (deselect all)
        setFocusedObjectAndToggleListeners(null);

        // Focus TopComponent when scene is selected
        topComponent.requestFocus();
    }

    // Selected as well as focused, because of how IconNodeWidgets work
    private void setFocusedObjectAndToggleListeners(Object object) {
        removeLookupListeners();
        if (object != null) setSelectedObjects(Collections.singleton(object));
        else setSelectedObjects(Collections.EMPTY_SET);
        setFocusedObject(object);
        addLookupListeners();
    }

    private class NeuralNetworkObjectSceneListener implements ObjectSceneListener {

        @Override
        public void objectAdded(ObjectSceneEvent event, Object addedObject) {
            // on neuron add redraw layer
            // on layer add redraw network/layers
            // on connection add redraw connection between objects
        }

        @Override
        public void objectRemoved(ObjectSceneEvent event, Object removedObject) {
        }

        @Override
        public void objectStateChanged(ObjectSceneEvent event, Object changedObject, ObjectState previousState, ObjectState newState) {
        }

        @Override
        public void selectionChanged(ObjectSceneEvent event, Set<Object> previousSelection, Set<Object> newSelection) {
        }

        @Override
        public void highlightingChanged(ObjectSceneEvent event, Set<Object> previousHighlighting, Set<Object> newHighlighting) {
        }

        @Override
        public void hoverChanged(ObjectSceneEvent event, Object previousHoveredObject, Object newHoveredObject) {
        }

        @Override
        public void focusChanged(ObjectSceneEvent event, Object previousFocusedObject, Object newFocusedObject) {
            // For selection inside the Explorer
            if (previousFocusedObject != null) {
                content.remove(previousFocusedObject);
            } else {
//                content.remove(NeuralNetworkScene.this.neuralNetwork);
            }

            if (newFocusedObject != null) {
                // For properties 
                if (newFocusedObject instanceof NeuralNetwork) {
                    selectNode(getExplorerManager().getRootContext());
                } else if (newFocusedObject instanceof LearningRule) {
                    selectLearningRuleNode((LearningRule) newFocusedObject);
                } else if (newFocusedObject instanceof Layer) {
                    selectLayerNode((Layer) newFocusedObject);
                } else if (newFocusedObject instanceof Neuron) {
                    selectNeuronNode((Neuron) newFocusedObject);
                } else if (newFocusedObject instanceof Connection) {
                    selectConnectionNode((Connection) newFocusedObject);
                }

                // For selection inside the Explorer
                content.add(newFocusedObject);
            } else {
                // For properties
                deselectAllNodes();
            }
        }
    }

    private static class SceneSelectProvider implements SelectProvider {

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {
            NeuralNetworkScene scene = (NeuralNetworkScene) widget;
            scene.selectScene();
        }
    }

    private void selectLearningRuleNode(LearningRule learningRule) {
        for (Node node : getExplorerManager().getRootContext().getChildren().getNodes()) {
            if (node instanceof LearningRuleNode) {
                LearningRuleNode learningRuleNode = (LearningRuleNode) node;
                if (learningRuleNode.getLearningRule() == learningRule) {
                    selectNode(node);
                    return;
                }
            }
        }
    }

    private void selectLayerNode(Layer layer) {
        for (Node node : getExplorerManager().getRootContext().getChildren().getNodes()) {
            if (node instanceof LayerNode) {
                LayerNode layerNode = (LayerNode) node;
                if (layerNode.getLayer() == layer) {
                    selectNode(node);
                    return;
                }
            }
        }
    }

    private void selectNeuronNode(Neuron neuron) {
        for (Node node : getExplorerManager().getRootContext().getChildren().getNodes()) {
            if (node instanceof LayerNode) {
                for (Node childNode : node.getChildren().getNodes()) {
                    if (childNode instanceof NeuronNode) {
                        NeuronNode neuronNode = (NeuronNode) childNode;
                        if (neuronNode.getNeuron() == neuron) {
                            selectNode(childNode);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void selectConnectionNode(Connection connection) {
        for (Node node : getExplorerManager().getRootContext().getChildren().getNodes()) {
            if (node instanceof LayerNode) {
                for (Node childNode : node.getChildren().getNodes()) {
                    if (childNode instanceof NeuronNode) {
                        for (Node grandChildNode : childNode.getChildren().getNodes()) {
                            if (grandChildNode instanceof ConnectionNode) {
                                ConnectionNode connectionNode = (ConnectionNode) grandChildNode;
                                if (connectionNode.getConnection() == connection) {
                                    selectNode(grandChildNode);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Select node in the tree. If null is passed, selection is cleared.
     *
     * @param node
     */
    private void selectNode(Node node) {
        try {
            if (node != null) {
                getExplorerManager().setSelectedNodes(new Node[]{node});
            } else {
                getExplorerManager().setSelectedNodes(new Node[0]);
                getExplorerManager().setExploredContext(null);
            }
        } catch (PropertyVetoException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void deselectAllNodes() {
        selectNode(null);
    }
}
