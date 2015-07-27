package org.neuroph.contrib.licenceplaterecognition;

import static java.awt.Color.WHITE;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.sourceforge.javaocr.ocrPlugins.CharacterExtractor;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.imgrec.image.ImageJ2SE;
import org.neuroph.ocr.OcrPlugin;


/**
 * Problemi: 1. Image i ImageJ2SE. BufferedImage 2. Problem ako su slova blizu
 * ili ako je pozadina muljava CharExtractor ne radi kako treba resenje:
 * omoguciti setovanje boje pozadine i slova CharExtractor -u i dozvoliti
 * izvesnu toleranciju odnosno odstupanje od tih vrednosti, koje se takodje moze
 * setovati Napravljeno resenje: slika se preprocesira sa graysacla i binarize i
 * to onda radi; testirati threshold
 *
 * 3. Dokumentovati OCR i image recognition API 4. Napisati neki kratak
 * tutorijal uz ovaj demo primer
 *
 * Sledeci korak: resiti crticu i sve znake manje od slova po visini
 *
 * @author zoran
 */
public class OcrDemo {

   
    
    /**
     * Image file with text to recognize
     */
    private String textImageFile = "data/tablica.jpg";
    /**
     * Image with all the font letters
     */
    private String datasetImageFile = "data/svaslova.jpg";
    /**
     * Trained neural network file created with OCR wizard from Neuroph Studio
     */
    private String neuralNetworkFile = "data/mrezica.nnet";
    /*
     * Output directory for dataset (individual letters)
     */
    private String datasetOutputFile = "data/dataset";
    /**
     * Location for storing extracted character images
     */
    private String charOutputFile = "data";
    /**
     * Trained neural network
     */
    
    private NeuralNetwork nnet; 
    
    /**
     * Image with licence plate to recognize
     */
    private BufferedImage image;
    
    /**
     * String to store recognized characters;
     */
    private String recognizedCharacters="";

    
     public OcrDemo() {
    }

    public OcrDemo(BufferedImage licencePlateImage,NeuralNetwork neuralNetwork ) {
        image=licencePlateImage;
        nnet=neuralNetwork;
        
    }

    public String getRecognizedCharacters() {
        return recognizedCharacters;
    }
    
    /**
     * Crop the part of an image with a white rectangle
     *
     * @return A cropped image File
     */
    public File crop(BufferedImage image) {
        // this will be coordinates of the upper left white pixel
        int upperLeftCornerx = Integer.MAX_VALUE;
        int upperLeftCornery = Integer.MAX_VALUE;
        //this will be coordinates of the lower right white pixel
        int lowerRightCornerx = Integer.MIN_VALUE;
        int lowerRightCornery = Integer.MIN_VALUE;
        //find the minimum and maximum white pixel coordinates
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (image.getRGB(i, j) == WHITE.getRGB() && (i < upperLeftCornerx && j < upperLeftCornery) 
                        || (i <= upperLeftCornerx && j < upperLeftCornery)
                         || (i < upperLeftCornerx && j <= upperLeftCornery)) {
                    upperLeftCornerx = i;
                    upperLeftCornery = j;
                }
                if (image.getRGB(i, j) == WHITE.getRGB() && ((i > lowerRightCornerx && j >= lowerRightCornery)
                        || (i >= lowerRightCornerx && j > lowerRightCornery)
                        || (i > lowerRightCornerx && j >= lowerRightCornery))) {
                    lowerRightCornerx = i;
                    lowerRightCornery = j;
                }
            }
        }
        //crop the image to the white rectangle size
        BufferedImage croppedImage = image.getSubimage(upperLeftCornerx, upperLeftCornery, lowerRightCornerx - upperLeftCornerx, lowerRightCornery - upperLeftCornery);
       //make a file from that cropped image
        File cropFile = new File("croppedimage.png");
        try {
            ImageIO.write(croppedImage, "png", cropFile);
        } catch (IOException ex) {
            Logger.getLogger(OcrDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cropFile;
    }

    public void run() {
        try {

            // load image with text to recognize
            if (image==null){
             image = ImageIO.read(new File(textImageFile));
            }
            //binarize the input image
            image = BinaryOps.binary(textImageFile);
   
            //dataset creation 
            /**
             * CharacterExtractor ce1 = new CharacterExtractor(); File
             * inputImage1 = new File(datasetImageFile); File outputDirectory1 =
             * new File (datasetOutputFile); ce1.slice(inputImage1,
             * outputDirectory1, 60, 60);
             */
            // crop the white rectange from the image
            File cropFile = crop(image);
            
            // extract individual characters from text image
            CharacterExtractor ce = new CharacterExtractor();
            
           //make the output file
            File outputDirectory = new File(charOutputFile);
            //slice the cropped file to individual character with the width and height of 60px
            ce.slice(cropFile, outputDirectory, 60, 60);
            
            //make a list of character images and add the images form char files
            List<BufferedImage> lista = new ArrayList<BufferedImage>();
            for (int i = 0; i <= 7; i++) {
                File f = new File("data/char_" + i + ".png");
                BufferedImage bi = ImageIO.read(f);
                lista.add(bi);
            }
           
            // load neural network from file
           if (nnet==null){
            NeuralNetwork nnet = NeuralNetwork.createFromFile(neuralNetworkFile);
           }
            // get ocr plugin from neural network
            nnet.addPlugin(new OcrPlugin(new Dimension(10, 10), ColorMode.BLACK_AND_WHITE));
            OcrPlugin ocrPlugin = (OcrPlugin) nnet.getPlugin(OcrPlugin.class);
           
            // and recognize current character - ( have to use ImageJ2SE here to wrap BufferedImage)
            for (int i = 0; i < lista.size(); i++) {
                recognizedCharacters+=ocrPlugin.recognizeCharacter(new ImageJ2SE(lista.get(i))) + " ";
                System.out.print(ocrPlugin.recognizeCharacter(new ImageJ2SE(lista.get(i))) + " ");
            }
            recognizedCharacters.trim();
        } catch (IOException e) {
            //Let us know what happened  
            System.out.println("Error reading dir: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        (new OcrDemo()).run();
    }
}
