package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.Point;
import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.netbeans.visual.widgets.NeuralLayerWidget;
import org.neuroph.netbeans.visual.widgets.NeuronWidget;

/**
 *
 * @author zoran
 */
public class NeurophConnectDecorator implements ConnectDecorator {

    public ConnectionWidget createConnectionWidget(Scene scene) {
        ConnectionWidget widget = new ConnectionWidget(scene);
        return widget;

    }

    public Anchor createSourceAnchor(Widget sourceWidget) {
        if (sourceWidget instanceof NeuronWidget) {
            return AnchorFactory.createCenterAnchor(sourceWidget);
        } else if (sourceWidget instanceof NeuralLayerWidget) {
            return AnchorFactory.createRectangularAnchor(sourceWidget, true);
        }
        
        return AnchorFactory.createCenterAnchor(sourceWidget);

    }

    public Anchor createTargetAnchor(Widget targetWidget) {
        if (targetWidget instanceof NeuralLayerWidget) {
            return AnchorFactory.createRectangularAnchor(targetWidget, true);
        }
        
        return AnchorFactory.createCenterAnchor(targetWidget);                
    }

    public Anchor createFloatAnchor(Point point) {
        return AnchorFactory.createFixedAnchor(point);
    }
    
}
