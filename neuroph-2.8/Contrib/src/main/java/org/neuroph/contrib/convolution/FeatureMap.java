package org.neuroph.contrib.convolution;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;

public class FeatureMap extends Layer {

	private static final long serialVersionUID = 8498669699995172395L;

	private MapDimension dimension;

	public FeatureMap(MapDimension dimension) {
		this.dimension = dimension;
	}

	public int getWidth() {
		return dimension.getWidth();
	}

	public int getHeight() {
		return dimension.getHeight();
	}

	public Neuron getNeuronAt(int x, int y) {
//		 System.out.println("//************");
//		 System.out.println(x);
//		 System.out.println(y);
//		 System.out.println(dimension.getWidth());
		return getNeuronAt(x + y * (dimension.getWidth()));
	}

}
