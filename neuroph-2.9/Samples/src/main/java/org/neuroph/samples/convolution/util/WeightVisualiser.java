package org.neuroph.samples.convolution.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.nnet.comp.Kernel;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.BiasNeuron;

public class WeightVisualiser {

	private static final int RATIO = 20;

	private List<List<Double>> featureDetector;
	private Kernel kernel;

	public WeightVisualiser(Layer2D map, Kernel kernel) {
		this.kernel = kernel;
		this.featureDetector = new ArrayList<>();
		initWeights(map);
	}

	private void initWeights(Layer2D map) {
		List<Double> weights = new ArrayList<>();
		Neuron neuron = map.getNeuronAt(0);
		int counter = 0;
		for (Connection conn : neuron.getInputConnections()) {
			if (!(conn.getFromNeuron() instanceof BiasNeuron)) {
				if (counter < kernel.getArea() ) {
					weights.add(conn.getWeight().getValue());
					counter++;
				} else {
					featureDetector.add(weights);
					weights = new ArrayList<>();
					weights.add(conn.getWeight().getValue());
					counter = 1;
				}
			}
		}
		featureDetector.add(weights);

	}

	public void displayWeights() {
		for (List<Double> currentKernel : featureDetector) {
			displayWeight(currentKernel);
		}
	}

	private void displayWeight(List<Double> currentKernel) {

		JFrame frame = new JFrame("Weight Visualiser: ");
		frame.setSize(400, 400);

		JLabel label = new JLabel();
		Dimension d = new Dimension(kernel.getWidth() * RATIO, kernel.getHeight() * RATIO);
		label.setSize(d);
		label.setPreferredSize(d);

		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

		BufferedImage image = new BufferedImage(kernel.getWidth(), kernel.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		int[] rgb = convertWeightToRGB(currentKernel);
		image.setRGB(0, 0, kernel.getWidth(), kernel.getHeight(), rgb, 0, kernel.getWidth());
		label.setIcon(new ImageIcon(image.getScaledInstance(kernel.getWidth() * RATIO, kernel.getHeight() * RATIO, Image.SCALE_SMOOTH)));

	}

	private int[] convertWeightToRGB(List<Double> weights) {
		normalizeWeights(weights);
		int[] data = new int[kernel.getWidth() * kernel.getHeight()];
		int i = 0;
		for (Double weight : weights) {
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
