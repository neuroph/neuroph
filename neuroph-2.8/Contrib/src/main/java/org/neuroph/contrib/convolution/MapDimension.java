package org.neuroph.contrib.convolution;

import java.io.Serializable;

public class MapDimension implements Serializable {

	private static final long serialVersionUID = -4491706467345191107L;
	private int width;
	private int height;

	public MapDimension(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		String dimension = "Width = " + width + "; Height = " + height;
		return dimension;
	}

}
