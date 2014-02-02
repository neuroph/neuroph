package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.neuroph.core.Connection;
import org.neuroph.netbeans.visual.popup.NeuronConnectionPopupMenuProvider;


/**
 * This class represents a connection widget between two neurons
 * http://bits.netbeans.org/dev/javadoc/org-netbeans-api-visual/org/netbeans/api/visual/widget/ConnectionWidget.html
 *
 * http://platform.netbeans.org/graph/common-usages.html // conenction anchors
 */
public class NeuronConnectionWidget extends ConnectionWidget {

    private Connection connection;
    private NeuronWidget source;
    private NeuronWidget target;
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final Color HOVER_COLOR = Color.GRAY;
    private static final Color SELECTED_COLOR = Color.RED;

    public NeuronConnectionWidget(Scene scene, Connection connection, NeuronWidget src, NeuronWidget trg) {
        super(scene);
        this.connection = connection;
        this.source = src;
        this.target = trg;

        NeuralNetworkScene nnScene = (NeuralNetworkScene) scene;

        getActions().addAction(ActionFactory.createPopupMenuAction(new NeuronConnectionPopupMenuProvider(src, trg)));
        getActions().addAction(nnScene.createObjectHoverAction());
        getActions().addAction(nnScene.createSelectAction());
       

        /*
         WidgetAction hoverAction = ActionFactory.createHoverAction(new TwoStateHoverProvider() {
         public void unsetHovering(Widget widget) {
         if (isSelected) {
         setLineColor(SELECTED_COLOR);
         } else {
         setLineColor(DEFAULT_COLOR);

         }
         }

         public void setHovering(Widget widget) {
         setLineColor(HOVER_COLOR);
         }
         });
         */
    }

    public Connection getConnection() {
        return connection;
    }

    public void removeConnection() {
        target.getNeuron().removeInputConnectionFrom(source.getNeuron());
    }

    public NeuronWidget getSrc() {
        return source;
    }

    public NeuronWidget getTrg() {
        return target;
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        super.notifyStateChanged(previousState, state); //To change body of generated methods, choose Tools | Templates.

//        if (state.isHovered()) {
//            
//            setLineColor(HOVER_COLOR);
//        } else {
//            if (state.isSelected())
//                setLineColor(SELECTED_COLOR);
//            else
//                setLineColor(DEFAULT_COLOR);
//
//        }

    }
}
