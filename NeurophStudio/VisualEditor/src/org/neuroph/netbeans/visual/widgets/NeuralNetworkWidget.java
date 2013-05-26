package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.visual.widgets.actions.NeuralNetworkWidgetAcceptProvider;

/**
 *
 * @author Zoran i Marjan
 */
public class NeuralNetworkWidget extends IconNodeWidget {

    private NeuralNetwork neuralNetwork;

    public NeuralNetworkWidget(Scene scene, NeuralNetwork neuralNetwork) {
        super(scene);
        this.neuralNetwork = neuralNetwork;
        this.setMinimumSize(new Dimension(300, 400));
        setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 50));
        setBorder(BorderFactory.createRoundedBorder(5, 5, Color.white, Color.black));
        getActions().addAction(ActionFactory.createAcceptAction(new NeuralNetworkWidgetAcceptProvider(this)));
    }

    public void addLayer(int position, NeuralLayerWidget neuralLayerWidget) {
        neuralNetwork.addLayer(neuralLayerWidget.getLayer());
        addChild(position, neuralLayerWidget);
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }
}