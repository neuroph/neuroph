package org.neuroph.contrib.convolution.ocr.mnist;

import java.io.IOException;
import java.util.List;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class MNISTLoader {

	private static String TRAIN_LABEL_NAME = "train-labels.idx1-ubyte";
	private static String TRAIN_IMAGE_NAME = "train-images.idx3-ubyte";
	private static String TEST_LABEL_NAME = "t10k-labels.idx1-ubyte";
	private static String TEST_IMAGE_NAME = "t10k-images.idx3-ubyte";

	public static DataSet loadTrainSet(String path, int sampleCount) throws IOException {
		path = createValidPath(path);
		List<MNISTImage> trainImages = getTrainDataSet(path);
		DataSet trainSet = crateDataSet(trainImages, sampleCount);
		return trainSet;
	}

	private static String createValidPath(String path) {
		if (path.charAt(path.length() - 1) != '/') {
			path += "/";
		}
		return path;
	}

	public static DataSet loadTestSet(String path, int sampleCount) throws IOException {
		path = createValidPath(path);
		List<MNISTImage> testImages = getTestDataSet(path);
		DataSet testSet = crateDataSet(testImages, sampleCount);
		return testSet;
	}

	private static List<MNISTImage> getTrainDataSet(String path) throws IOException {
		String labelPath = path + TRAIN_LABEL_NAME;
		String imagePath = path + TRAIN_IMAGE_NAME;
		MNISTLoadingService db = new MNISTLoadingService(labelPath, imagePath);
		List<MNISTImage> imageList = db.loadDigitImages();
		return imageList;
	}

	private static List<MNISTImage> getTestDataSet(String path) throws IOException {
		String labelPath = path + TEST_LABEL_NAME;
		String imagePath = path + TEST_IMAGE_NAME;
		MNISTLoadingService db = new MNISTLoadingService(labelPath, imagePath);
		List<MNISTImage> imageList = db.loadDigitImages();
		return imageList;
	}

	private static DataSet crateDataSet(List<MNISTImage> imageList, int sampleCount) {

		int pixelCount = imageList.get(0).getSize();
		DataSet dataSet = new DataSet(pixelCount, 10);
		for (int i = 0; i < sampleCount; i++) {
			MNISTImage dImage = imageList.get(i);
			double[] input = new double[pixelCount];
			double[] output = new double[10];
			output[dImage.getLabel()] = 1;
			byte[] imageData = dImage.getData();
			for (int j = 0; j < pixelCount; j++) {
				input[j] = (imageData[j] & 0xff) / 255.0;
			}
			DataSetRow row = new DataSetRow(input, output);
			dataSet.addRow(row);
		}
		return dataSet;
	}

}
