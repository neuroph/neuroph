package org.neuroph.samples.convolution.mnist;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Represents one Image from MNIST dataset
 */
public class MNISTImage {

       
	private int label;
        private byte[] imageData;

	/**
	 * the following constants are defined as per the values described at
	 * http://yann.lecun.com/exdb/mnist/
	 **/

	private static final int MAGIC_OFFSET = 0; // position of the magic number - its allways first in file
	private static final int OFFSET_SIZE = 4; // in bytes ???

	private static final int LABEL_MAGIC = 2049; // magic number for labels?
	private static final int IMAGE_MAGIC = 2051; // magic number for images?

	private static final int NUMBER_ITEMS_OFFSET = 4; // number of images or labels in dataset
	private static final int ITEMS_SIZE = 4; // size of rows number 32-bit integer = 4 bytes


	public static final int ROWS = 28; // number of image rows        
        private static final int NUMBER_OF_ROWS_OFFSET = 8;  // position of the number of rows 
	private static final int ROWS_SIZE = 4; // size of rows number 32-bit integer = 4 bytes

	public static final int COLUMNS = 28; // number of image columns
	private static final int NUMBER_OF_COLUMNS_OFFSET = 12; // position of the number of columns
	private static final int COLUMNS_SIZE = 4; // size of columns number 32-bit integer = 4 bytes

	private static final int IMAGE_OFFSET = 16; // position where image data starts
	private static final int IMAGE_SIZE = ROWS * COLUMNS; // size of the image        
        

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
        
        
	public static List<MNISTImage> loadDigitImages(String labelFileName, String imageFileName) throws IOException {
		List<MNISTImage> images = new ArrayList<>();

		ByteArrayOutputStream labelBuffer = new ByteArrayOutputStream();
		ByteArrayOutputStream imageBuffer = new ByteArrayOutputStream();
                InputStream labelInputStream = new FileInputStream(labelFileName);   
		InputStream imageInputStream = new FileInputStream(imageFileName);                
	/*	InputStream labelInputStream = MNISTDataSet.class.getResourceAsStream(labelFileName);
		InputStream imageInputStream = MNISTDataSet.class.getResourceAsStream(imageFileName);*/

		int read;
		byte[] buffer = new byte[16384];

		while ((read = labelInputStream.read(buffer, 0, buffer.length)) != -1) {
			labelBuffer.write(buffer, 0, read);
		}

		labelBuffer.flush();

		while ((read = imageInputStream.read(buffer, 0, buffer.length)) != -1) {
			imageBuffer.write(buffer, 0, read);
		}

		imageBuffer.flush();

		byte[] labelBytes = labelBuffer.toByteArray();
		byte[] imageBytes = imageBuffer.toByteArray();

		byte[] labelMagic = Arrays.copyOfRange(labelBytes, 0, OFFSET_SIZE);
		byte[] imageMagic = Arrays.copyOfRange(imageBytes, 0, OFFSET_SIZE);

		if (ByteBuffer.wrap(labelMagic).getInt() != LABEL_MAGIC) {
			throw new IOException("Bad magic number in label file!");
		}

		if (ByteBuffer.wrap(imageMagic).getInt() != IMAGE_MAGIC) {
			throw new IOException("Bad magic number in image file!");
		}

		int numberOfLabels = ByteBuffer.wrap(Arrays.copyOfRange(labelBytes, NUMBER_ITEMS_OFFSET, NUMBER_ITEMS_OFFSET + ITEMS_SIZE))
				.getInt();
		int numberOfImages = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, NUMBER_ITEMS_OFFSET, NUMBER_ITEMS_OFFSET + ITEMS_SIZE))
				.getInt();

		if (numberOfImages != numberOfLabels) {
			throw new IOException("The number of labels and images do not match!");
		}

		int numRows = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, NUMBER_OF_ROWS_OFFSET, NUMBER_OF_ROWS_OFFSET + ROWS_SIZE)).getInt();
		int numCols = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, NUMBER_OF_COLUMNS_OFFSET, NUMBER_OF_COLUMNS_OFFSET + COLUMNS_SIZE))
				.getInt();

		if (numRows != ROWS && numCols != COLUMNS) {
			throw new IOException("Bad image. Rows and columns do not equal " + ROWS + "x" + COLUMNS);
		}

		for (int i = 0; i < numberOfLabels; i++) {
			int label = labelBytes[OFFSET_SIZE + ITEMS_SIZE + i];
			byte[] imageData = Arrays
					.copyOfRange(imageBytes, (i * IMAGE_SIZE) + IMAGE_OFFSET, (i * IMAGE_SIZE) + IMAGE_OFFSET + IMAGE_SIZE);

			images.add(new MNISTImage(label, imageData));
		}

		return images;
	}        

}
