/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.palette;

import org.neuroph.netbeans.visual.widgets.NeuralLayerWidget;

/**
 *
 * @author hrza
 */
public class NeuralLayerWidgetStack {

    private static NeuralLayerWidget connectWidget;

    public static NeuralLayerWidget getConnectionWidget() {
        if (connectWidget != null) {
            return connectWidget;
        }
        throw new RuntimeException("No LayerWidget is Selected");
    }

    public static void setConnectWidget(NeuralLayerWidget widget) {
        connectWidget = widget;
    }
}
