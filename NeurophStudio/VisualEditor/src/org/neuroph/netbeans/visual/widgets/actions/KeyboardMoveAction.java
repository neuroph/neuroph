/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.Point;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.widgets.NeuralLayerWidget;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkWidget;
import org.neuroph.netbeans.visual.widgets.NeuronConnectionWidget;
import org.neuroph.netbeans.visual.widgets.NeuronWidget;

/**
 *
 * @author Ana
 */
public class KeyboardMoveAction extends WidgetAction.Adapter {

    Widget scene;
    // Layer layer;
    private MoveProvider provider;

    public KeyboardMoveAction(Widget widget) {
        scene = widget;
        //this.layer = layer;
        this.provider = ActionFactory.createDefaultMoveProvider();
        //keyPressed(widget, new WidgetKeyEvent(0, null));
    }

    @Override
    public WidgetAction.State keyPressed(Widget widget, WidgetAction.WidgetKeyEvent event) {
        widget = (Widget)((NeuralNetworkScene) widget.getScene()).getSelection();
        if ((event.getKeyCode() == KeyEvent.VK_DELETE)) {
            if (widget instanceof NeuralLayerWidget) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete selected layer?", "Delete Layer?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Layer myLayer = ((NeuralLayerWidget) widget).getLayer();
                    myLayer.getParentNetwork().removeLayer(myLayer);
                    ((NeuralNetworkScene) widget.getScene()).refresh();
                    provider.movementStarted(widget);

                }
            }
            if (widget instanceof NeuronWidget) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete selected neuron?", "Delete Neuron", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Neuron myNeuron = ((NeuronWidget) widget).getLookup().lookup(Neuron.class);
                    myNeuron.getParentLayer().removeNeuron(myNeuron);
                    ((NeuralNetworkScene) widget.getScene()).refresh();
                    provider.movementStarted(widget);

                }
            }
            if (widget instanceof NeuronConnectionWidget) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete selected connection?", "Delete Connection", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Neuron srcNeuron = ((NeuronConnectionWidget) widget).getSrc().getNeuron();
                    Neuron trgNeuron = ((NeuronConnectionWidget) widget).getTrg().getNeuron();
                    trgNeuron.removeInputConnectionFrom(srcNeuron);
                    ((NeuralNetworkScene) widget.getScene()).refresh();
                    provider.movementStarted(widget);
                }
            }
        }


        return State.CONSUMED;
    }
}
