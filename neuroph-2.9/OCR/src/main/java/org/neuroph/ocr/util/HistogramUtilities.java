/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mihailo
 */
public class HistogramUtilities {

    private int letterColor;

    /**
     * Set color of the letters in the image. <br/>
     * For black set the value 0 (zero).<br/>
     * For white set the value 255.<br/>
     * Only these two values are possible
     *
     * @param letterColor
     */
    public HistogramUtilities(int letterColor) {
        this.letterColor = letterColor;
    }

    public HistogramUtilities() {
        letterColor = 0;
    }

    /**
     * Set color of the letters in the image. <br/>
     * For black set the value 0 (zero).<br/>
     * For white set the value 255.<br/>
     * Only these two values are possible
     *
     * @param letterColor
     */
    public void setLetterColor(int letterColor) {
        this.letterColor = letterColor;
    }

    /**
     *
     * @param image binarized image, letters are black, background is white
     * @return array which length is height of image, every element of array
     * represent count of black pixels in that row.
     */
    public int[] colorHistogramHeight(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();

        int[] histogram = new int[height];

        int color;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                color = new Color(image.getRGB(j, i)).getRed();
                if (color == letterColor) {
                    histogram[i]++;
                }
            }
        }
        return histogram;
    }

    /**
     *
     * @param histogram histogram calculated by method
     * blackHistogram(BufferedImage)
     * @return array that represents gradient Each element in array is
     * calculated in the following way:<br/>
     * gradient[i] = histogram[i] - histogram[i-1]
     */
    public int[] gradient(int[] histogram) {
        int[] gradient = new int[histogram.length];
        for (int i = 1; i < gradient.length; i++) {
            gradient[i] = histogram[i] - histogram[i - 1];
        }
        return gradient;
    }

    /**
     *
     * @param gradient gradient array calculated with method gradient(int [])
     * @param ignoredSize - noise - what is the minimum size of letter to be
     * recognized <br/>
     * With lower value you will probably find trash as separate line <br/>
     * With higher value you will probably miss the letter <br/>
     * Ideal value is less that the letter size
     * @return List of integers where each element represent center of line.
     * First element corresponds to the first line etc.
     */
    public List<Integer> linePositions(int[] gradient, int ignoredSize) {
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
                if (count < ignoredSize) {
                    count = 0;
                } else { //count >= lineHeightThresh // found line!
                    int startLetter = row - count;
                    int endLetter = row;
                    int line = (startLetter + endLetter) / 2;
                    lines.add(line);
                    count = 0;
                }
            }
        }
        return lines;

    }

    /**
     * Method for finding histogram but with ignoring spaces between lines/words<br/>
     * @param histogram
     * @param minSpaceGap size of the space
     * @return new histogram with ignored spaces. <br/>
     * Previously in histogram these spaces have had values of zero<br/>
     * Now they are filled with ones: histogram[i] = 1
     */
    public int[] histogramIgnoreMiniSpaces(int[] histogram, int minSpaceGap) {
        int count = 0;
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] == 0) {
                count++;
            } else { //(histogram[i] != 0) drugim recima vece je od nule
//                System.out.println(i+"-"+count);
                if (count > 0 && count < minSpaceGap) {
                    for (int j = i - count; j < i; j++) {
                        histogram[j] = 1;
                    }
                }
                count = 0;
            }

        }
        return histogram;
    }

    /**
     * Calculate the horizontal histogram for single row.
     * @param image input image with multiple lines and letters (optional)
     * @param row pixel position of the row. It should be center of the single row.
     * @param letterSize predicted letter size
     * @return 
     */
    public int[] horizontalHistogram(BufferedImage image, int row, int letterSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        int color;
        
        int[] histogram = new int[width];
        for (int i = 0; i < width; i++) {
            for (int j = row - (letterSize / 2); j <= row + (letterSize / 2); j++) {
                if (j < 0 || j >= height) {
                    continue;
                }
                color = new Color(image.getRGB(i, j)).getRed();
                if (color == letterColor ) {
                    histogram[i]++;
                }
            }

        }
        return histogram;
    }
    
}
