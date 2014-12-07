/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.workflow;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import javax.imageio.ImageIO;
import org.neuroph.imgrec.FractionHSLData;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.filter.ImageFilterChain;
import org.neuroph.imgrec.filter.impl.GrayscaleFilter;
import org.neuroph.imgrec.filter.impl.LetterSeparationFilter;
import org.neuroph.imgrec.filter.impl.OCRSeparationFilter;
import org.neuroph.imgrec.image.Dimension;

/**
 *
 * @author Mihailo
 */
public class Test {

    public static void main(String[] args) throws IOException {

        
        String pathImage = "C:/Users/Mihailo/Desktop/OCR workflow/part_75.png";
        BufferedImage image = ImageIO.read(new File(pathImage));

        //Reader.readImage(pathImage);
        String pathText = "C:/Users/Mihailo/Desktop/OCR workflow/part_75.txt";
        Path path = FileSystems.getDefault().getPath(pathText);
        try {
            String text = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            System.err.println("File not found.");
        }
        
        
        
//        Share.getInstance().setImage(image);
//        Reader.readText(pathText);

        ImageFilterChain filterChain = new ImageFilterChain();
        filterChain.addFilter(new GrayscaleFilter());
        filterChain.addFilter(new LetterSeparationFilter());
        BufferedImage binarizedImage = filterChain.processImage(image); // output: binrizovna slka, sa belom izmedju slova

        LinePositions linePos = new LinePositions(binarizedImage);
        linePos.setLineHeightThresh(9);  // 
        int [] linePositions = linePos.findLinePositions();
        
        String locationFolder = "C:\\Users\\Mihailo\\Desktop\\OCR workflow\\letters";
        OCRSeparationFilter separation = new OCRSeparationFilter();
        separation.setDimension(30, 30);
        separation.setLinePositions(linePositions);
        separation.setLocationFolder(locationFolder);
//        separation.setLocationFolder(locationFolder);
        separation.processImage(binarizedImage);
        
        
        
        System.out.println("Creating letters...");
//        String locationFolder = "C:\\Users\\Mihailo\\Desktop\\OCR workflow\\letters";
//        LetterCreator letterCreator = new LetterCreator(locationFolder);
//        letterCreator.createLetterImages(80, 80); //dimensions of the image

//        ArrayList<String> letterLabels = Share.getInstance().getLetterLabels();

        Map<String, FractionHSLData> map = ImageRecognitionHelper.getFractionHSLDataForDirectory(new File(locationFolder), new Dimension(20, 20));
    }

}
