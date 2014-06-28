/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.filter.impl;

import imagepreprocessing.filter.ImageFilter;
import imagepreprocessing.helper.PreprocessingHelper;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author Mihailo Stupar
 */
public class SobelEdgeDetection implements ImageFilter,Serializable{
    
    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;
    
    private double [][] sobelX;
    private double [][] sobelY;
    
    private double treshold;
    
    
    @Override
    public BufferedImage processImage(BufferedImage image) {
        
        originalImage = image;
        
        int width = image.getWidth();
        int height = image.getHeight();
        
        filteredImage = new BufferedImage(width, height, image.getType());
        
        treshold = 0.1;
        generateSobelOperators();
        
        double [][] Gx = new double[width][height];
        double [][] Gy = new double[width][height];
        double [][] G = new double[width][height];
        
        double max = 0;
        
        for (int i = 1; i < width-1; i++) {
            for (int j = 1; j < height-1; j++) {
                
                Gx[i][j] = calculateGradient(i, j, sobelX);
                Gy[i][j] = calculateGradient(i, j, sobelY);
                
                G[i][j] = Math.abs(Gx[i][j]) + Math.abs(Gy[i][j]);
                
                if (G[i][j] > max)
                    max = G[i][j];
                
                
            }
            
        }
        
        
        treshold = treshold*max;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                
                int newColor;
                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                
                if (G[i][j] > treshold)
                    newColor = 0;
                else
                    newColor = 255;
               
                int rgb = PreprocessingHelper.colorToRGB(alpha, newColor, newColor, newColor);
                filteredImage.setRGB(i, j, rgb);
                
            }
        }
      
        return filteredImage;
    }
    
    protected void generateSobelOperators () {
        
        sobelX = new double[3][3];
        sobelX [0][0] = -0.25;  sobelX [0][1] = -0.5;  sobelX [0][2] = -0.25;
        sobelX [1][0] = 0;      sobelX [1][1] = 0;     sobelX [1][2] = 0;
        sobelX [2][0] = 0.25;   sobelX [2][1] = 0.5;   sobelX [2][2] = 0.25;
        
        sobelY = new double[3][3];
        sobelY [0][0] = -0.25;  sobelY [0][1] = 0;  sobelY [0][2] = 0.25;
        sobelY [1][0] = -0.5;   sobelY [1][1] = 0;  sobelY [1][2] = 0.5;
        sobelY [2][0] = -0.25;  sobelY [2][1] = 0;  sobelY [2][2] = 0.25;
 
        
        double one = 1;
        double oneThird = one/3;
        
        
        sobelX [0][0] = -oneThird;  sobelX [0][1] = -oneThird;  sobelX [0][2] = -oneThird;
        sobelX [1][0] = 0;          sobelX [1][1] = 0;          sobelX [1][2] = 0;
        sobelX [2][0] = oneThird;   sobelX [2][1] = oneThird;   sobelX [2][2] = oneThird;
        
        sobelY [0][0] = -oneThird;   sobelY [0][1] = 0;  sobelY [0][2] = oneThird;
        sobelY [1][0] = -oneThird;   sobelY [1][1] = 0;  sobelY [1][2] = oneThird;
        sobelY [2][0] = -oneThird;   sobelY [2][1] = 0;  sobelY [2][2] = oneThird;
        
        
    }
    
    protected double calculateGradient (int i, int j, double [][] sobelOperator) {
        
    
    	double sum = 0;
        
        int posX = 0;
        for (int x = i-1; x <= i+1; x++) {
            
            int posY = 0;
            for (int y = j-1; y <= j+1; y++) {
                
                double gray = new Color(originalImage.getRGB(x, y)).getRed();
                
                sum = sum + gray*sobelOperator[posX][posY];
                posY++;
            }
            posX++;
        }
        
        return sum;
    } 

    public void setTreshold(double treshold) {
        this.treshold = treshold;
    }

    @Override
    public String toString() {
        return "Sobel method";
    }
    
    
}