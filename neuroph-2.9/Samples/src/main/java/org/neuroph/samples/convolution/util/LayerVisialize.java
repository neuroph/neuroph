package org.neuroph.samples.convolution.util;

import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.comp.layer.Layer2D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LayerVisialize {
    private static final int RATIO = 20;

    private List<List<Double>> layerMaps;
    private FeatureMapsLayer featureMapsLayer;
    private Layer2D.Dimensions mapDimensions;

    public LayerVisialize(FeatureMapsLayer featureMapsLayer) {

        this.layerMaps = new ArrayList<>();
        this.featureMapsLayer = featureMapsLayer;
        this.mapDimensions = featureMapsLayer.getMapDimensions();
        initWeights();
    }

    private void initWeights() {

        for (Layer2D featureMap : featureMapsLayer.getFeatureMaps()) {
            List<Double> map = new ArrayList<>();
            for (Neuron neuron : featureMap.getNeurons()) {
                map.add(neuron.getOutput());
            }
            layerMaps.add(map);
        }
    }

    public void displayWeights() {
        for (List<Double> currentKernel : layerMaps) {
            displayWeight(currentKernel);
        }
    }

    private void displayWeight(List<Double> currentKernel) {

        JFrame frame = new JFrame("Weight Visualiser: ");
        frame.setSize(400, 400);

        JLabel label = new JLabel();
        Dimension d = new Dimension(mapDimensions.getWidth() * RATIO, mapDimensions.getHeight() * RATIO);
        label.setSize(d);
        label.setPreferredSize(d);

        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        BufferedImage image = new BufferedImage(mapDimensions.getWidth(), mapDimensions.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        int[] rgb = convertWeightToRGB(currentKernel);
        image.setRGB(0, 0, mapDimensions.getWidth(), mapDimensions.getHeight(), rgb, 0, mapDimensions.getWidth());
        label.setIcon(new ImageIcon(image.getScaledInstance(mapDimensions.getWidth() * RATIO, mapDimensions.getHeight() * RATIO, Image.SCALE_SMOOTH)));

    }

    private int[] convertWeightToRGB(List<Double> pixels) {
        normalizeWeights(pixels);
        int[] data = new int[mapDimensions.getWidth() * mapDimensions.getHeight()];
        int i = 0;
        for (Double weight : pixels) {
            int val = (int) (weight * 255);
            data[i++] = new Color(val, val, val).getRGB();
        }
        return data;
    }

    private void normalizeWeights(List<Double> weights) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Double weight : weights) {
            min = Math.min(min, weight);
            max = Math.max(max, weight);
        }

        for (int i = 0; i < weights.size(); i++) {
            double value = (weights.get(i) - min) / (max - min);
            weights.set(i, value);
        }
    }

}
