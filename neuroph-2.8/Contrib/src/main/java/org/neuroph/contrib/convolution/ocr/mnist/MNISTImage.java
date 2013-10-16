package org.neuroph.contrib.convolution.ocr.mnist;

public class MNISTImage {

	private byte[] imageData;
	private int label;

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
