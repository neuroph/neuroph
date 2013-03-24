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
 * @author Damir
 */
public class NeuronConnectProvider implements ConnectProvider {

    @Override
    public boolean isSourceWidget(Widget sourceWidget) {
        return ((sourceWidget instanceof NeuronWidget) && (sourceWidget != null));
    }

    @Override
    public ConnectorState isTargetWidget(Widget src, Widget trg) {
        return (trg instanceof NeuronWidget || trg instanceof NeuralLayerWidget) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public boolean hasCustomTargetWidgetResolver(Scene arg0) {
        return false;
    }

    @Override
    public Widget resolveTargetWidget(Scene arg0, Point arg1) {
        return null;
    }

    @Override
    public void createConnection(Widget source, Widget target) {
        // connect neuron to neuron
        if ((source instanceof NeuronWidget) && (  target instanceof NeuronWidget)) {
            NeuralNetworkScene scene = (NeuralNetworkScene) source.getScene();
            ((Connectable) source).createConnectionTo(target);
            scene.refresh();
        }
        // connect neuron to layer (all neurons in layer)
        else if ((source instanceof NeuronWidget) && (target instanceof NeuralLayerWidget)) {
            NeuralNetworkScene scene = (NeuralNetworkScene) source.getScene();
            ((Connectable) source).createConnectionTo(target);
            scene.refresh();            
        }
    } 
    
    
}
