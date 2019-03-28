/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.util.histogram;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Mihailo
 */
public class Histogram {
    
    /**
     * @param image binarized image, letters are black, background is white
     * @return array which length is height of image, every element of array
     * represent count of black pixels in that row.
     */
    public static int[] heightHistogram(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();

        int[] histogram = new int[height];
        int black = 0;
        int color;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                color = new Color(image.getRGB(j, i)).getRed();
                if (color == black) {
                    histogram[i]++;
                }
            }
        }
        return histogram;
    }
    
    
    /**
     * @param image binarized image, letters are black, background is white
     * @return array which length is width of image, every element of array
     * represent count of black pixels in that column of pixels.
     */
    public static int[] widthHistogram(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();

        int[] histogram = new int[width];
        int black = 0;
        int color;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                color = new Color(image.getRGB(i, j)).getRed();
                if (color == black) {
                    histogram[i]++;
                }
            }
        }
        return histogram;
    }
    
    /**
     * @param histogram histogram calculated by method
     * <b>heightHistogram(BufferedImage)</b> or 
     * <b>widthHistogram(BufferedImage)</b>
     * @return array that represents gradient Each element in array is
     * calculated in the following way:<br/>
     * gradient[i] = histogram[i] - histogram[i-1]
     */
    public static int[] gradient(int[] histogram) {
        int[] gradient = new int[histogram.length];
        for (int i = 1; i < gradient.length; i++) {
            gradient[i] = histogram[i] - histogram[i - 1];
        }
        return gradient;
    }
    
}
