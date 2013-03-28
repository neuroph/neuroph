package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.Color;
import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.netbeans.visual.widgets.Connectable;
import org.neuroph.netbeans.visual.palette.NeuronWidgetStack;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuronWidget;

/**
 *
 * @author hrza
 */ 

public class NeuronSelectProvider implements SelectProvider {

    public NeuronSelectProvider() {
    }

    public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
        return false;
    }

    public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
        return true;
    }

    public void select(Widget widget, Point point, boolean bln) {
        NeuralNetworkScene scene = (NeuralNetworkScene) widget.getScene();
        /*NeuronWidget neuronWidget = ((NeuronWidget) widget);
        neuronWidget.changeSelection();

        if (scene.isWaitingClick()) {
            NeuronWidget waitingClickWidget = NeuronWidgetStack.connectionneuron;
            ((Connectable) waitingClickWidget).createConnectionTo(widget);
            scene.setWaitingClick(false);
            neuronWidget.changeSelection();
        }
        scene.refresh();*/
        
        ((NeuronWidget)widget).changeSelection();
        
    }
}
