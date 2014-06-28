package org.neuroph.netbeans.tcr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JTextArea;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.ocr.OcrHelper;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.VectorParser;
import org.openide.util.Exceptions;

/**
 * Create training set and neural network, extract charsArray from image, manage text recognition
 * @author Vlada-laptop
 */
public class TextRecognitionManager {

    private Dimension charDimension;
    private BufferedImage image;
    private List<String> imageLabels;
    
    private static TextRecognitionManager instance = null;    
    private NeuralNetwork neuralNet;
    
    private TextRecognitionManager() {

    }

    public static TextRecognitionManager getInstance() {
        if(instance==null){
            instance = new TextRecognitionManager();
        }
        return instance;
    }

    /**
     * Create training set 
     * 
     * @param imageWithChars
     * @param chars
     * @param scaleToDim
     * @param trainingSetName 
     */
    public void createTrainingSet (BufferedImage imageWithChars, String chars, Dimension scaleToDim, String trainingSetName){
//
//        // convert chars from string to list 
//        List<String> charList = Arrays.asList(chars.split(" "));        // izgleda da ovo zeza...
//        // extract individual char images which will be used to create training set
//        HashMap <String, BufferedImage> charImageMap = charImageExtractor.extractCharImagesToLearn(imageWithChars, charList, scaleToDim);
//        
        this.charDimension = scaleToDim;
//        
       // prepare image labels (we need them to label output neurons...)
//        imageLabels = new ArrayList<String>();   
//        for (String imgName : charImageMap.keySet()) {
//            StringTokenizer st = new StringTokenizer(imgName, "._");
//            String imgLabel = st.nextToken();
//            if (!this.imageLabels.contains(imgLabel)) { // check for duplicates ...
//                this.imageLabels.add(imgLabel);
//            }
//        }
//        Collections.sort(this.imageLabels);
//        
//        // get RGB image data - map chars and their their rgb data
//        Map<String, FractionRgbData> imageRgbData = ImageUtilities.getFractionRgbDataForImages(charImageMap);
//                       
//        // also put junk all black and white image in training set (for black n whit emode)
//        BufferedImage allWhite = new BufferedImage(charDimension.getWidth(), charDimension.getHeight(), BufferedImage.TYPE_INT_RGB);
//        Graphics g = allWhite.getGraphics();
//        g.setColor(Color.WHITE);
//        g.fillRect(0, 0, allWhite.getWidth(), allWhite.getHeight());
//        imageRgbData.put("allWhite", new FractionRgbData(allWhite));
//        
////        BufferedImage allBlack = new BufferedImage(charDimension.getWidth(), charDimension.getHeight(), BufferedImage.TYPE_INT_RGB);
////        g = allBlack.getGraphics();
////        g.setColor(Color.BLACK);
////        g.fillRect(0, 0, allBlack.getWidth(), allBlack.getHeight());
////        imageRgbData.put("allBlack", new FractionRgbData(allBlack));        
//                      
//        // put junk images (all red, blue, green) to avoid errors (used for full color mode)
////        BufferedImage allRed = new BufferedImage(charDimension.getWidth(), charDimension.getHeight(), BufferedImage.TYPE_INT_RGB);
////        Graphics g = allRed.getGraphics();
////        g.setColor(Color.RED);
////        g.fillRect(0, 0, allRed.getWidth(), allRed.getHeight());
////        imageRgbData.put("allRed", new FractionRgbData(allRed));        
////        
////        BufferedImage allBlue = new BufferedImage(charDimension.getWidth(), charDimension.getHeight(), BufferedImage.TYPE_INT_RGB);
////        g = allBlue.getGraphics(); 
////        g.setColor(Color.BLUE);
////        g.fillRect(0, 0, allBlue.getWidth(), allBlue.getHeight());
////        imageRgbData.put("allBlue", new FractionRgbData(allBlue));        
////        
////        BufferedImage allGreen = new BufferedImage(charDimension.getWidth(), charDimension.getHeight(), BufferedImage.TYPE_INT_RGB);
////        g = allGreen.getGraphics(); 
////        g.setColor(Color.GREEN);
////        g.fillRect(0, 0, allGreen.getWidth(), allGreen.getHeight());
////        imageRgbData.put("allGreen", new FractionRgbData(allGreen));                
//                
//        // create training set using image rgb data
//        DataSet activeTrainingSet = ImageRecognitionHelper.createBlackAndWhiteTrainingSet(this.imageLabels, imageRgbData);
//         //DataSet activeTrainingSet = ImageRecognitionHelper.createTrainingSet(this.imageLabels, imageRgbData);
//        activeTrainingSet.setLabel(trainingSetName);        
//        //printDataSet(activeTrainingSet, scaleToDim); //(used to print out created dataset for debugging purporse)
//        
        imageLabels = new ArrayList<String>();   
        DataSet dataSet = OcrHelper.createTrainingSet(trainingSetName, imageWithChars, chars, scaleToDim, imageLabels);
        
        // write data set to file
        NeurophProjectFilesFactory.getDefault().createTrainingSetFile(dataSet);
    }
    
