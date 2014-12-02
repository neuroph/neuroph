/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.workflow;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.neuroph.imgrec.filter.impl.OCRSeparationFilter;

/**
 *
 * @author Mihailo
 */
public class LetterCreator {

    private String location;
    private String text;
    private BufferedImage image;
    

    

    public LetterCreator(String location) {
        this.location = location+"\\";
        text = Share.getInstance().getText();
        image = Share.getInstance().getImage();
    }

    public void createLetterImage(int letterWidth, int letterHeight) {
        int [] linePosition = linePositions(9);
        OCRSeparationFilter osf = new OCRSeparationFilter(letterWidth, letterHeight, location, text);
        osf.setLinePositions(linePosition);
        osf.setDimension(30, 30);
        osf.processImage(image);
        Share.getInstance().setLetterLabels(osf.getLetterLabels());
    }

    
    public int[] linePositions(int window) {
        int [] histogram = histogram(image);
        int [] gradient = gradient(histogram);
        int [] linePos = linePositions(gradient, window);
        return linePos;
    }
    
    private int [] histogram(BufferedImage imageP) {
        int height = imageP.getHeight();
        int width = imageP.getWidth();
        
        int [] histogram = new int[height];
        
        int color;
        int black = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                color = new Color(imageP.getRGB(j, i)).getRed();
                if (color == black) {
                    histogram[i]++;
                }
            }
        }
        return histogram;
    } 

    private int [] gradient (int [] histogram) {
        int [] gradient = new int [histogram.length];
        for (int i = 1; i < gradient.length; i++) {
            gradient[i] = histogram[i]-histogram[i-1];
        }
        return gradient;
    }
    
    /**
     * 
     * @param gradient 
     * @param window window is actually height of the letter
     * @return 
     */
    private int [] linePositions(int gradient [], int window) {
        ArrayList<Integer> lines = new ArrayList<Integer>();
        int sum = 0;
        int count = 0;
        for (int i = 0; i < gradient.length; i++) {
            sum += gradient[i];
            if (sum != 0) {
                count++;
                continue;
            }
            if (sum == 0) {
                if (count < window) {
                    count = 0;
                }
                else { //count >= window
                    int startLetter = i-count;
                    int endLetter = i;
                    int line = (startLetter+endLetter)/2;
                    lines.add(line);
                    count=0;
                }
            }
        } 
        return list2array(lines);
    }
    
    private int [] list2array(ArrayList<Integer> list) {
        int [] array = new int[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
            
        }
        return array;
    }
    
    
    
    
    
    
}
