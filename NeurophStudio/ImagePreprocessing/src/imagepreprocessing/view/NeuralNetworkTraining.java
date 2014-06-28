/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagepreprocessing.view;

import imagepreprocessing.filter.ImageFilterChain;
import imagepreprocessing.filter.impl.MedianFilter;
import imagepreprocessing.filter.impl.OtsuBinarizeFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.ImageRecognitionPlugin;
import org.neuroph.imgrec.ImageSizeMismatchException;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *                                                                                  //Should we make this singleton??
 *
 * @author Aleksandar
 */
public class NeuralNetworkTraining {

    private static NeuralNetwork nnet;
    private static List<String> imageLabels;
    private static List<String> testLabels;                                         //zasto ne moze non static varibla iz static metode??
    private static DataSet dataSet;

    private static void createNeuralNetwork() {
        imageLabels = new ArrayList();
        imageLabels.add("Cancer");                                                    //Sta da radimo sa ovim??
        imageLabels.add("devojka");
        List<Integer> hiddenLayers = new ArrayList<Integer>();
        hiddenLayers.add(12);

        nnet = ImageRecognitionHelper.createNewNeuralNetwork("network", new Dimension(20, 20), ColorMode.FULL_COLOR, imageLabels, hiddenLayers, TransferFunctionType.SIGMOID);

        System.out.println("NNet created!");
    }

    /**
     * Trains neural network with data in dirName directory.
     *
     * @param dirName name of a directory where training data resides.
     */
    public static void trainNeuralNetwork(String dirName) {
        createNeuralNetwork();

        testLabels = new ArrayList<String>();
        ImageFilterChain fc = new ImageFilterChain();
        fc.addFilter(new MedianFilter());
        fc.addFilter(new OtsuBinarizeFilter());

        File dir = new File(dirName);
        File dir3 = new File("processed");
        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles()) {
                BufferedImage img = null;

                try {
                    img = ImageIO.read(f);
                    img = fc.processImage(img);

                    File actualFile = new File(dir3, f.getName());
                    ImageIO.write(img, "png", actualFile);
                    testLabels.add("processed/" + f.getName());

                } catch (final IOException e) {
                    throw new RuntimeException("Training filed");
                }
            }
        }
        
        createDataSet();
        
    }

    /**
     *
     * @param testLabels
     * @return map containing image name as key and FractionRgbData object
     * representing that image as value.
     */
    private static Map<String, FractionRgbData> createMap(List<String> testLabels) {

        Map<String, FractionRgbData> map = new HashMap<String, FractionRgbData>();
        try {
            for (String string : testLabels) {
                BufferedImage img = ImageIO.read(new File(string));
                FractionRgbData frgb = new FractionRgbData(img);
                map.put(string.split("/")[1], frgb);
            }

        } catch (IOException ex) {
            Logger.getLogger(NeuralNetworkTraining.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    static String testNeuralNetwork(File file) {
        String result = null;
        ImageRecognitionPlugin ir = (ImageRecognitionPlugin) nnet.getPlugin(ImageRecognitionPlugin.class);
        try {
            HashMap<String, Double> output = ir.recognizeImage(new File("resized/"+file.getName()));
            result = output.toString();
        } catch (IOException ex) {
            Logger.getLogger(NeuralNetworkTraining.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ImageSizeMismatchException ex) {
            Logger.getLogger(NeuralNetworkTraining.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private static void createDataSet() {
        Map<String,FractionRgbData> map = createMap(testLabels);        
        DataSet ds = ImageRecognitionHelper.createTrainingSet(imageLabels, map);
        MomentumBackpropagation m = new MomentumBackpropagation();
        m.setLearningRate(0.2);
        m.setMaxError(0.1);
        m.setMomentum(0.7);
        nnet.learn(ds,m);                                                           //Sta da radim sa ovim, gde da ide??
    }
}
