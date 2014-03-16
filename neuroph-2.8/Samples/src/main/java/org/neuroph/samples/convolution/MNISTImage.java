package org.neuroph.samples.convolution;


/**
 * Represents one Image from MNIST dataset
 */
public class MNISTImage {

	private int label;
        private byte[] imageData;
	

	public MNISTImage(int label, byte[] imageData) {
		this.imageData = imageData;
		this.label = label;
	}
	
	public int getLabel(){
		return label;
	}
	
	public byte[] getData(){
		return imageData;
	}
	
	public int getSize(){
		return imageData.length;
	}

}
