/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagepreprocessing.filter.impl;

import imagepreprocessing.filter.ImageFilter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;




/**
 *
 * @author Sanja
 */
public class EdgeDetection implements ImageFilter,Serializable {

    int width;
    int height;
    transient  BufferedImage  originalImage;
    transient BufferedImage filteredImage;

    @Override
    public BufferedImage processImage(BufferedImage image) {
        originalImage = image;
        setAttributes(image);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());

        int[][] filter1 = {{-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}
        };
        int[][] filter2 = {{1, 2, 1},
        {0, 0, 0},
        {-1, -2, -1}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                // get 3-by-3 array of colors in neighborhood
                int[][] gray = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray[i][j] = (int) lum(new Color(originalImage.getRGB(x - 1 + i, y - 1 + j)));
                    }
                }

                // apply filter
                int gray1 = 0, gray2 = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray1 += gray[i][j] * filter1[i][j];
                        gray2 += gray[i][j] * filter2[i][j];
                    }
                }
                // int magnitude = 255 - truncate(Math.abs(gray1) + Math.abs(gray2));
                int magnitude = 255 - truncate((int) Math.sqrt(gray1 * gray1 + gray2 * gray2));
                Color grayscale = new Color(magnitude, magnitude, magnitude);
                filteredImage.setRGB(x, y, grayscale.getRGB());

            }
        }
        return filteredImage;
    }

    /**
     * Truncate color component to be between 0 and 255.
     * @param a
     * @return 
     */
    public static int truncate(int a) {
        if (a < 0) {
            return 0;
        } else if (a > 255) {
            return 255;
        } else {
            return a;
        }
    }

    private void setAttributes(BufferedImage image) {
        //this.originalImage = image;
        this.height = originalImage.getHeight();
        this.width = originalImage.getWidth();
    }
    
    /**
     * Return the  luminance of a given color
     * @param color - color of one pixel in image. 
     * @return - luminance of a color
     */
    public double lum(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return .299*r + .587*g + .114*b;
    }
    
    @Override
    public String toString() {
        return "Edge Detection";
    }

}
