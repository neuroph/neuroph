package org.neuroph.contrib.convolution;

import java.io.Serializable;



public class Kernel implements Serializable {
	
	private static final long serialVersionUID = -3948374914759253222L;
        
        /**
         * Kernel width
         */
	private int width;
        
        /**
         * Kernel height
         */
	private int height;

        
	public Kernel(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

        /**
         * Returns width of this kernel
         * @return width of this kernel
         */
	public int getWidth() {
		return width;
	}

	public void setWidth(int kernelWidth) {
		this.width = kernelWidth;
	}

        /**
         * Returns height of this kernel
         * @return height of this kernel
         */
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getArea() {
		return width * height;
	}

}
