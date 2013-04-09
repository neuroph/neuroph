/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuronConnectionWidget;

/**
 *
 * @author Ana
 */
public class ConnectionSelectionProvider implements SelectProvider{

    public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
       return false;
    }

    public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
        return true;
    }

    public void select(Widget widget, Point point, boolean bln) {
        NeuralNetworkScene scene = (NeuralNetworkScene) widget.getScene();
        widget.getActions().addAction(new KeyboardMoveAction((NeuronConnectionWidget)widget)); 
        ((NeuronConnectionWidget)widget).changeSelection();
    }
    
}
