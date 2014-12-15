/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.ocr.samples;

import org.neuroph.ocr.LinePositions;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.neuroph.imgrec.filter.ImageFilterChain;
import org.neuroph.imgrec.filter.impl.GrayscaleFilter;
import org.neuroph.imgrec.filter.impl.LetterSeparationFilter;
import org.neuroph.imgrec.filter.impl.OCRSeparationFilter;

/**
 *
 * @author Mihailo
 */
public class OCRNeuroph {

    
    public static void main(String[] args) throws IOException{
        
        // Image with text
        String pathImage = "C:/Users/Mihailo/Desktop/OCR workflow/part_75.png";
        BufferedImage image = ImageIO.read(new File(pathImage));

        // File with text that corresponds with text in image
        String pathText = "C:/Users/Mihailo/Desktop/OCR workflow/part_75.txt";
        Path path = FileSystems.getDefault().getPath(pathText);
        String text = new String(Files.readAllBytes(path));
       
        
        ImageFilterChain filterChain = new ImageFilterChain();
        filterChain.addFilter(new GrayscaleFilter());
        filterChain.addFilter(new LetterSeparationFilter());
        BufferedImage binarizedImage = filterChain.processImage(image); // output: binrizovna slka, sa belom izmedju slova

        // Positions of each line measured in pixels
        LinePositions linePos = new LinePositions(binarizedImage);
        linePos.setLineHeightThresh(9);  // eacl letter should not be smaller than 9px
        int[] linePositions = linePos.findLinePositions();

        // In locationFolder will be segmented letters as separated images
        String locationFolder = "C:\\Users\\Mihailo\\Desktop\\OCR workflow\\letters\\";
        OCRSeparationFilter separation = new OCRSeparationFilter();
        separation.setDimension(30, 30); // each letter will be in single image, dimension of the image
        separation.setLinePositions(linePositions);
        separation.setLocationFolder(locationFolder);
        separation.setText(text);
        separation.processImage(binarizedImage);
        
    }
    
}
