package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.popup.NeuronConnectionPopupMenuProvider;

/**
 * This class represents a connection between two neurons
 * http://bits.netbeans.org/dev/javadoc/org-netbeans-api-visual/org/netbeans/api/visual/widget/ConnectionWidget.html
 *
 * http://platform.netbeans.org/graph/common-usages.html // conenction anchors
 */
public class NeuronConnectionWidget extends ConnectionWidget {

    private Connection connection;
    private NeuronWidget src;
    private NeuronWidget trg;
    
    private static final Color DEFAULT_COLOR  = Color.BLACK;
    private static final Color HOVER_COLOR  = Color.GRAY;
    //private static final Color SELECTED_COLOR  = Color.RED;

    public NeuronConnectionWidget(Scene scene, Connection connection, NeuronWidget src, NeuronWidget trg) {
        super(scene);
        this.connection = connection;
        this.src = src;
        this.trg = trg;
        getActions().addAction(ActionFactory.createPopupMenuAction(new NeuronConnectionPopupMenuProvider(src, trg)));
        
        WidgetAction hoverAction = ActionFactory.createHoverAction(new TwoStateHoverProvider() {

            public void unsetHovering(Widget widget) {
                setLineColor(DEFAULT_COLOR);
            }

            public void setHovering(Widget widget) {
                setLineColor(HOVER_COLOR);
            }
        });
        getScene().getActions().addAction(hoverAction);
        getActions().addAction(hoverAction);
    }

    public Connection getConnection() {
        return connection;
    }

    public void removeConnection() {
        trg.getNeuron().removeInputConnectionFrom(src.getNeuron());
    }

    public NeuronWidget getSrc() {
        return src;
    }

    public NeuronWidget getTrg() {
        return trg;
    }
}
