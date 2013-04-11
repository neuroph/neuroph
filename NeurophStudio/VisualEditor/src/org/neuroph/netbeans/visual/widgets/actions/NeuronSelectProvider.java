package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
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
//        NeuralNetworkScene scene = (NeuralNetworkScene) widget.getScene();
//        widget.getActions().addAction(new KeyboardMoveAction((NeuronWidget)widget)); 
//        ((NeuronWidget)widget).changeSelection();
        
        if (widget.getState().isSelected()) {
            widget.setState(widget.getState().deriveSelected(false));
            widget.setBorder(NeuronWidget.DEFAULT_BORDER);            
        }
        else {
            widget.setState(widget.getState().deriveSelected(true));
            widget.setBorder(NeuronWidget.SELECTED_BORDER);
        }
        //widget.getState()
    }
}
