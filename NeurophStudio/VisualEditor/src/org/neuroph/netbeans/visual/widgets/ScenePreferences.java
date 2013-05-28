/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.neuroph.core.Neuron;

/**
 *
 * @author Maja
 */
public class ScenePreferences {

    NeuralNetworkScene scene;
    private boolean showConnections = true;
    private boolean showActivationSize = false;
    private boolean showActivationColor = false;

    public ScenePreferences(NeuralNetworkScene scene) {
        this.scene = scene;
    }

    void showActivationSize() {
        showActivationSize(showActivationSize);
    }

    void showActivationColor() {
        showActivationColor(showActivationColor);
    }

    public void showActivationSize(boolean show) {
        HashMap<Neuron, NeuronWidget> neuronsAndWidgets = scene.getNeuronsAndWidgets();
        for (Neuron neuron : neuronsAndWidgets.keySet()) {
            if (!show) {
                neuronsAndWidgets.get(neuron).setPreferredSize(new Dimension(50, 50));
            } else {
                int size = NeuralNetworkUtils.getSize(neuron);
                neuronsAndWidgets.get(neuron).setPreferredSize(new Dimension(size, size));
            }
        }
        showActivationSize = show;
    }

    public void showActivationColor(boolean show) {
        HashMap<Neuron, NeuronWidget> neuronsAndWidgets = scene.getNeuronsAndWidgets();
        for (Neuron neuron : neuronsAndWidgets.keySet()) {
            if (!show) {
                Border border = BorderFactory.createRoundedBorder(50, 50, Color.red, Color.black);
                neuronsAndWidgets.get(neuron).setBorder(border);
            } else {
                Border border = BorderFactory.createRoundedBorder(50, 50, NeuralNetworkUtils.getColor(neuron), Color.black);
                neuronsAndWidgets.get(neuron).setBorder(border);
            }
        }
        showActivationColor = show;
    }

    public void showConnections(boolean show) {
        if (showConnections != show) {
            showConnections = show;
            scene.refresh();
        }
    }

    public boolean isShowConnections() {
        return showConnections;
    }

    public void setShowConnections(boolean showConnections) {
        this.showConnections = showConnections;
    }

    public boolean isShowActivationSize() {
        return showActivationSize;
    }

    public void setShowActivationSize(boolean showActivationSize) {
        this.showActivationSize = showActivationSize;
    }

    public boolean isShowActivationColor() {
        return showActivationColor;
    }

    public void setShowActivationColor(boolean showActivationColor) {
        this.showActivationColor = showActivationColor;
    }
}
