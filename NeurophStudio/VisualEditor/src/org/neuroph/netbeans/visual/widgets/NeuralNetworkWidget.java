package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
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
}