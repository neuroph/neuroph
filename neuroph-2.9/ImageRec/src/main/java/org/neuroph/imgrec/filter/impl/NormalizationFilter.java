package org.neuroph.imgrec.filter.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.filter.ImageFilter;

/**
 *
 * @author Mihailo Stupar
 * 
 * Filter who improves the quality of handwriting letters. 
 */
public class NormalizationFilter implements ImageFilter{
    
    
    private BufferedImage originalImage;
    private BufferedImage filteredImage;

    private int blockSize = 5; //should be odd number (ex. 5)

    private double GOAL_MEAN = 0;
    private double GOAL_VARIANCE = 1;
    
    private int mean;
    private int var;
    
    private int width;
    private int height;

    private int[][] imageMatrix;

    @Override

    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;

       

        width = originalImage.getWidth();
        height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());
        imageMatrix = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                imageMatrix[i][j] = new Color(originalImage.getRGB(i, j)).getRed();

            }
        }
        
        mean = calculateMean();
        var = calculateVariance();
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                
                double normalizedPixel = 0;
                double squareError = 0;
                
                if (imageMatrix[i][j] > mean) {
                    squareError = (imageMatrix[i][j] - mean)*(imageMatrix[i][j]-mean);
                    normalizedPixel = (GOAL_MEAN + Math.sqrt(((GOAL_VARIANCE * squareError / var))));
                }
                else {
                    squareError = (imageMatrix[i][j] - mean)*(imageMatrix[i][j]-mean);
                    normalizedPixel = (GOAL_MEAN - Math.sqrt(((GOAL_VARIANCE * squareError / var))));
                }
                
                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                
                int rgb = (int)-normalizedPixel;
                
                int color = ImageUtilities.colorToRGB(alpha, rgb, rgb, rgb);
                
                filteredImage.setRGB(i, j, color);
                
            }
        }
        

        return filteredImage;
    }

    /**
     *
     * @param x x coordinate of block
     * @param y y coordinate of block
     * @return
     */
    public int calculateVariance() {

        int var = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                var += (imageMatrix[i][j]-mean)*(imageMatrix[i][j] - mean);
                
            }
        }
        return (int)var/ (height*width*255); //255 for white color
    }

    public int calculateMean() {
        double mean = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0 ; j < height; j++) {
                mean += imageMatrix[i][j];

            }
        }

        return (int) mean / (width*height);
    }

    @Override
    public String toString() {
        return "Normalization Filter";
    }

    public void setGOAL_MEAN(double GOAL_MEAN) {
        this.GOAL_MEAN = GOAL_MEAN;
    }

    public void setGOAL_VARIANCE(double GOAL_VARIANCE) {
        this.GOAL_VARIANCE = GOAL_VARIANCE;
    }

    
    
    
    
    
    
    
}
