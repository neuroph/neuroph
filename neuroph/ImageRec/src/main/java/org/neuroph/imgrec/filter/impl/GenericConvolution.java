package org.neuroph.imgrec.filter.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.filter.ImageFilter;

/**
 *
 * @author Mihailo Stupar
 */
public class GenericConvolution implements ImageFilter{
    
    private BufferedImage originalImage;
    private BufferedImage filteredImage;

    private double [][] kernel;
    private boolean normalize;

    public GenericConvolution(double[][] kernel) {
        this.kernel = kernel;
    }
    

        
    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());
        
        int radius = kernel.length/2;
        
        if (normalize) {
            normalizeKernel();
        }
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {                
                double result = convolve(x, y, radius);                
                int gray = (int)Math.round(result);                
                int alpha = new Color(originalImage.getRGB(x, y)).getAlpha();
                int rgb = ImageUtilities.colorToRGB(alpha, gray, gray, gray);                
                filteredImage.setRGB(x, y, rgb);
            }
        }
                       
        return filteredImage;
    }

    
    protected double convolve(int x, int y, int radius) {        
        double sum = 0;
        int kernelI = 0;
        for (int i = x-radius; i <= x+radius; i++) {
            int kernelJ = 0;
            for (int j = y-radius; j <= y+radius; j++) {              
                if (i>=0 && i<originalImage.getWidth() && j>0 && j<originalImage.getHeight()) {                
                    int color = new Color(originalImage.getRGB(i, j)).getRed();
                    sum = sum + color*kernel[kernelI][kernelJ];                    
                }
                kernelJ++;
            }
            kernelI++;
        }
        
        return sum;
    } 
 
    /*
    * Mak sure that kernel element sum is 1
    */
    private void normalizeKernel() {
        int n = 0;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                n += kernel[i][j];
            }
            
        }
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                kernel[i][j] = kernel[i][j]/n;
            }
            
        }
    }
    
    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

      public void setKernel(double[][] kernel) {
        if (kernel.length % 2 == 0) {
            System.out.println("ERROR!");
        }
        this.kernel = kernel;
    }    
    
   @Override
    public String toString() {
        return "Generic convolution";
    }    
    
    
    
}
