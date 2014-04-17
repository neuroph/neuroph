package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;

/**
 *
 * @author Ana
 */
    public class KeyboardDeleteAction extends WidgetAction.Adapter {

    NeuralNetworkScene scene;

    public KeyboardDeleteAction(NeuralNetworkScene scene) {
        this.scene = scene;
    }

    @Override
    public WidgetAction.State keyPressed(Widget widget, WidgetAction.WidgetKeyEvent event) {
        for (Object object : scene.getSelectedObjects()) {
            if ((event.getKeyCode() == KeyEvent.VK_DELETE)) {
                if (object instanceof Layer) {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete selected layer?", "Delete Layer?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        Layer myLayer = (Layer) object;
                    //    myLayer.getParentNetwork().removeLayer(myLayer);
                        scene.getNeuralNetworkEditor().removeLayer(myLayer);
                        scene.refresh();
                    }
                }
                if (object instanceof Neuron) {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete selected neuron?", "Delete Neuron", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        Neuron myNeuron = (Neuron) object;
                        //myNeuron.getParentLayer().removeNeuron(myNeuron);
                        scene.getNeuralNetworkEditor().removeNeuron(myNeuron);
                        scene.refresh();
                    }
                }
                if (object instanceof Connection) {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete selected connection?", "Delete Connection", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        Neuron srcNeuron = ((Connection) object).getFromNeuron();
                        Neuron trgNeuron = ((Connection) object).getToNeuron();
                        trgNeuron.removeInputConnectionFrom(srcNeuron);
                      //  scene.getNeuralNetworkEditor().removeConnection(widget, widget);
                        scene.refresh();
                    }
                }
            }
        }

        return State.CONSUMED;
    }
}
