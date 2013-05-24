package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.Point;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.netbeans.visual.widgets.Connectable;
import org.neuroph.netbeans.visual.widgets.NeuralLayerWidget;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuronWidget;

/**
 *
 * @author hrza
 */
public class LayerConnectProvider implements ConnectProvider {

    public LayerConnectProvider() {
 
    }

    public boolean isSourceWidget(Widget widget) {
        return (widget instanceof NeuralLayerWidget);
    }

    public ConnectorState isTargetWidget(Widget widget, Widget widget1) {
        return (widget1 instanceof NeuronWidget || widget1 instanceof NeuralLayerWidget) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    public boolean hasCustomTargetWidgetResolver(Scene scene) {
        return false;
    }

    public Widget resolveTargetWidget(Scene scene, Point point) {
        return null;
    }

    public void createConnection(Widget sourceWidget, Widget targetWidget) {
        if ((targetWidget instanceof NeuralLayerWidget) && (sourceWidget instanceof NeuralLayerWidget)) { 
            NeuralNetworkScene scene = (NeuralNetworkScene) sourceWidget.getScene();
            ((Connectable) sourceWidget).createConnectionTo(targetWidget);
            scene.refresh();
        } 
        else if ((sourceWidget instanceof NeuralLayerWidget) && ( targetWidget instanceof NeuronWidget)) { 
            NeuralNetworkScene scene = (NeuralNetworkScene) sourceWidget.getScene();
            ((Connectable) sourceWidget).createConnectionTo(targetWidget);
            scene.refresh();
        }
    }
}
