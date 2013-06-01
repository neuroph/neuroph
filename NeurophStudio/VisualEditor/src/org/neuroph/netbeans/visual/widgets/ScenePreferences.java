/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.widgets;

/**
 *
 * @author Maja
 */
public class ScenePreferences {

    private boolean showConnections;
    private boolean showActivationSize;
    private boolean showActivationColor;

    public ScenePreferences() {
        showConnections = true;
        showActivationSize = false;
        showActivationColor = false;
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
