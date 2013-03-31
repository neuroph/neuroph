package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.popup.NeuronPopupMenuProvider;
import org.neuroph.netbeans.visual.widgets.actions.NeuronConnectProvider;
import org.neuroph.netbeans.visual.widgets.actions.NeuronSelectProvider;
import org.neuroph.netbeans.visual.widgets.actions.NeuronWidgetAcceptProvider;
import org.neuroph.util.ConnectionFactory;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Zoran Sevarac
 */
public class NeuronWidget extends IconNodeWidget implements Lookup.Provider, Connectable {

    private Neuron neuron;
    private final Lookup lookup;
    private boolean selected;
    private List<ConnectionWidget> connections;
    
    private static final Border DEFAULT_BORDER  = BorderFactory.createRoundedBorder(50, 50, Color.red, Color.black);
    private static final Border HOVER_BORDER = BorderFactory.createRoundedBorder(50, 50, new Color(255, 100, 100), Color.GRAY);
    private static final Border SELECTED_BORDER = BorderFactory.createRoundedBorder(50, 50, Color.yellow, Color.black);    
   
    public NeuronWidget(NeuralNetworkScene scene, Neuron neuron) {
        super(scene);
        connections = new ArrayList<ConnectionWidget>();
        this.neuron = neuron;
        lookup = Lookups.singleton(neuron);
        getActions().addAction(ActionFactory.createAcceptAction(new NeuronWidgetAcceptProvider(this)));
        getActions().addAction(ActionFactory.createPopupMenuAction(new NeuronPopupMenuProvider()));
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new NeuronConnectProvider()));
        //getActions().addAction(ActionFactory.createConnectAction(scene.getInterractionLayer(), new NeuronConnectProvider()));
        getActions().addAction(ActionFactory.createSelectAction(new NeuronSelectProvider())); // move this above connection action to react to it before connection
        
        
        WidgetAction hoverAction = ActionFactory.createHoverAction(new TwoStateHoverProvider() {

            public void unsetHovering(Widget widget) {
                setBorder(DEFAULT_BORDER);
                ((NeuralNetworkScene)getScene()).refresh(); //zbog selekcije
            }

            public void setHovering(Widget widget) {
                  setBorder(HOVER_BORDER);
            }
        });
        getScene().getActions().addAction(hoverAction);
        getActions().addAction(hoverAction);
        
        // getActions().addAction(ActionFactory.createContiguousSelectAction(new NeuronWidgetContiguousSelectProvider()));
        setToolTipText("Hold Ctrl and drag to create connection");
        setPreferredSize(new Dimension(50, 50));
        setBorder(DEFAULT_BORDER);
        setOpaque(false);
        selected = false;
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
        return Lookups.singleton(neuron);
    }

    public boolean isAcceptableWidget(Widget w) {
        if (w instanceof NeuronWidget) {
            Neuron connectionNeuron = ((NeuronWidget) w).getNeuron();
            if (connectionNeuron != neuron && !connectionNeuron.getParentLayer().equals(neuron.getParentLayer())) {
                return true;
            }
        }
        return (w instanceof NeuralLayerWidget && !((NeuralLayerWidget) w).getLayer().equals(neuron.getParentLayer()));
    }

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
// TODO Implementacija preko lookup-a

    public void setSelected(boolean selection) {
        if (selection) {
            setBorder(SELECTED_BORDER);
            getNeuron().setLabel("Selected");
        } else {
            setBorder(DEFAULT_BORDER);
            getNeuron().setLabel("Not Selected");
        }
        //this.selected = selection;
    }

    public void changeSelection() {
        if (getNeuron().getLabel().equals("Selected")) {
            setSelected(false);
        } else {
            setSelected(true);
        }
    }

    public boolean isSelected() {
        return selected;
    }

   
}