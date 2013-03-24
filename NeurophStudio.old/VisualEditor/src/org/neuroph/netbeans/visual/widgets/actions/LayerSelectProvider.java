/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.netbeans.visual.palette.NeuralLayerWidgetStack;
import org.neuroph.netbeans.visual.widgets.NeuralLayerWidget;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;

/**
 *
 * @author hrza
 */
public class LayerSelectProvider implements SelectProvider {

    public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
        return false;
    }

    public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
        return true;
    }

    // TODO Promeniti NeuralLayerWidgetStack da cuva Connectable interfejs 
    public void select(Widget widget, Point point, boolean bln) {
//        ((NeuralLayerWidget) widget).changeSelection();
//        if (((NeuralNetworkScene) widget.getScene()).isWaitingLayerClick()) {
//            NeuralLayerWidget waitingWidget = NeuralLayerWidgetStack.getConnectionWidget();
//            waitingWidget.createConnectionTo(widget);
//            ((NeuralNetworkScene) widget.getScene()).setWaitingLayerClick(false);
//            ((NeuralLayerWidget) widget).changeSelection();
//        }
//
//        ((NeuralNetworkScene) widget.getScene()).refresh();
    }
}
