package org.neuroph.samples.convolution;

import java.io.IOException;
import java.util.List;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * Provides methods for loading MNIST dataset (training and test set)
 * 
 * TODO: reorganizuj tako da imamo samo jedu metodu loadDataSet kojoj se
 * prosledjuje naziv i koja ucitava i datas et i test set
 * a ne da postoje dve posebne metode za to koje rade potpuno istu stvar
 * 
 * 
 * @author Boris Fulurija
 * @author Zoran Sevarac
 */
public class MNISTDataSet {

    public static final String TRAIN_LABEL_NAME = "train-labels.idx1-ubyte";
    public static final String TRAIN_IMAGE_NAME = "train-images.idx3-ubyte";
    public static final String TEST_LABEL_NAME = "t10k-labels.idx1-ubyte";
    public static final String TEST_IMAGE_NAME = "t10k-images.idx3-ubyte";

    
    public static DataSet createFromFile(String labelPath, String imagePath, int sampleCount) throws IOException {
        //List<MNISTImage> mnistImages = loadMNISTImages(labelPath, imagePath);
        MNISTImageLoader mnistLoader = new MNISTImageLoader("/" + labelPath, "/" + imagePath);
        List<MNISTImage> mnistImages = mnistLoader.loadDigitImages();        
        DataSet dataSet = createDataSet(mnistImages, sampleCount);
        return dataSet;
    }    
    
    
//    private static List<MNISTImage> loadMNISTImages(String labelPath, String imagePath) throws IOException {
////        String labelPath = "/" + TRAIN_LABEL_NAME;
////        String imagePath = "/" + TRAIN_IMAGE_NAME;
//        MNISTImageLoader mnistLoader = new MNISTImageLoader("/" + labelPath, "/" + imagePath);
//        List<MNISTImage> imageList = mnistLoader.loadDigitImages();
//        return imageList;
//    }    
    
    
    
    
    
//    public static DataSet loadTrainSet(int sampleCount) throws IOException {
//        String labelPath = "/" + TRAIN_LABEL_NAME;
//        String imagePath = "/" + TRAIN_IMAGE_NAME;        
//        List<MNISTImage> trainImages = loadMNISTImages(labelPath, imagePath);
//        DataSet trainSet = crateDataSet(trainImages, sampleCount);
//        return trainSet;
//    }

//    private static String createValidPath(String path) {
//        if (path.charAt(path.length() - 1) != '/') {
//            path += "/";
//        }
//        return path;
//    }

//    public static DataSet loadTestSet(int sampleCount) throws IOException {
//        // path = createValidPath(path);
//        List<MNISTImage> testImages = getTestDataSet();
//        DataSet testSet = crateDataSet(testImages, sampleCount);
//        return testSet;
//    }

//    private static List<MNISTImage> getTrainDataSet() throws IOException {
//        String labelPath = "/" + TRAIN_LABEL_NAME;
//        String imagePath = "/" + TRAIN_IMAGE_NAME;
//        MNISTLoader db = new MNISTLoader(labelPath, imagePath);
//        List<MNISTImage> imageList = db.loadDigitImages();
//        return imageList;
//    }
//
//    private static List<MNISTImage> getTestDataSet() throws IOException {
//        String labelPath = "/" + TEST_LABEL_NAME;
//        String imagePath = "/" + TEST_IMAGE_NAME;
//        MNISTLoader db = new MNISTLoader(labelPath, imagePath);
//        List<MNISTImage> imageList = db.loadDigitImages();
//        return imageList;
//    }

    // TODO; remove sampleCount param its ame as  list count
    private static DataSet createDataSet(List<MNISTImage> imageList, int sampleCount) {

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
