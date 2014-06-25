package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.popup.NeuronPopupMenuProvider;
import org.neuroph.netbeans.visual.widgets.actions.NeuronConnectProvider;
import org.neuroph.netbeans.visual.widgets.actions.NeuronWidgetAcceptProvider;
import org.neuroph.util.ConnectionFactory;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Dva bug-a za selekciju: elektuje objekat ali ne menja stanje widgeta
 * ne poziva metodi notifyChanged sto je verovatno posledica ovog gore
 
 * @author Zoran Sevarac
 */
public class NeuronWidget extends IconNodeWidget /*Widget*/ implements Lookup.Provider, Connectable {

    private Neuron neuron;
    private final Lookup lookup;
    private List<ConnectionWidget> connections;
    public static final Border DEFAULT_BORDER = BorderFactory.createRoundedBorder(50, 50, Color.red, Color.black);
    public static final Border HOVER_BORDER = BorderFactory.createRoundedBorder(50, 50, Color.YELLOW, Color.GRAY); //zuto na hover
    public static final Border SELECTED_BORDER = BorderFactory.createRoundedBorder(50, 50, Color.yellow, Color.black);

    public NeuronWidget(NeuralNetworkScene scene, Neuron neuron) {
        super(scene);
        connections = new ArrayList<>();
        this.neuron = neuron;
        lookup = Lookups.fixed(neuron, this);
        
        getActions().addAction(ActionFactory.createAcceptAction(new NeuronWidgetAcceptProvider(this)));
        getActions().addAction(ActionFactory.createPopupMenuAction(new NeuronPopupMenuProvider()));
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new NeuronConnectProvider()));
        getActions().addAction(scene.createSelectAction());

        getActions().addAction(scene.createObjectHoverAction());

        setToolTipText("Hold Ctrl and drag to create connection");
        setPreferredSize(new Dimension(50, 50));
        setBorder(DEFAULT_BORDER);  
        setOpaque(false);
    }

    public Neuron getNeuron() {
        return this.neuron;
    }

    public void addConnection(ConnectionWidget cw) {
        connections.add(cw);
    }

    public void removeAllConnections() {
        connections.clear();
    }

    public List<ConnectionWidget> getConnections() {
        return connections;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public void createConnectionTo(Widget targetWidget) {
        if (targetWidget instanceof NeuralLayerWidget) {
            Layer targetLayer = ((NeuralLayerWidget) targetWidget).getLayer();
            for (Neuron targetNeuron : targetLayer.getNeurons()) {
                ConnectionFactory.createConnection(neuron, targetNeuron);
            }
        } else { // Ukoliko je  neuron widget
            Neuron targetNeuron = ((NeuronWidget) targetWidget).getNeuron();
            ConnectionFactory.createConnection(neuron, targetNeuron);
        }
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        super.notifyStateChanged(previousState, state);
        Color color = Color.red;
        // allways show neurons color
       // if(((NeuralNetworkScene) getScene()).getScenePreferences().isShowActivationColor())
            color = NeuralNetworkUtils.getColor(neuron);
        
        if (state.isSelected())
            setBorder(SELECTED_BORDER);
        else {
            if (state.isHovered()) {
                setBorder(HOVER_BORDER);  //hover je zut
                //setBorder(BorderFactory.createRoundedBorder(50, 50, color, Color.gray)); //samo okvir se  menja u sivo nahover
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            else
                setBorder(BorderFactory.createRoundedBorder(50, 50, color, Color.black));
        }  
    } 
    
    public void setActivationSize(boolean show) {
        if (!show) {
            setPreferredSize(new Dimension(50, 50));
        } else {
            int size = NeuralNetworkUtils.getSize(getNeuron());
            setPreferredSize(new Dimension(size, size));
        }
    }
    
    public void setActivationColor(boolean show) {
        if (!show) {
            Border border = BorderFactory.createRoundedBorder(50, 50, Color.red, Color.black);
            setBorder(border);
        } else {
            Border border = BorderFactory.createRoundedBorder(50, 50, NeuralNetworkUtils.getColor(getNeuron()), Color.black);
            setBorder(border);
        }
    }
    
}