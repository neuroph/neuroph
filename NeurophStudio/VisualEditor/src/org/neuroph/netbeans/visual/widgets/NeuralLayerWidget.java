package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
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
public class NeuralLayerWidget extends IconNodeWidget implements Lookup.Provider, Connectable, Selectable {

    private NeuralLayerType type;
    private final Lookup lookup;
    private boolean isSelected;
    
    private static final Border DEFAULT_BORDER  = BorderFactory.createRoundedBorder(5, 5, Color.white, Color.BLACK);
    private static final Border HOVER_BORDER = BorderFactory.createRoundedBorder(5, 5, new Color(240, 240, 240), Color.GRAY);
    private static final Border SELECTED_BORDER = BorderFactory.createRoundedBorder(5, 5, new Color(180, 180, 180), Color.black);
    
    public NeuralLayerWidget(NeuralNetworkScene scene, Layer layer) {
        super(scene);
        this.lookup = Lookups.singleton(layer);
        setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 15));
        setBorder(DEFAULT_BORDER);
        setPreferredSize(new Dimension(80, 60));

        getActions().addAction(ActionFactory.createAcceptAction(new NeuralLayerWidgetAcceptProvider(this)));
        getActions().addAction(ActionFactory.createPopupMenuAction(new NeuralLayerPopupMenuProvider()));
        getActions().addAction(ActionFactory.createExtendedConnectAction( scene.getInterractionLayer(), new LayerConnectProvider()));
        getActions().addAction(ActionFactory.createSelectAction(new LayerSelectProvider())); // move this above connection action to react to it before connection
        
        WidgetAction hoverAction = ActionFactory.createHoverAction(new TwoStateHoverProvider() {

            public void unsetHovering(Widget widget) {
                if (isSelected) {
                    setBorder(SELECTED_BORDER);
                } else {
                    setBorder(DEFAULT_BORDER);
                }
            }

            public void setHovering(Widget widget) {
                setBorder(HOVER_BORDER);
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
    


        public void changeSelection() {
        if (isSelected) {
            unselect();
        } else {
             setSelected();
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

     public void unselect() {
        setBorder(DEFAULT_BORDER);
       this.isSelected = false;
    }

    public void setSelected() {
         NeuralNetworkScene scene = (NeuralNetworkScene)this.getScene();
           if (scene.getSelection() != null){
                scene.getSelection().unselect();
           }
        scene.setSelection(this);
        setBorder(SELECTED_BORDER);
        this.isSelected = true;
    }
}