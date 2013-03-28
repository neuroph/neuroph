package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
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
        this.setMinimumSize(new Dimension(250, 100));
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

//    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
//        this.neuralNetwork = neuralNetwork;
//    }

//    //TODO Fix allighment
//    public void addEmptyLabel() {
//        NeuralLayerWidget lw;
//        for (Widget w : getChildren()) { // Prolazak kroz WrapperWidgete, tj. Layer Widgete
//            for (Widget w1 : w.getChildren()) { // Ovde su i LabelWidget i NeuralLayerWidget
//                if (w1 instanceof IconNodeWidget) {
//                    lw = (NeuralLayerWidget) w1;
//                    if (lw.getLayer().getNeuronsCount() == 0) {
//                        lw.getLayer().setLabel("Empty Layer");
//                    }
//                }
//            }
//        }
//    }
//
//    public void addLabelNameForLayers() {
//        NeuralLayerWidget lw;
//        for (Widget w : getChildren()) {
//            if ((lw = (NeuralLayerWidget) w).getLayer().getClass().equals(InputLayer.class)) {
//                LabelWidget label = new LabelWidget(getScene(), "Input Layer");
//                label.setPreferredSize(new Dimension(100, 15));
//                label.setMaximumSize(new Dimension(100, 15));
//                label.setPreferredLocation(new Point(0, 0));
//                lw.addChild(label);
//                label.setVisible(true);
//            }
//            //TODO Finish.
//        }
//    }
}