    private void printDataSet(DataSet dset, Dimension dim) {
        int r = 0;
        for( DataSetRow row : dset.getRows()) {
            System.out.println("DataSetRow ["+r+"]   -------------------------------------------------");
            double[] input = row.getInput();
            writeToImage(input, dim);
            System.out.println("Input:");
                for(int i=0; i<input.length; i++) {
                    if (i % dim.getWidth() == 0) {
                        System.out.println();
                    }
                    System.out.print(" "+((int)input[i]));
                    
                }
            
            double[] output = row.getDesiredOutput();                        
            System.out.println();
            System.out.println("Output: "+Arrays.toString(output));                        
            r++;
         }        
    }


    // used for debuging to write specified black n white input vector to  image file
    int li=0;
    private void writeToImage(double[] input, Dimension dim) {
        BufferedImage bi = new BufferedImage(dim.getWidth(), dim.getHeight(), BufferedImage.TYPE_INT_RGB);
        bi.getGraphics().setColor(Color.WHITE);
        bi.getGraphics().drawRect(0, 0, dim.getWidth(), dim.getHeight());

        for(int y=0; y<dim.getHeight(); y++) {
            for(int x=0; x<dim.getWidth(); x++) {
                int pix = (int)input[y*dim.getWidth() + x];
                if (pix==1) {
                    bi.setRGB(x, y, -16777216 ); // set black
                } 
                    else {
                    bi.setRGB(x, y, -1);  // set white
                }                
            }
        }

        try {
            ImageIO.write(bi, "PNG", new File("ds_" + li + ".png"));
            li++;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    public void createNeuralNetwork(String neuralNetworkName,String transferFunction,Dimension resolution,String hiddenLayersStr){
       ArrayList<Integer> hiddenLayersNeuronsCount = VectorParser.parseInteger(hiddenLayersStr.trim());
       neuralNet = OcrHelper.createNewNeuralNetwork(neuralNetworkName, resolution, ColorMode.BLACK_AND_WHITE, imageLabels, hiddenLayersNeuronsCount, TransferFunctionType.valueOf(transferFunction.toUpperCase()));
       // neuralNet = OcrHelper.createNewNeuralNetwork(neuralNetworkName, resolution, ColorMode.FULL_COLOR, imageLabels, hiddenLayersNeuronsCount, TransferFunctionType.valueOf(transferFunction.toUpperCase()));
        NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(neuralNet);                   
    }
    
    public void setNeuralNetwork(NeuralNetwork neuralNet) {
        this.neuralNet = neuralNet;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImageWithText(){
        return image;
    }

    public void recognizeText(JTextArea area){
//        charImageExtractor.setImageWithChars(image);
//        List<BufferedImage> listOfImages = charImageExtractor.extractCharImagesToRecognize();
        List<Character> characters = OcrHelper.recognizeText(neuralNet, image, charDimension);
        for (Character character : characters){
            area.append(character+" ");
        }
    }
    
    
//    public List<Character> recognize (NeuralNetwork nnet, List<BufferedImage> images, Dimension charDimension){
//         OcrPlugin  ocrPlugin = (OcrPlugin) nnet.getPlugin(OcrPlugin.class);
//        
//        List<Character> letters = new ArrayList<Character>();
//        int i = 0;
//        for (BufferedImage img : images) {
//            Character letter = ocrPlugin.recognizeCharacter(new ImageJ2SE(img), charDimension);
//            letters.add(letter);
//        }
//        return letters;
//    }    
    
}
