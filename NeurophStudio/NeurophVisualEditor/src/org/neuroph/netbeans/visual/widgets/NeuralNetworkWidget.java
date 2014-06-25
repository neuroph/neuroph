package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.visual.widgets.actions.NeuralNetworkWidgetAcceptProvider;

/**
 * This class represents widget for neural network
 * @author Zoran Sevarac
 * @author Marjan Hrzic
 */
// use IconNodeWidget to add Label Widget...
public class NeuralNetworkWidget extends /*IconNodeWidget*/ Widget {

    private NeuralNetwork neuralNetwork;
    // do we need lookup here?
    // we could put neuralNetwork in lookup instead in field, like in NeuralLayerWidget

    /**
     * Constructs instance of NeuralNetworkWidget for specified neural network and scene 
     *
     * @param scene parent scene of a widget
     * @param neuralNetwork neural network that this widget represents
     */
    public NeuralNetworkWidget(Scene scene, NeuralNetwork neuralNetwork) {
        super(scene);
        this.neuralNetwork = neuralNetwork;
        this.setMinimumSize(new Dimension(300, 400));
        setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 50)); // vertical flow layout for all layers
        setBorder(BorderFactory.createRoundedBorder(5, 5, 30, 30, Color.white, Color.black));       // border to outline neural network widget
        getActions().addAction(ActionFactory.createAcceptAction(new NeuralNetworkWidgetAcceptProvider(this)));  // accept provider to handle drag n drop
    }


    /**
     * Returns neural network from this widget
     * @return neural network from this widget
     */
    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }
    
    private void drawEmptyNetwork() {
            LabelWidget emptyLabel = new LabelWidget(this.getScene(), "Empty Neural Network");
            emptyLabel.setForeground(Color.LIGHT_GRAY);
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 14));
            this.setLayout(LayoutFactory.createAbsoluteLayout());
            emptyLabel.setPreferredLocation(new Point(60, 160));
            this.addChild(emptyLabel);

            LabelWidget emptyLabel2 = new LabelWidget(this.getScene(), "Drag n' drop  or right click to add layers");
            emptyLabel2.setForeground(Color.LIGHT_GRAY);
            emptyLabel2.setFont(new Font("Arial", Font.PLAIN, 11));
            emptyLabel2.setPreferredLocation(new Point(35, 180));
            this.addChild(emptyLabel2);
    }
    
    public void redrawChildWidgets()  {
        this.removeChildren();
        if (neuralNetwork.getLayersCount() == 0) {
            drawEmptyNetwork();
        } else {
            drawLayerWidgets();
        }                      
    }

    private void drawLayerWidgets() {
        NeuralNetworkScene nnScene = (NeuralNetworkScene)this.getScene();
                
        for (int i = 0; i < neuralNetwork.getLayersCount(); i++) { // iterate all layers in network
            Layer layer = neuralNetwork.getLayerAt(i); // get layer for this widget
            NeuralLayerWidget neuralLayerWidget = new NeuralLayerWidget(nnScene, layer); // create widget for layer

            // TODO: these two should be removed from gere once we find different solution
            nnScene.getLayers().add(layer);
           // nnScene.getLayersAndWidgets().put(layer, neuralLayerWidget);

            if (nnScene.getObjects().contains(layer)) {
               nnScene.removeObject(layer);
            }
            
            nnScene.addObject(layer, neuralLayerWidget);
            addChild(neuralLayerWidget);
        }
    }
    
}