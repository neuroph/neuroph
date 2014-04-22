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
    private boolean showActivationLevels;
    private boolean showConnectionWeights;
    private boolean weightHighlighting;

    public ScenePreferences() {
        showConnections = true;
        showActivationSize = false;
        showActivationColor = true;
        showActivationLevels = true;
        showConnectionWeights = false;
        weightHighlighting = false;
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

    public boolean isShowActivationLevels() {
        return showActivationLevels;
    }

    public void setShowActivationLevels(boolean showActivationLevels) {
        this.showActivationLevels = showActivationLevels;
    }

    public boolean isShowConnectionWeights() {
        return showConnectionWeights;
    }

    public void setShowConnectionWeights(boolean showConnectionWeights) {
        this.showConnectionWeights = showConnectionWeights;
    }

    public boolean isWeightHighlighting() {
        return weightHighlighting;
    }

    public void setWeightHighlighting(boolean weightHighlighting) {
        this.weightHighlighting = weightHighlighting;
    }
    
    
}
