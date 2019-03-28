package org.neuroph.samples.convolution.mnist;

import java.io.IOException;
import java.util.List;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * Provides methods for loading MNIST dataset (training and test set)
 * <p/>
 * TODO: reorganizuj tako da imamo samo jedu metodu loadDataSet kojoj se
 * prosledjuje naziv i koja ucitava i datas et i test set
 * a ne da postoje dve posebne metode za to koje rade potpuno istu stvar
 *
 * @author Boris Fulurija
 * @author Zoran Sevarac
 */
public class MNISTDataSet {

    public static final String TRAIN_LABEL_NAME = "data_sets/train-labels.idx1-ubyte";
    public static final String TRAIN_IMAGE_NAME = "data_sets/train-images.idx3-ubyte";
    public static final String TEST_LABEL_NAME = "data_sets/t10k-labels.idx1-ubyte";
    public static final String TEST_IMAGE_NAME = "data_sets/t10k-images.idx3-ubyte";


    public static DataSet createFromFile(String labelPath, String imagePath, int sampleCount) throws IOException {
        List<MNISTImage> mnistImages = MNISTImage.loadDigitImages(labelPath, imagePath);
        DataSet dataSet = createDataSet(mnistImages, sampleCount);
        return dataSet;
    }

    // TODO; remove sampleCount param its ame as  list count
    private static DataSet createDataSet(List<MNISTImage> imageList, int sampleCount) {

        int pixelCount = imageList.get(0).getSize();
        int totalSize = 1024;
        DataSet dataSet = new DataSet(totalSize, 10);

        for (int i = 0; i < sampleCount; i++) {
            MNISTImage dImage = imageList.get(i);
            double[] input = new double[totalSize];
            double[] output = new double[10];
            for (int j = 0; j < 10; j++) {
                output[j] = 0;
            }

            for (int j = 0; j < totalSize; j++) {
                input[j] = 0;
            }

            output[dImage.getLabel()] = 1;
            byte[] imageData = dImage.getData();
            int k = 66;
            for (int j = 0; j < pixelCount; j++) {
                if ((imageData[j] & 0xff) > 0)
                    input[k++] = 255;
                else
                    k++;
                if (j % 28 == 27)
                    k += 4;
            }
            DataSetRow row = new DataSetRow(input, output);
            dataSet.addRow(row);
        }
        dataSet.setColumnName(1024, "0");
        dataSet.setColumnName(1025, "1");
        dataSet.setColumnName(1026, "2");
        dataSet.setColumnName(1027, "3");
        dataSet.setColumnName(1028, "4");
        dataSet.setColumnName(1029, "5");
        dataSet.setColumnName(1030, "6");
        dataSet.setColumnName(1031, "7");
        dataSet.setColumnName(1032, "8");
        dataSet.setColumnName(1033, "9");
        
        return dataSet;
    }
}
