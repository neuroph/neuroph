/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.ocr.workflow;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * 
 * @author Mihailo
 */
public class LinePositions {
    
    private BufferedImage image;
    private int lineHeightThresh;

    public LinePositions(BufferedImage image) {
        this.image = image;
        lineHeightThresh = 0;
    }

    public LinePositions() {
        lineHeightThresh = 0;
    }

    /**
     * The height of each letter in the text measured in pixels
     * @param lineHeightThresh 
     */
    // Treba dodati koja vrednost ide z akoju velicinu slova ako je slika skenirana sa 300bpi
    public void setLineHeightThresh(int lineHeightThresh) {
        this.lineHeightThresh = lineHeightThresh;
    }
    
    

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    /**
     * Calculate the pixel position of each line in document
     * @return array of pixels positions
     */
    public int [] findLinePositions() {
        int [] blackHistogram = blackHistogramByHeight();
        int [] gradients = calculateHistogramGradients(blackHistogram);
        int [] linePos = findLinePositions(gradients);
        return linePos;
    }
    
    // return blackHistogram of black pixes by imageheight
    // assumes input imge is binaried image
    private int [] blackHistogramByHeight() {
        int height = image.getHeight();
        int width = image.getWidth();
        
        int [] blackHistogram = new int[height];
        
        int color;
        int black = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                color = new Color(image.getRGB(j, i)).getRed();
                if (color == black) {
                    blackHistogram[i]++;
                }
            }
        }
        return blackHistogram;
    } 
    
    // calculas radient as a difference between two neighbouring elemements (lines)
    private int [] calculateHistogramGradients (int [] blackHistogram) {
        int [] gradient = new int [blackHistogram.length];
        for (int i = 1; i < gradient.length; i++) {
            gradient[i] = blackHistogram[i]-blackHistogram[i-1];
        }
        return gradient;
    }
    
    
    /**
     * @param gradient 
     * @param lineHeightThresh lineHeightThresh is actually height of the letter
     * @return 
     */
    private int [] findLinePositions(int gradient []) {
        ArrayList<Integer> lines = new ArrayList<Integer>();
        int sum = 0;
        int count = 0;
        for (int row = 0; row < gradient.length; row++) {
            sum += gradient[row];
            if (sum != 0) {
                count++;
                continue;
            }
            if (sum == 0) {
                if (count < lineHeightThresh) {
                    count = 0;
                }
                else { //count >= lineHeightThresh // found line!
                    int startLetter = row-count;
                    int endLetter = row;
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
