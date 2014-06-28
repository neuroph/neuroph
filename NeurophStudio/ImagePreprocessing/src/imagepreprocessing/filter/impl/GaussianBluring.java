/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.filter.impl;

import imagepreprocessing.helper.PreprocessingHelper;
import imagepreprocessing.filter.ImageFilter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author Mihailo Stupar
 */

//http://www.swageroo.com/wordpress/how-to-program-a-gaussian-blur-without-using-3rd-party-libraries/
public class GaussianBluring implements ImageFilter,Serializable{
    
    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;

    private int radius;
    private double sigma;
    
    private double [][] kernel;

    public GaussianBluring() {
        radius = 7;
        sigma = 10;
    }
    
    
    
    
    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;
        
        int oldWidth = image.getWidth();
        int oldHeight = image.getHeight();
        
        int width = image.getWidth() - 2*radius;
        int height = image.getHeight() - 2*radius;
        
        
        
        
        
        filteredImage = new BufferedImage(width, height, originalImage.getType());
        
        createKernel();
        

        for (int i = radius; i < oldWidth - radius; i++) {
            for (int j = radius; j < oldHeight - radius; j++) {
                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                int newColor = getNewColor(i, j);
                int rgb = PreprocessingHelper.colorToRGB(alpha, newColor, newColor, newColor);
                
                int x = i-radius;
                int y = j-radius;
                filteredImage.setRGB(x,y, rgb);
                
            }
        }
  
        
        return filteredImage;
    }

    
    
    
    protected void createKernel() {
        
        int size = radius*2 + 1;
        int center = radius;
        kernel = new double [size][size];
        
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                int distanceX = Math.abs(center - i);
                int distanceY = Math.abs(center - j);
                kernel [i][j] = gaussianFormula(distanceX, distanceY);
            }
        }
        
        double noralizationValue = getNormalizationValue(kernel);
        
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                kernel[i][j] = kernel[i][j]*noralizationValue;
            }
        }
    
    }
    
    public double gaussianFormula (double x, double y) {
        double one = 1.0;
        double value = one/(2*Math.PI*sigma*sigma);
        double exp = -(x*x+y*y)/(2*sigma*sigma);
        exp = Math.pow(Math.E, exp);
        value = value*exp;
        return value;
    }
    
    public double getNormalizationValue (double[][] kernel) {
        double sum = 0;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                sum = sum + kernel [i][j];
            }
        }
        double one = 1.0;
        return one/sum;
    }
 
    public int getNewColor (int x, int y) {
        if (!checkConditios(x, y)) {
            return new Color(originalImage.getRGB(x, y)).getRed();
        }
        
        int size = 2*radius+1;
        double [][] matrix = new double [size][size];
        
        int newI = 0;
        int newJ = 0;
        for (int i = x-radius; i <= x+radius; i++) {
            for (int j = y-radius; j <= y+radius; j++) {
                
                //System.out.println("size:" +size+", radius:"+radius+", x:"+x+",y:"+y+", i:"+i+",j:"+j+ ", newI:"+newI+"newJ:"+newJ);
                int oldColor = new Color(originalImage.getRGB(i, j)).getRed();
                matrix[newI][newJ] = oldColor * kernel[newI][newJ];
                newJ++;
            }
            newI++;
            newJ=0;
        }
        
        double sum = 0;
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                sum = sum + matrix[i][j];
            }
        }
        
        return (int) Math.round(sum);
        
        
        
        
    }
   
    public boolean checkConditios (int x, int y) {
        if (x-radius >= 0 && x+radius < originalImage.getWidth() && y-radius >= 0 && y+radius < originalImage.getHeight())
            return true;
        return false;
    }
    
    
    @Override
    public String toString() {
        return "Gaussian bluring";
    }
   
    public void setRadius(int radius) {
        if (radius % 2 != 0) 
            this.radius = radius;
        this.radius = radius - 1;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }
    
    

    
    
    
}
