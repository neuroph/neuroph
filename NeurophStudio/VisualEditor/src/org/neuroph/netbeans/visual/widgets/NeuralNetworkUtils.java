package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;

/**
 *
 * @author zoran
 */
public class NeuralNetworkUtils {

    public static boolean hasInputConnections(Layer layer) {

        for (Neuron neuron : layer.getNeurons()) {
            if (neuron.hasInputConnections()) {
                return true;
            }
        }
        return false;
    }

    public static int getSize(Neuron neuron) {
        if (neuron == null) {
            return 30;
        }
        double output = Math.abs(neuron.getOutput());
        if (output <= -0.75) {
            return 15;
        }
        if (output <= -0.5) {
            return 20;
        }
        if (output <= -0.25) {
            return 25;
        }
        if (output <= 0) {
            return 30;
        }
        if (output <= 0.25) {
            return 35;
        }
        if (output <= 0.50) {
            return 40;
        }
        if (output <= 0.75) {
            return 45;
        }
        if (output <= 1) {
            return 50;
        }
        return 30;
    }

    public static Color getColor(Neuron neuron) {
        if (neuron == null) {
            return new Color(192, 192, 192); //gray
        }
        double output = neuron.getOutput();
        if (output == -1) {
            return new Color(0, 0, 255); //blue
        }
        if (output <= -0.9) {
            return new Color(17, 17, 249);
        }
        if (output <= -0.8) {
            return new Color(35, 35, 244);
        }
        if (output <= -0.7) {
            return new Color(52, 52, 238);
        }
        if (output <= -0.6) {
            return new Color(70, 70, 232);
        }
        if (output <= -0.5) {
            return new Color(87, 87, 226);
        }
        if (output <= -0.4) {
            return new Color(105, 105, 221);
        }
        if (output <= -0.3) {
            return new Color(122, 122, 215);
        }
        if (output <= -0.2) {
            return new Color(140, 140, 209);
        }
        if (output <= -0.1) {
            return new Color(157, 157, 203);
        }
        if (output < 0) {
            return new Color(175, 175, 198);
        }
        if (output == 0) {
            return new Color(192, 192, 192); //gray
        }
        if (output <= 0.1) {
            return new Color(198, 175, 175);
        }
        if (output <= 0.2) {
            return new Color(203, 157, 157);
        }
        if (output <= 0.3) {
            return new Color(209, 140, 140);
        }
        if (output <= 0.4) {
            return new Color(215, 122, 122);
        }
        if (output <= 0.5) {
            return new Color(221, 105, 105);
        }
        if (output <= 0.6) {
            return new Color(226, 87, 87);
        }
        if (output <= 0.7) {
            return new Color(232, 70, 70);
        }
        if (output <= 0.8) {
            return new Color(238, 52, 52);
        }
        if (output <= 0.9) {
            return new Color(244, 35, 35);
        }
        if (output < 1) {
            return new Color(249, 17, 17);
        }
        if (output == 1) {
            return new Color(255, 0, 0); //red
        }
                
        return new Color(192, 192, 192); //gray
    }
    public static int countConnections(Layer layer) {
        int count = 0;
        for (Neuron neuron : layer.getNeurons()) {
            count += neuron.getInputConnections().length;
        }
        return count;
    }    
    
}
