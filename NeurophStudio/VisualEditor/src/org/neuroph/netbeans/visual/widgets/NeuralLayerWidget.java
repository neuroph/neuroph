package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.NeuralLayerType;
import org.neuroph.netbeans.visual.popup.NeuralLayerPopupMenuProvider;
import org.neuroph.netbeans.visual.widgets.actions.*;
import org.neuroph.util.ConnectionFactory;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Damir Kocic
 * @author Zoran Sevarac
 */
public class NeuralLayerWidget extends IconNodeWidget implements Lookup.Provider, Connectable {

    private NeuralLayerType type;
    private final Lookup lookup;
    private boolean selected;
    
    public NeuralLayerWidget(NeuralNetworkScene scene, Layer layer) {
        super(scene);
        this.lookup = Lookups.singleton(layer);
        setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 15));
        setBorder(BorderFactory.createRoundedBorder(5, 5, Color.white, Color.black));
        setPreferredSize(new Dimension(80, 60));

        getActions().addAction(ActionFactory.createAcceptAction(new NeuralLayerWidgetAcceptProvider(this)));
        getActions().addAction(ActionFactory.createPopupMenuAction(new NeuralLayerPopupMenuProvider()));
        getActions().addAction(ActionFactory.createExtendedConnectAction( scene.getInterractionLayer(), new LayerConnectProvider()));
        getActions().addAction(ActionFactory.createSelectAction(new LayerSelectProvider())); // move this above connection action to react to it before connection
        
//        getActions().addAction(scene.createObjectHoverAction());

        WidgetAction hoverAction = ActionFactory.createHoverAction(new TwoStateHoverProvider() {

            public void unsetHovering(Widget widget) {
                setBorder(BorderFactory.createRoundedBorder(5, 5, Color.white, Color.BLACK));
            }

            public void setHovering(Widget widget) {
                setBorder(BorderFactory.createRoundedBorder(5, 5, Color.white, Color.RED));
            }
        });
        
        getScene().getActions().addAction(hoverAction);
        getActions().addAction(hoverAction);
        
        //  getActions().addAction(ActionFactory.createSelectAction(new LayerSelectProvider()));
    }
    
    

    public Layer getLayer() {
        return (Layer) lookup.lookup(Layer.class);
    }

    public NeuralLayerType getType() {
        return type;
    }

    public void setType(NeuralLayerType type) {
        this.type = type;
    }

    public Lookup getlookup() {
        return this.lookup;
    }

    public boolean isAcceptableWidget(Widget widget) {
        return widget instanceof NeuronWidget;
    }

    public void createConnectionTo(Widget targetWidget) {

        Layer myLayer = getLayer();
        if (targetWidget instanceof NeuronWidget) { // Connect all Neurons from current Layer to One Pointed Neuron
            Neuron toNeuron = ((NeuronWidget) targetWidget).getNeuron();

            for (Neuron fromNeuron : myLayer.getNeurons()) {
                ConnectionFactory.createConnection(fromNeuron, toNeuron);
            }
            

        } else {  // Else will be only NeuralLayer Widget
            Layer targetLayer = ((NeuralLayerWidget) targetWidget).getLayer();
            for (Neuron fromNeuron : myLayer.getNeurons()) {
                for (Neuron toNeuron : targetLayer.getNeurons()) {
                    ConnectionFactory.createConnection(fromNeuron, toNeuron);
                }
            }
        }
    }
    
//    @Override
//    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
//            super.notifyStateChanged(previousState, state);
//            final boolean isHoveredBorder = state.isHovered();
//
////            setBorder(state.isSelected() ? (isHoveredBorder ? resizeSelectedBorder : selectedBorder)
////                    : (isHoveredBorder ?  resizeBorder : unselectedBorder));
//            
//            if (isHoveredBorder) {
//                setBorder(BorderFactory.createRoundedBorder(5, 5, Color.white, Color.RED));
//            } else {
//                setBorder(BorderFactory.createRoundedBorder(5, 5, Color.white, Color.BLACK));
//            }
//            
//            getScene().validate();
//        }
    


    public void setSelected(boolean selected) {

        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void changeSelection() {
        if (isSelected()) {
            setSelected(false);
            setBorder(BorderFactory.createRoundedBorder(5, 5, Color.white, Color.black));
        } else {
            setSelected(true);
            setBorder(BorderFactory.createRoundedBorder(5, 5, Color.red, Color.black));
        }
    }

}