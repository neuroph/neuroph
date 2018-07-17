/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.imgrec.samples;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageFactory;
import org.neuroph.imgrec.image.ImageJ2SE;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Maneesh
 */
public class ImageHBodyExtract {
    final static int IMG_TRAIN_WIDTH=128;
    final static int IMG_TRAIN_HEIGHT=128;
    final static int numOfPixelInARow=32;
    /**
     * 1. Read all output files
     * 2. Find respective input files,
     * 3. Create data set
     * 4. Train
     * 5. Test
     * @param outputDir
     * @param inputDir
     * @param outputFile
     */
    
    public static void train(String outputDir,String inputDir,String outputFile){
        try {

            Map<FractionRgbData,FractionRgbData> map = ImageRecognitionHelper.getFractionRgbDataForDirectory (new File(outputDir),new File(inputDir), new Dimension(IMG_TRAIN_WIDTH,IMG_TRAIN_HEIGHT));
            DataSet dataSet = ImageRecognitionHelper.createRGBandBWTrainingSet(map);
            
            // create neural network
        List <Integer> hiddenLayers = new ArrayList<>();
        hiddenLayers.add(5);
        NeuralNetwork nnet = ImageRecognitionHelper.createNewNeuralNetwork("someNetworkName", new Dimension(IMG_TRAIN_WIDTH,IMG_TRAIN_HEIGHT), ColorMode.COLOR_RGB,  hiddenLayers, TransferFunctionType.SIGMOID);

        // set learning rule parameters
        MomentumBackpropagation mb = (MomentumBackpropagation)nnet.getLearningRule();
        mb.setLearningRate(0.5);
        mb.setMaxError(0.001);
        mb.setMomentum(.6);
        mb.setMaxIterations(100);
      
       
        //add learning listener in order to print out training info
        mb.addListener(new LearningEventListener() {
            @Override
            public void handleLearningEvent(LearningEvent event) {
                BackPropagation bp = (BackPropagation) event.getSource();
                if (event.getEventType().equals(LearningEvent.Type.LEARNING_STOPPED)) {
                    System.out.println();
                    System.out.println("Training completed in " + bp.getCurrentIteration() + " iterations");
                    System.out.println("With total error " + bp.getTotalNetworkError() + '\n');
                } else {
                    System.out.println("Iteration: " + bp.getCurrentIteration() + " | Network error: " + bp.getTotalNetworkError());
                }
            }
});
        
        // traiin network
        System.out.println("NNet start learning..."+new Date());
        nnet.learn(dataSet);
        System.out.println("NNet learned"+new Date()); 
        nnet.save(outputFile);
        
        } catch (IOException ex) {
            Logger.getLogger(ImageHBodyExtract.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void test(String inputFile, String nnetFile,String outputFile){
         NeuralNetwork nnet = NeuralNetwork.createFromFile(nnetFile);
         
         File imgFile = new File(inputFile);
         Image img = ImageFactory.getImage(imgFile);
         //img = ImageSampler.downSampleImage(new Dimension(20,20), img);
        int row=img.getHeight()/IMG_TRAIN_HEIGHT;
         int col=img.getWidth()/IMG_TRAIN_WIDTH;
         

         
        try {
            //Tessilate images
            BufferedImage[] images = ImageUtilities.chunks(inputFile, row, col);
            BufferedImage[] imagesResult=new BufferedImage[row*col];
            int i=0;
            for(BufferedImage bimg:images)
            {
                double[] input = new FractionRgbData(bimg).getFlattenedRgbValues();
                nnet.setInput(input);
                nnet.calculate();
                nnet.getOutput();
                ImageJ2SE imgOutput = FractionRgbData.defaltRGBValues(nnet.getOutput(), IMG_TRAIN_WIDTH,IMG_TRAIN_HEIGHT, img.getType());
               
                imagesResult[i++]=imgOutput.getBufferedImage();
            }
            
            //Combine them and save
            
            ImageIO.write(ImageUtilities.stitch(imagesResult, row, col), "jpg", new File(outputFile));

        } catch (IOException ex) {
            Logger.getLogger(ImageHBodyExtract.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void trainV2(String outputDir,String inputDir,String outputFile){
        try {

            Map<FractionRgbData,FractionRgbData> map = ImageRecognitionHelper.getFractionGreyScaleDataForDirectory (new File(outputDir),new File(inputDir), new Dimension(IMG_TRAIN_WIDTH,IMG_TRAIN_HEIGHT));
            DataSet dataSet = ImageRecognitionHelper.createRGBandBWTrainingSetV2(map,numOfPixelInARow);
            
            // create neural network
        List <Integer> hiddenLayers = new ArrayList<>();
        hiddenLayers.add(1);
        NeuralNetwork nnet = ImageRecognitionHelper.createNewNeuralNetwork("someNetworkName", new Dimension(numOfPixelInARow,1), ColorMode.COLOR_RGB,  hiddenLayers, TransferFunctionType.SIGMOID);

        // set learning rule parameters
        MomentumBackpropagation mb = (MomentumBackpropagation)nnet.getLearningRule();
        mb.setLearningRate(0.5);
        mb.setMaxError(0.001);
        mb.setMomentum(.6);
        mb.setMaxIterations(100);
      
       
        //add learning listener in order to print out training info
        mb.addListener(new LearningEventListener() {
            @Override
            public void handleLearningEvent(LearningEvent event) {
                BackPropagation bp = (BackPropagation) event.getSource();
                if (event.getEventType().equals(LearningEvent.Type.LEARNING_STOPPED)) {
                    System.out.println();
                    System.out.println("Training completed in " + bp.getCurrentIteration() + " iterations");
                    System.out.println("With total error " + bp.getTotalNetworkError() + '\n');
                } else {
                    System.out.println("Iteration: " + bp.getCurrentIteration() + " | Network error: " + bp.getTotalNetworkError());
                }
            }
});
        
        // traiin network
        System.out.println("NNet start learning..."+new Date());
        nnet.learn(dataSet);
        System.out.println("NNet learned"+new Date()); 
        nnet.save(outputFile);
        
        } catch (IOException ex) {
            Logger.getLogger(ImageHBodyExtract.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void testV2(String inputFile, String nnetFile,String outputFile){
         NeuralNetwork nnet = NeuralNetwork.createFromFile(nnetFile);
         
         System.out.println("getInputsCount>>"+nnet.getInputsCount());
         
         File imgFile = new File(inputFile);
         Image img = ImageFactory.getImage(imgFile);
         //img = ImageSampler.downSampleImage(new Dimension(20,20), img);
         
          int row=img.getHeight()/1;
         int col=img.getWidth()/numOfPixelInARow;
         
        try {
            //Tessilate images
            BufferedImage[] images = ImageUtilities.chunks(inputFile, row, col);
            BufferedImage[] imagesResult=new BufferedImage[row*col];
            int i=0;
            for(BufferedImage bimg:images)
            {
                double[] input = new FractionRgbData(bimg).getFlattenedRgbValues();
                System.out.println("input>>"+input.length);
                nnet.setInput(input);
                nnet.calculate();
                nnet.getOutput();
ImageJ2SE imgOutput = FractionRgbData.defaltRGBValues(nnet.getOutput(), numOfPixelInARow,1, img.getType());
                
                imagesResult[i++]=imgOutput.getBufferedImage();
            }
            
            //Combine them and save
            
            ImageIO.write(ImageUtilities.stitch(imagesResult, row, col), "jpg", new File(outputFile));

        } catch (IOException ex) {
            Logger.getLogger(ImageHBodyExtract.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public static void main(String... args){
                String nnetVersion="100";
                String basedir="G:/bkp/$AVG/baseDir/Imports/Yugkatha/27Queens/Processing/";
                //train(basedir+"output",basedir+"input",basedir+"output"+nnetVersion+".nnet");
//                             test(basedir+"input/f999cu8wCZyGFnT5UJ2CLpfrHKGUhCRdScX5MRMgvhGHtW5dSS0CDCogGm2e8wcs1+mQY.jpg",
//                        basedir+"output"+nnetVersion+".nnet",
//                        basedir+"f999cu8wCZyGFnT5UJ2CLpfrHKGUhCRdScX5MRMgvhGHtW5dSS0CDCogGm2e8wcs1+mQY.jpg");
//   
                             
//                train(basedir+"outputbw",basedir+"input",basedir+"outputbw"+nnetVersion+".nnet");
//                
//                
//                test(basedir+"input/f999cu8wCZyGFnT5UJ2CLpfrHKGUhCRdScX5MRMgvhGHtW5dSS0CDCogGm2e8wcs1+mQY.jpg",
//                        basedir+"outputbw"+nnetVersion+".nnet",
//                        basedir+"outputbw/_f999cu8wCZyGFnT5UJ2CLpfrHKGUhCRdScX5MRMgvhGHtW5dSS0CDCogGm2e8wcs1+mQY.jpg");
                
                
                
                //=================V2=========================
               trainV2(basedir+"outputbw",basedir+"input",basedir+"outputbw"+nnetVersion+".nnet");
                
                
                testV2(basedir+"input/f999cu8wCZyGFnT5UJ2CLpfrHKGUhCRdScX5MRMgvhGHtW5dSS0CDCogGm2e8wcs1+mQY.jpg",
                        basedir+"outputbw"+nnetVersion+".nnet",
                        basedir+"outputbw/_f999cu8wCZyGFnT5UJ2CLpfrHKGUhCRdScX5MRMgvhGHtW5dSS0CDCogGm2e8wcs1+mQY.jpg");
                
                
    }
}
