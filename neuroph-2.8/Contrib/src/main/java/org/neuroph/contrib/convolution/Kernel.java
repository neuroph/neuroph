package org.neuroph.contrib.convolution;

import java.io.Serializable;

public class Kernel implements Serializable {
	
	private static final long serialVersionUID = -3948374914759253222L;
	private int width;
	private int height;

	public Kernel(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int kernelWidth) {
		this.width = kernelWidth;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int area() {
		return width * height;
	}

}
